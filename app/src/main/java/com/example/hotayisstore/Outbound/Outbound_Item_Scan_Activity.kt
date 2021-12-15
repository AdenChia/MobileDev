package com.example.hotayisstore.Outbound

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.viewModels
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.hotayisstore.MyApplication
import com.example.hotayisstore.R
import com.example.hotayisstore.databinding.ActivityOutboundItemScanBinding
import com.example.hotayisstore.Data.Entity.Item
import com.example.hotayisstore.Data.Entity.Outbound_Item
import com.example.hotayisstore.Data.ViewModel.ItemViewModel
import com.example.hotayisstore.Data.ViewModel.Outbound_ItemViewModel

class Outbound_Item_Scan_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityOutboundItemScanBinding
    private lateinit var codeScanner: CodeScanner

    private val itemViewModel: ItemViewModel by viewModels{
        ItemViewModel.ItemViewModelFactory((application as MyApplication).itemRepository)
    }

    private val outbound_ItemViewModel: Outbound_ItemViewModel by viewModels{
        Outbound_ItemViewModel.Outbound_ItemViewModelFactory((application as MyApplication).outbound_ItemRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutboundItemScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Scan and Collect Item"
        val outboundIDItem = intent.getStringExtra("outboundIDItem").toString() //received from outbound item list
        val dataUpdate = intent.getStringExtra("dataUpdate").toString()

        val itemArray: List<String> = outboundIDItem.split(",")
        val updateArray: List<String> = dataUpdate.split(",")

        val updateOutboundItemNo :String = updateArray[0] //store outbound item info to update
        val updateOutboundID :String = updateArray[1]
        val updateOutboundItemID :String = updateArray[2]
        val updateOutboundItemQuantity :Int = updateArray[3].toInt()


        //variable to store item info to update
        val itemID :String = itemArray[0]
        val quantity :String = itemArray[1]

        var scanResult :String = ""
        var scanTime :Int = 0 // how many time we scan
        var intQuantity :Int = quantity.toInt()

        binding.buttonCollect.isEnabled = false //disable the collect button if not complete scan
        binding.textViewOutboundItemIDDisplay.text = "Scan "+ itemID
        binding.textViewNeed.text = quantity //how many time we need

        //camera area setup
        codeScanner = CodeScanner(this, binding.scannerOutboundItem)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                scanResult = it.text.toString()
                if(scanResult == itemID){
                    if(scanTime != intQuantity){
                        scanTime++
                        binding.textViewNotYet.text = scanTime.toString() // how many time we scan

                        if(scanTime == intQuantity){
                            binding.buttonCollect.isEnabled = true //enable button if times we scan = quantity we need
                        }
                    }else{
                        Toast.makeText(this, "Item Enough to Collect!", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "Please Scan Correct Item Code!", Toast.LENGTH_LONG).show()
                }
            }
        }

        //press to rescan
        binding.scannerOutboundItem.setOnClickListener {
            codeScanner.startPreview()
        }

        //variable to store item info
        var getItemID :String = ""
        var getItemName :String = ""
        var getQuantity :Int = 0
        var getprice : Float = 0.0F
        var getcategory :String = ""
        var getrack :String = ""

        //search item info for update
            itemViewModel.getScanItem(itemID).observe(this,{ itemList ->
                itemList?.let {
                    val items = itemList.toString()
                    val itemsClean1 = items.replace("[Item(Item_ID=","")    //result filter
                    val itemsClean2 = itemsClean1.replace("Item_Name=","")
                    val itemsClean3 = itemsClean2.replace("Quantity=","")
                    val itemsClean4 = itemsClean3.replace("Price=","")
                    val itemsClean5 = itemsClean4.replace("Category_ID=","")
                    val itemsClean6 = itemsClean5.replace("Rack_ID=","")
                    val itemsClean7 = itemsClean6.replace(")]","")
                    val itemsClean8 = itemsClean7.replace(" ","")
                    val itemArrayNice: List<String> = itemsClean7.split(",")
                    val itemArray: List<String> = itemsClean8.split(",")

                    getItemID = itemArray[0]
                    getItemName = itemArrayNice[1]
                    getQuantity = itemArray[2].toInt()
                    getprice = itemArray[3].toFloat()
                    getcategory = itemArray[4]
                    getrack = itemArray[5]

                    getItemName = getItemName.replace("\\s".toRegex(), "")

                    binding.textViewCollectRack.text = "Collect from rack $getrack"
                }
            })

        //click to update item db and collect
        binding.buttonCollect.setOnClickListener {
            var newQuantity = getQuantity - scanTime
            if(newQuantity < 5){
                //stock nearly empty warning
                Toast.makeText(this, "Stock is nearly less, PLease inform stock department", Toast.LENGTH_LONG).show()
            }else if(newQuantity < 0){
                //stock empty warning
                Toast.makeText(this, "Stock Empty", Toast.LENGTH_LONG).show()
            }else{
                //update item db
                itemViewModel.update(Item(getItemID,getItemName,newQuantity,getprice,getcategory,getrack))

                //update outbound item db
                outbound_ItemViewModel.update(Outbound_Item(updateOutboundItemNo,updateOutboundID,updateOutboundItemID,updateOutboundItemQuantity,"C"))

                Toast.makeText(this, "Item $updateOutboundID Collected", Toast.LENGTH_LONG).show()
                binding.buttonCollect.isEnabled = false //disable the collect button
                finish() //finish this activity
            }
        }

        //rack Location popup
        binding.imageButtonOutboundRackPopup.setOnClickListener {
            // Initialize a new layout inflater instance
            val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // Inflate a custom view using layout inflater
            val view = inflater.inflate(R.layout.rackpopup, null)
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

            val outboundRackPopup = binding.outboundRackPopup
            // show the popup window on app
            TransitionManager.beginDelayedTransition(outboundRackPopup)
            popupWindow.showAtLocation(
                outboundRackPopup, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                50 // Y offset
            )
        }
    }

    //resume scan
    override fun onResume() {
        super.onResume()

        codeScanner.startPreview()
    }

    //pause scan
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

}