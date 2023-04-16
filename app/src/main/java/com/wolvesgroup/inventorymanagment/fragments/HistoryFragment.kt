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
import com.wolvesgroup.inventorymanagment.R
import com.wolvesgroup.inventorymanagment.databinding.FragmentAlertsBinding
import com.wolvesgroup.inventorymanagment.databinding.FragmentHistoryBinding
import com.wolvesgroup.inventorymanagment.utils.adapters.AlertsAdapter
import com.wolvesgroup.inventorymanagment.utils.adapters.HistoryAdapter
import com.wolvesgroup.inventorymanagment.utils.models.AlertModel
import com.wolvesgroup.inventorymanagment.utils.models.HistoryModel


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    val ref = FirebaseDatabase.getInstance().getReference("History")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        ref.addValueEventListener(eventListener);

        return binding.root
    }

    private val eventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val historyList: ArrayList<HistoryModel> = ArrayList()
            for (ds in dataSnapshot.getChildren()) {
                val model: HistoryModel? = ds.getValue(HistoryModel::class.java)
                if (model != null) {
                    historyList.add(model)
                }
            }

            val adapter = HistoryAdapter(requireContext(), historyList as ArrayList<HistoryModel>)
            binding.rvHistory.setAdapter(adapter)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Toast.makeText(requireContext(), "" + databaseError.message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}