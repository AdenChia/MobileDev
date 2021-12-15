package com.example.hotayisstore.Outbound

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotayisstore.MyApplication
import com.example.hotayisstore.R
import com.example.hotayisstore.Data.ViewModel.OutboundViewModel


class OutboundFragment : Fragment() {

    private var ctx: Context? = null
    private var self: View? = null

    private val outboundViewModel: OutboundViewModel by viewModels {
        OutboundViewModel.OutboundViewModelFactory((activity?.application as MyApplication).outboundRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        ctx = container?.context
        self = LayoutInflater.from(ctx).inflate(R.layout.fragment_outbound, container, false)

        //getting recycleView by id
        val recycleView = self?.findViewById<RecyclerView>(R.id.recycleViewOutboundItem)
        val imageButtonDisplayStatus = self?.findViewById<ImageButton>(R.id.imageButtonDisplayStatus)

        //create a vertical layout Manager
        recycleView?.layoutManager = LinearLayoutManager(ctx)

        //outbound recycle view
        outboundViewModel.allOutbound.observe(this, { outboundList ->
            outboundList?.let {

                val data = ArrayList<Outbound_ViewModel>()  //data to make recycle view
                val idData = ArrayList<String>()            //data to pass to next activity

                for (Outbound in outboundList) {

                    val items = Outbound.toString()
                    val itemsClean1 = items.replace("Outbound(Outbound_ID=","")     //result filter
                    val itemsClean3 = itemsClean1.replace("Date=","")
                    val itemsClean4 = itemsClean3.replace("Status=","")
                    val itemsClean5 = itemsClean4.replace("Address=","")
                    val itemsClean6 = itemsClean5.replace(")","")
                    val itemsClean7 = itemsClean6.replace(" ","")
                    val itemArray: List<String> = itemsClean7.split(",")

                    val outboundIDList : String = itemArray[0]
                    val outboundStatusList : String = itemArray[2]
                    outboundStatusList.replace(" ","")

                    idData.add(outboundIDList)
                    data.add(Outbound_ViewModel(outboundIDList,outboundStatusList))

                    //Pass array to adapter
                    val adapter = Outbound_List_Adapter(data)

                    //setting Adapter with recycleView
                    recycleView?.adapter = adapter

                    //click on button in adapter list
                    adapter.setOnOutboundClickListener(object : Outbound_List_Adapter.onOutboundClickListener{
                        override fun onOutboundClick(position: Int) {
                            val bIntent = Intent(ctx,Outbound_Item_List_Activity::class.java)
                            bIntent.putExtra("outboundID",idData[position])
                            startActivity(bIntent)
                        }
                    })
                }
            }
        })

        //pop up status info
        imageButtonDisplayStatus?.setOnClickListener {
            // Initialize a new layout inflater instance
            val inflater: LayoutInflater =
                ctx?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // Inflate a custom view using layout inflater
            val view = inflater.inflate(R.layout.outboundpopup, null)
            // Initialize a new instance of popup window
            val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )

            // Set an elevation for the popup window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.elevation = 10.0F
            }

            // If API level 23 or higher then execute the code
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.BOTTOM
                popupWindow.exitTransition = slideOut

                popupWindow.isOutsideTouchable = true
                popupWindow.isFocusable = true
            }

            val outboundStatusPopup = self?.findViewById<ConstraintLayout>(R.id.outboundStatusPopup)
            // show the popup window on app
            TransitionManager.beginDelayedTransition(outboundStatusPopup)
            popupWindow.showAtLocation(
                outboundStatusPopup, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )
        }

        return self
    }
}