package com.wolvesgroup.inventorymanagment.utils.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase
import com.wolvesgroup.inventorymanagment.R
import com.wolvesgroup.inventorymanagment.databinding.AddProductBottomSheetBinding
import com.wolvesgroup.inventorymanagment.databinding.UpdateInfoBsBinding
import com.wolvesgroup.inventorymanagment.utils.models.AlertModel
import com.wolvesgroup.inventorymanagment.utils.models.ProductModel


class InventoryAdapter(context: Context, list: ArrayList<ProductModel>) :
    RecyclerView.Adapter<InventoryAdapter.MyViewHolder>() {
    var context: Context
    var list: ArrayList<ProductModel>

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txt_name.text = list[position].name
        holder.txt_quantity.text = list[position].quantity + " pieces"

        holder.itemView.setOnClickListener {
            val view1 = LayoutInflater.from(context).inflate(R.layout.update_info_bs, null)
            val bsBinding = UpdateInfoBsBinding.bind(view1)

            bsBinding.root.findViewById<EditText>(R.id.edt_product_name_edit).setText(list[position].name)
            bsBinding.root.findViewById<EditText>(R.id.edt_product_quantity_edit).setText(list[position].quantity)
            bsBinding.root.findViewById<EditText>(R.id.edt_product_price_edit).setText(list[position].price)
            bsBinding.root.findViewById<EditText>(R.id.edt_product_desc_edit).setText(list[position].desc)

            val bottomSheetDialog = BottomSheetDialog(context)
            bottomSheetDialog.setContentView(view1)
            bottomSheetDialog.show()

            bsBinding.btnSaveProductEdit.setOnClickListener {
                val txt_new_name = bsBinding.edtProductNameEdit.text.toString().trim()
                val txt_quantity_new = bsBinding.edtProductQuantityEdit.text.toString().trim()
                val txt_price_new = bsBinding.edtProductPriceEdit.text.toString().trim()
                val txt_desc_new = bsBinding.edtProductDescEdit.text.toString().trim()

                if (txt_new_name != "" && txt_quantity_new != ""){

                    val model = ProductModel(txt_new_name, txt_quantity_new, txt_price_new, list[position].sales_num, txt_desc_new, list[position].key)

                    FirebaseDatabase.getInstance().getReference("Inventory").child(list[position].key).setValue(model).addOnCompleteListener { task ->

                        if (task.isSuccessful){
                            Toast.makeText(context, "Edited!", Toast.LENGTH_SHORT).show()
                            bottomSheetDialog.dismiss()
                        } else {
                            Toast.makeText(context, "Unexpected error occurred!", Toast.LENGTH_SHORT).show()
                            bottomSheetDialog.dismiss()
                        }
                    }

                } else {
                    Toast.makeText(context, "Fill in all the info!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
            builder.setMessage("Do you really want to delete ${list[position].name}?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                FirebaseDatabase.getInstance().getReference("Inventory").child(list[position].key)
                    .removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(context, "Unexpected error occurred!", Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()
                    }
                }
                Unit
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
            true
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_name: TextView
        var txt_quantity: TextView

        init {
            txt_name = itemView.findViewById(R.id.txt_resource_name)
            txt_quantity = itemView.findViewById(R.id.txt_resource_quantity)
        }
    }
}
