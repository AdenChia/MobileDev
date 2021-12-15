package com.example.hotayisstore.Outbound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotayisstore.MyApplication
import com.example.hotayisstore.databinding.ActivityOutboundItemListBinding
import com.example.hotayisstore.Data.ViewModel.Outbound_ItemViewModel

class Outbound_Item_List_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityOutboundItemListBinding

    private val outbound_ItemViewModel: Outbound_ItemViewModel by viewModels{
        Outbound_ItemViewModel.Outbound_ItemViewModelFactory((application as MyApplication).outbound_ItemRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutboundItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Step 1: Collect Item"
        val outboundID = intent.getStringExtra("outboundID").toString()  //data passed from previous activity

        val title_ID = binding.textViewDisplayOutboundID
        val packingBth = binding.buttonPacking

        packingBth.isEnabled = false    //false the proceed button before completed all collect
        title_ID.text = outboundID

        var countC: Int = 0         //get total of "status - C "
        var countView: Int = 0      //get total of recycle view list

        val recyclerView = binding.recycleViewOIID
        recyclerView.layoutManager = LinearLayoutManager(this)

        var billArray :String = ""   //collect data to generate report/bill/qrcode

        //select all in outbound Item db
        outbound_ItemViewModel.getSelectOutboundItem(outboundID).observe(this, { outbound_ItemList ->
            outbound_ItemList?.let {

                val data = ArrayList<Outbound_Item_ViewModel>() //Array to display recycle view
                val scanData = ArrayList<String>()              //Array to fetch data to scan
                val dataUpdate = ArrayList<String>()            //Array Data to update in Outbound Item db
                val statusArray = ArrayList<String>()           //Array to get total of Status

                for (Outbound_Items in outbound_ItemList) {
                    val items = Outbound_Items.toString()
                    val itemsClean1 = items.replace("Outbound_Item(No=","")    //result filter
                    val itemsClean2 = itemsClean1.replace("Outbound_ID=","")
                    val itemsClean3 = itemsClean2.replace("Item_ID=","")
                    val itemsClean4 = itemsClean3.replace("Quantity=","")
                    val itemsClean5 = itemsClean4.replace("Status=","")
                    val itemsClean6 = itemsClean5.replace(")","")
                    val itemsClean7 = itemsClean6.replace(" ","")
                    val itemArray: List<String> = itemsClean7.split(",")

                    val outboundNoList : String = itemArray[0]
                    val outboundIDList : String = itemArray[1]
                    val outboundItemIDList: String = itemArray[2]
                    val outboundItemQuantity : String = itemArray[3]
                    val outboundItemStatusList : String = itemArray[4]

                    if(outboundItemStatusList =="C"){    //collect total of C - collected
                        statusArray.add(outboundItemStatusList)
                        billArray += outboundItemIDList+"X"+ outboundItemQuantity +"\n\n"   //Collect data to generate report/bill/qrcode in next activity
                    }

                    scanData.add("$outboundItemIDList,$outboundItemQuantity")

                    data.add(Outbound_Item_ViewModel(outboundItemIDList,outboundItemQuantity,outboundItemStatusList))

                    dataUpdate.add("$outboundNoList,$outboundIDList,$outboundItemIDList,$outboundItemQuantity")

                    val adapter = Outbound_Item_List_Adapter(data)
                    recyclerView.adapter = adapter

                    //click on button in adapter list
                    adapter.setOnOutboundItemClickListener(object : Outbound_Item_List_Adapter.onOutboundItemClickListener{
                        override fun onOutboundItemClick(position: Int) {
                            val bIntent = Intent(this@Outbound_Item_List_Activity,Outbound_Item_Scan_Activity::class.java)
                            bIntent.putExtra("outboundIDItem",scanData[position]) //Item ID & quantity
                            bIntent.putExtra("dataUpdate",dataUpdate[position])   //Item data to update in Outbound Item db
                            startActivity(bIntent)
                        }
                    })
                }
                countC = statusArray.size
                countView = data.size

                if(countView == countC){
                    packingBth.isEnabled = true  //enable proceed button if all item collected
                }
            }
        })

        //proceed to upload activity
        packingBth.setOnClickListener {
            val bIntent = Intent(this,Outbound_Upload_Activity::class.java)
            bIntent.putExtra("outboundID",outboundID)
            bIntent.putExtra("billString",billArray)
            startActivity(bIntent)
        }
    }

    //back function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}