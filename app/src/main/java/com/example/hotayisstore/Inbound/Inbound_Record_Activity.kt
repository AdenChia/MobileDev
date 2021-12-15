package com.example.hotayisstore.Inbound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.hotayisstore.Home.HomeFragment
import com.example.hotayisstore.MyApplication
import com.example.hotayisstore.databinding.ActivityInboundRecordBinding
import com.example.hotayisstore.Data.Entity.Item
import com.example.hotayisstore.Data.ViewModel.ItemViewModel

class Inbound_Record_Activity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private lateinit var binding: ActivityInboundRecordBinding

    private val itemViewModel: ItemViewModel by viewModels{
        ItemViewModel.ItemViewModelFactory((application as MyApplication).itemRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInboundRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Step 2: Record Inbound Quantity"

        val itemCode = intent.getStringExtra("itemCode").toString()  //receive item ID from previous activity
        val buttonItemRecord : Button = binding.buttonItemRecord //button to update Item info

        //TEXT VIEW
        val textViewItemID: TextView = binding.textViewItemID
        val textViewItemName: TextView = binding.textViewItemName
        val textViewPrice: TextView = binding.textViewPrice
        val textViewCategory: TextView = binding.textViewCategory
        val textViewRack: TextView = binding.textViewRack
        val textViewQuantity: TextView = binding.textViewQuantity

        //variable to store Item searched
        var itemID :String = ""
        var itemName :String = ""
        var price :String = ""
        var category :String = ""
        var rack :String = ""
        var quantity :Int = 0
        var floatPrice : Float = 0.0F

        //get Scan Item function from Item View Model
        itemViewModel.getScanItem(itemCode).observe(this,{ itemList -> //Present Information
            itemList?.let {
                val items = itemList.toString()
                val itemsClean1 = items.replace("[Item(Item_ID=","")     //result filter
                val itemsClean2 = itemsClean1.replace("Item_Name=","")
                val itemsClean3 = itemsClean2.replace("Quantity=","")
                val itemsClean4 = itemsClean3.replace("Price=","")
                val itemsClean5 = itemsClean4.replace("Category_ID=","")
                val itemsClean6 = itemsClean5.replace("Rack_ID=","")
                val itemsClean7 = itemsClean6.replace(")]","")
                val itemsClean8 = itemsClean7.replace(" ","")
                val itemArrayNice: List<String> = itemsClean7.split(",")
                val itemArray: List<String> = itemsClean8.split(",")

                //storing result to variable
                itemID = itemArray[0]
                itemName = itemArrayNice[1]
                quantity = itemArray[2].toInt()
                price = itemArray[3]
                category = itemArray[4]
                rack = itemArray[5]
                itemName = itemName.replace("\\s".toRegex(), "")

                //set textview value
                textViewItemID.text = itemID
                textViewItemName.text = itemName
                textViewPrice.text = price
                textViewRack.text = rack
                textViewQuantity.text = itemArray[2]

                when (category) {
                    "C1" -> {
                        textViewCategory.text = "C1 Small Accessories"
                    }
                    "C2" -> {
                        textViewCategory.text = "C2 Produce Accessories"
                    }
                    "C3" -> {
                        textViewCategory.text = "C3 Electronic Accessories"
                    }
                    "C4" -> {
                        textViewCategory.text = "C4 Cable Accessories"
                    }
                    "C5" -> {
                        textViewCategory.text = "C5 Machine"
                    }
                    else -> {
                        textViewCategory.text = category
                    }
                }
                floatPrice = price.toFloat()
            }
        })

        //click to update Item quantity in item db
        buttonItemRecord.setOnClickListener {  //record process

            val editTextQuantityReceive: EditText = binding.editTextQuantityReceived
            when{
                editTextQuantityReceive.text.toString().isEmpty()->{ //input Quantity validation
                    editTextQuantityReceive.error = "Required field"
                }
                editTextQuantityReceive.text.toString() == "0"->{  //input Quantity validation
                    Toast.makeText(this, "Please Enter Valid Number", Toast.LENGTH_SHORT).show()
                }
                else->{
                    val newQuantity: Int= editTextQuantityReceive.text.toString().toInt()
                    val total: Int = quantity + newQuantity   //quantity already have - quantity user input
                    var msg: String = "Record Saved! Total $total items in Store"

                    //update new quantity into item db
                    val item = Item(itemID,itemName,total,floatPrice,category,rack)
                    itemViewModel.update(item)
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

                    //proceed to Inbound storing
                    val bIntent = Intent(this,Inbound_Storing_Activity::class.java)
                    bIntent.putExtra("rackCode",rack)
                    startActivity(bIntent)
                }
            }

        }
    }

    //back function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}