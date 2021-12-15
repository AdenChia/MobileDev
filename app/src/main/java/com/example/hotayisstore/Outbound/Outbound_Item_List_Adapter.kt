package com.example.hotayisstore.Outbound

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotayisstore.R

class Outbound_Item_List_Adapter(private val oiList: List<Outbound_Item_ViewModel>) : RecyclerView.Adapter<Outbound_Item_List_Adapter.ViewHolder>() {

    private lateinit var oiListener : onOutboundItemClickListener

    //onClick function on button in Adapter
    interface onOutboundItemClickListener{
        fun onOutboundItemClick(position : Int)
    }

    fun setOnOutboundItemClickListener(listener : onOutboundItemClickListener){
        oiListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.outbound_item_card_layout,parent,false)
        return ViewHolder(view,oiListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val outbound_Item_ViewModel = oiList[position]
        holder.textViewItem_ID_Outbound.text = outbound_Item_ViewModel.oItem_Id
        holder.textViewQuantity.text = outbound_Item_ViewModel.oItem_Quantity

        //completed collect item will disable the button in adapter list
        if(outbound_Item_ViewModel.oStatus == "C"){
            holder.textViewCollected.text = "Item Collected"
            holder.buttonScanOI.isEnabled = false
            holder.buttonScanOI.text = "DONE"
        }

    }

    override fun getItemCount(): Int {
        return oiList.size
    }

    class ViewHolder(OutboundItemView : View, listener: onOutboundItemClickListener) : RecyclerView.ViewHolder(OutboundItemView){
        val textViewItem_ID_Outbound: TextView = itemView.findViewById(R.id.textViewItem_ID_Outbound)
        val textViewQuantity:TextView = itemView.findViewById(R.id.textViewQuantity)
        val buttonScanOI: Button = itemView.findViewById(R.id.buttonScanOI)
        val textViewCollected:TextView = itemView.findViewById(R.id.textViewCollected)

        init {
            buttonScanOI.setOnClickListener {
                listener.onOutboundItemClick(adapterPosition)
            }
        }
    }
}