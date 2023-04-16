package com.wolvesgroup.inventorymanagment.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wolvesgroup.inventorymanagment.databinding.FragmentAlertsBinding
import com.wolvesgroup.inventorymanagment.utils.adapters.AlertsAdapter
import com.wolvesgroup.inventorymanagment.utils.adapters.InventoryAdapter
import com.wolvesgroup.inventorymanagment.utils.models.AlertModel

class AlertsFragment : Fragment() {

    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!

    val ref = FirebaseDatabase.getInstance().getReference("Alerts")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAlertsBinding.inflate(inflater, container, false)

        ref.addValueEventListener(eventListener);


        return binding.root
    }

    private val eventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val alertList: ArrayList<AlertModel> = ArrayList()
            for (ds in dataSnapshot.getChildren()) {
                val model: AlertModel? = ds.getValue(AlertModel::class.java)
                if (model != null) {
                    alertList.add(model)
                }
            }

            val filteredList = alertList.filter { it.quantityThen.toInt() < 50 }

            val adapter = AlertsAdapter(requireContext(), filteredList as ArrayList<AlertModel>)
            binding.rvAlerts.setAdapter(adapter)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Toast.makeText(requireContext(), "" + databaseError.message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}