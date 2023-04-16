package com.wolvesgroup.inventorymanagment.utils.adapters

import com.wolvesgroup.inventorymanagment.utils.models.AlertModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wolvesgroup.inventorymanagment.R
import com.wolvesgroup.inventorymanagment.utils.models.HistoryModel


class HistoryAdapter(context: Context, list: ArrayList<HistoryModel>) :
    RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {
    var context: Context
    var list: ArrayList<HistoryModel>

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txt_name.text = list[position].name
        holder.txt_quantity.text = list[position].quantity + " pieces"
        holder.txt_time.text = list[position].date
        holder.txt_profit.text = "$"+list[position].profit
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_name: TextView
        var txt_quantity: TextView
        var txt_profit: TextView
        var txt_time: TextView

        init {
            txt_name = itemView.findViewById(R.id.txt_history_name)
            txt_quantity = itemView.findViewById(R.id.txt_history_quantity)
            txt_profit = itemView.findViewById(R.id.txt_history_profit)
            txt_time = itemView.findViewById(R.id.txt_history_date)
        }
    }
}
