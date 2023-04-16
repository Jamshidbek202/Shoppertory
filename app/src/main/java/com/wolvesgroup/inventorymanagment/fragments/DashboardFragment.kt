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
import com.wolvesgroup.inventorymanagment.databinding.FragmentDashboardBinding
import com.wolvesgroup.inventorymanagment.utils.adapters.AlertsAdapter
import com.wolvesgroup.inventorymanagment.utils.models.AlertModel
import com.wolvesgroup.inventorymanagment.utils.models.ProductModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val product_ref = FirebaseDatabase.getInstance().getReference("Inventory")
    private val revenue_ref =
        FirebaseDatabase.getInstance().getReference("Statistics/total_revenue")
    private val sales_ref = FirebaseDatabase.getInstance().getReference("Statistics/total_sales")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        product_ref.addValueEventListener(eventListener)

        revenue_ref.addValueEventListener(r_eventListener)

        sales_ref.addValueEventListener(s_eventListener)

        return binding.root
    }

    private val eventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val productList: ArrayList<ProductModel> = ArrayList()
            for (ds in dataSnapshot.getChildren()) {
                val model: ProductModel? = ds.getValue(ProductModel::class.java)
                if (model != null) {
                    productList.add(model)
                }
            }

            val most_sales_model = productList.maxByOrNull { it.sales_num }
            binding.txtMostSoldName.text = most_sales_model?.name
            binding.txtMostSoldQuantity.text = most_sales_model?.quantity + " pieces"
            binding.txtMostSoldSales.text = most_sales_model?.sales_num + " sales"

            val least_sales_model = productList.minByOrNull { it.sales_num }
            binding.txtLeastSoldName.text = least_sales_model?.name
            binding.txtLeastSoldQuantity.text = least_sales_model?.quantity + " pieces"
            binding.txtLeastSoldSales.text = least_sales_model?.sales_num + " sales"
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Toast.makeText(requireContext(), "" + databaseError.message, Toast.LENGTH_SHORT).show()
        }
    }

    private val r_eventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            val txt_total_revenue = snapshot.getValue(String::class.java)
            binding.txtTotalProfit.text = "+" + txt_total_revenue + "$"

        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(requireContext(), "" + error.message, Toast.LENGTH_SHORT).show()
        }

    }

    private val s_eventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            val txt_total_sales = snapshot.getValue(String::class.java)
            binding.txtSalesNum.text = txt_total_sales + " sales"

        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(requireContext(), "" + error.message, Toast.LENGTH_SHORT).show()
        }

    }
}