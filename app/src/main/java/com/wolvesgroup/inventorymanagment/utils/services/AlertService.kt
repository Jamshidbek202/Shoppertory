package com.wolvesgroup.inventorymanagment.utils.services

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.*
import com.wolvesgroup.inventorymanagment.MainActivity
import com.wolvesgroup.inventorymanagment.utils.models.AlertModel
import com.wolvesgroup.inventorymanagment.utils.models.ProductModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class AlertService : Service() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener

    override fun onCreate() {
        super.onCreate()

        databaseRef = FirebaseDatabase.getInstance().getReference("Inventory")

        valueEventListener = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {

                for (ds in snapshot.children){
                    val model: ProductModel? = ds.getValue(ProductModel::class.java)
                    if (model != null) {

                        if (model.quantity.toInt() <= 50){
                            val ref = FirebaseDatabase.getInstance().getReference("Alerts").child(model.key)

                            val currentDateTime = LocalDateTime.now()
                            val formatter = DateTimeFormatter.ofPattern("HH-MMMM-yyyy")
                            val time = currentDateTime.format(formatter)
                            val key = UUID.randomUUID().toString()

                            val model = AlertModel(model.name, time, key, model.quantity)

                            ref.setValue(model)

                            // Create a notification channel
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val name = "Inventory Alerts"
                                val descriptionText = "You are running out of a product!"
                                val importance = NotificationManager.IMPORTANCE_DEFAULT
                                val channel = NotificationChannel("channel001", name, importance).apply {
                                    description = descriptionText
                                }

                                val notificationManager: NotificationManager =
                                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                notificationManager.createNotificationChannel(channel)
                            }

                            val builder = NotificationCompat.Builder(applicationContext, "channel001")
                                .setSmallIcon(R.drawable.notification_icon_background)
                                .setContentTitle("Inventory Alert")
                                .setContentText("You are running out of a product! Check the alert tab for more info.")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                            val intent = Intent(applicationContext, MainActivity::class.java)
                            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                            builder.setContentIntent(pendingIntent)

// Show the notification
                            with(NotificationManagerCompat.from(applicationContext)) {
                                // notificationId is a unique int for each notification that you must define
                                if (ActivityCompat.checkSelfPermission(
                                        applicationContext,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return
                                }
                                notify(1, builder.build())
                            }

                        }

                        if (model.quantity.toInt() > 50){
                            try {

                                FirebaseDatabase.getInstance().getReference("Alerts").child(model.key).removeValue()

                            } catch (e: java.lang.Exception){
                                    //do nothing
                            }
                        }

                    } else {
                        Toast.makeText(applicationContext, "Model: null!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error getting information from the database.", Toast.LENGTH_SHORT).show()
            }
        }

        databaseRef.addValueEventListener(valueEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()

        databaseRef.removeEventListener(valueEventListener)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

