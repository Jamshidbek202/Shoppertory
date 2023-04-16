package com.wolvesgroup.inventorymanagment.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wolvesgroup.inventorymanagment.MainActivity
import com.wolvesgroup.inventorymanagment.databinding.ActivityLoginBinding
import com.wolvesgroup.inventorymanagment.utils.models.LoginModel

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val txt_phone = binding.edtPhone.text.toString().trim()
            val txt_password = binding.edtPassword.text.toString().trim()

            val ref = FirebaseDatabase.getInstance().getReference("Users")

            if (txt_phone.isNotEmpty() && txt_password.isNotEmpty()) {

                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (ds in snapshot.children) {
                            val model: LoginModel? = ds.getValue(LoginModel::class.java)
                            if (model != null) {
                                if (txt_phone == model.phone && txt_password == model.password){
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    Toast.makeText(this@LoginActivity, "Access granted!", Toast.LENGTH_SHORT).show()
                                    startActivity(intent)
                                    finish()
                                    break
                                } else {
                                    Toast.makeText(this@LoginActivity, "Access denied!", Toast.LENGTH_SHORT).show()
                                    break
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@LoginActivity, "" + error.message, Toast.LENGTH_SHORT).show()
                    }
                })

            } else {
                Toast.makeText(this, "Fields are empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}