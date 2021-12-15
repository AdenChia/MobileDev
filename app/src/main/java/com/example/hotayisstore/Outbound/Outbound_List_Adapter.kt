package com.example.hotayisstore.Outbound

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotayisstore.R

class Outbound_List_Adapter(private val oList: List<Outbound_ViewModel>) : RecyclerView.Adapter<Outbound_List_Adapter.ViewHolder>() {

    private lateinit var mListener : onOutboundClickListener

    //onClick function in adapter button list
    interface onOutboundClickListener{
        fun onOutboundClick(position : Int)
    }

    fun setOnOutboundClickListener(listener : onOutboundClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Outbound_List_Adapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.outbound_card_layout,parent,false)
        return ViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: Outbound_List_Adapter.ViewHolder, position: Int) {

        holder.buttonSelectOutbound.isEnabled = false
        val outbound_ViewModel = oList[position]
        //Set View Text in Card
        holder.textViewOutboundID.text = outbound_ViewModel.outboundID
        holder.textViewStatus.text = outbound_ViewModel.status

        //only Status is I-Incomplete can click the button
        if(holder.textViewStatus.text ==  "I"){
            holder.buttonSelectOutbound.isEnabled = true
        }

    }

    override fun getItemCount(): Int {
        return oList.size
    }

    class ViewHolder(OutboundView : View, listener : onOutboundClickListener) : RecyclerView.ViewHolder(OutboundView){
        val textViewOutboundID: TextView = itemView.findViewById(R.id.textViewOutboundID)
        val textViewStatus:TextView = itemView.findViewById(R.id.textViewStatus)
        val buttonSelectOutbound: Button = itemView.findViewById(R.id.buttonSelectOutbound)
        init {
            buttonSelectOutbound.setOnClickListener {
                listener.onOutboundClick(adapterPosition)
            }

        }
    }

}
