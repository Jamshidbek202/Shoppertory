package com.wolvesgroup.inventorymanagment.utils.adapters

import com.wolvesgroup.inventorymanagment.utils.models.AlertModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wolvesgroup.inventorymanagment.R


class AlertsAdapter(context: Context, list: ArrayList<AlertModel>) :
    RecyclerView.Adapter<AlertsAdapter.MyViewHolder>() {
    var context: Context
    var list: ArrayList<AlertModel>

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.alert_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txt_name.text = list[position].productName
        holder.txt_quantity.text = list[position].quantityThen
        holder.txt_time.text = list[position].time
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_name: TextView
        var txt_quantity: TextView
        var txt_time: TextView

        init {
            txt_name = itemView.findViewById(R.id.txt_notification_product_name)
            txt_quantity = itemView.findViewById(R.id.txt_quantity_left)
            txt_time = itemView.findViewById(R.id.txt_alert_time)
        }
    }
}
