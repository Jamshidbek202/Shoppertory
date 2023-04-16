package com.wolvesgroup.inventorymanagment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wolvesgroup.inventorymanagment.databinding.AddProductBottomSheetBinding
import com.wolvesgroup.inventorymanagment.databinding.FragmentInventoryManagementBinding
import com.wolvesgroup.inventorymanagment.utils.adapters.InventoryAdapter
import com.wolvesgroup.inventorymanagment.utils.models.ProductModel
import java.util.UUID


class InventoryManagementFragment : Fragment() {

    private var _binding: FragmentInventoryManagementBinding? = null
    private val binding get() = _binding!!

    val ref = FirebaseDatabase.getInstance().getReference("Inventory")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInventoryManagementBinding.inflate(inflater, container, false)

        ref.addValueEventListener(eventListener);

        binding.btnAddProduct.setOnClickListener {

            val bs_binding = AddProductBottomSheetBinding.inflate(layoutInflater)
            val view1 = bs_binding.root

            val bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(view1)
            bottomSheetDialog.show()

            bs_binding.btnSaveProduct.setOnClickListener {

                val txt_name = bs_binding.edtProductName.text.toString().trim()
                val txt_quantity = bs_binding.edtProductQuantity.text.toString().trim()
                val txt_desc = bs_binding.edtProductDesc.text.toString().trim()
                val txt_price = bs_binding.edtProductPrice.text.toString().trim()
                val key = UUID.randomUUID().toString()

                if (txt_name != "" && txt_quantity != "" && txt_desc != "" && txt_price != ""){

                    val model = ProductModel(txt_name, txt_quantity, txt_price, "0", txt_desc, key)

                    val dbRef = FirebaseDatabase.getInstance().getReference("Inventory").child(key)

                    dbRef.setValue(model).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
                            bottomSheetDialog.dismiss()
                        } else {
                            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                            bottomSheetDialog.dismiss()
                        }
                    }

                } else {
                    Toast.makeText(context, "Fields are empty!", Toast.LENGTH_SHORT).show()
                }
            }
        }


        return binding.root
    }

    private val eventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val mResourceList: ArrayList<ProductModel> = ArrayList()
            for (ds in dataSnapshot.getChildren()) {
                val model: ProductModel? = ds.getValue(ProductModel::class.java)
                if (model != null) {
                    mResourceList.add(model)
                }
            }
            val adapter = InventoryAdapter(requireContext(), mResourceList)
            binding.rvProducts.setAdapter(adapter)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Toast.makeText(requireContext(), "" + databaseError.message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}