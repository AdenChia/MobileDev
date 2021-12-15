package com.example.hotayisstore.Home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.hotayisstore.R
import com.example.hotayisstore.Data.Entity.Item
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.all_item_list.view.*

class ItemAdapter(private var context: Context) : RecyclerView.Adapter<ItemAdapter.MyViewHolder>() {

    private lateinit var mListener :onItemClickListener

    //create onClick function in adapter
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    private var itemsList = emptyList<Item>()
    //All Item View Holder
    class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.all_item_list, parent, false), mListener)
    }

    //set adapter List info
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = itemsList[position]
        holder.itemView.textViewItemName.text = currentItem.Item_Name
        holder.itemView.textViewItemID.text = currentItem.Item_ID
        holder.itemView.textViewItemQty.text = currentItem.Quantity.toString()

        holder.itemView.buttonReport.setOnClickListener{view->

            val id :String = currentItem.Item_ID
            val intent = Intent(context, ReportActivity::class.java)
            intent.putExtra("idReport", id)
            Snackbar.make(view, "$id", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            context.startActivities(arrayOf(intent))
        }
    }

    fun getCurrentItemID(position: Int): String {
        val currentItem = itemsList[position]
        return currentItem.Item_ID
    }

    //size in the adapter list
    override fun getItemCount(): Int {
        return itemsList.count()
    }

    fun setData(items: List<Item>){
        this.itemsList = items
        notifyDataSetChanged()
    }

}