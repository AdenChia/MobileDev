package com.example.hotayisstore.Home

import android.content.Intent
import android.net.Uri
import com.example.hotayisstore.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.text.isDigitsOnly
import com.example.hotayisstore.MainActivity
import com.example.hotayisstore.MyApplication
import com.example.hotayisstore.databinding.ActivityReportBinding
import com.example.hotayisstore.Data.Entity.Item
import com.example.hotayisstore.Data.ViewModel.ItemViewModel
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : AppCompatActivity() {

    private val itemViewModel: ItemViewModel by viewModels{
        ItemViewModel.ItemViewModelFactory((application as MyApplication).itemRepository)
    }

    private lateinit var binding: ActivityReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get idReport(needed) : allow system to fill in itemID directly
        var aintent = intent
        val id = aintent.getStringExtra("idReport")
        binding.editTextItemID.setText(id)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Report Item Problems" //Set activity title

        //function to only allow select one in check box
        allow1CheckBox()

        binding.buttonSubmitReport.setOnClickListener {
            when {
                binding.editTextItemID.text.toString().isEmpty() -> {   //Item ID input validation
                    binding.editTextItemID.error = getString(R.string.required)
                    return@setOnClickListener
                }
                binding.editTextItemQuantity.text.toString().isEmpty() -> { //Quantity input validation
                    binding.editTextItemQuantity.error = getString(R.string.required)
                    return@setOnClickListener
                }(!binding.checkBoxBroken.isChecked && !binding.checkBoxOthers.isChecked && !binding.checkBoxIncorrectQty.isChecked)->{  //checkbox validation
                        Toast.makeText(this, "Please choose 1 from checkbox", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                }(binding.editTextItemQuantity.text.toString().toInt()<0 || !binding.editTextItemQuantity.text.toString().isDigitsOnly())->{ //Quantity input validation
                        binding.editTextItemQuantity.error = getString(R.string.invalidQty)
                        return@setOnClickListener
                }
                (binding.checkBoxOthers.isChecked && binding.editTextOthers.text.toString().isEmpty())->{  // Other input validation
                    binding.editTextOthers.error = getString(R.string.required)
                    return@setOnClickListener
                } else->{

                    //variable to store info selected
                    var itemID :String = ""
                    var itemName :String = ""
                    var price : Float = 0.0F
                    var category :String = ""
                    var rack :String = ""
                    var quantity :Int = 0

                    //get item info by User input Item ID
                    itemViewModel.getScanItem(binding.editTextItemID.text.toString()).observe(this,{ itemList ->
                    itemList?.let {
                        val items = itemList.toString()
                        if(items != "[]"){
                            val items = itemList.toString()
                            val itemsClean1 = items.replace("[Item(Item_ID=","")    //returned data filter
                            val itemsClean2 = itemsClean1.replace("Item_Name=","")
                            val itemsClean3 = itemsClean2.replace("Quantity=","")
                            val itemsClean4 = itemsClean3.replace("Price=","")
                            val itemsClean5 = itemsClean4.replace("Category_ID=","")
                            val itemsClean6 = itemsClean5.replace("Rack_ID=","")
                            val itemsClean7 = itemsClean6.replace(")]","")
                            val itemsClean8 = itemsClean7.replace(" ","")
                            val itemArrayNice: List<String> = itemsClean7.split(",")
                            val itemArray: List<String> = itemsClean8.split(",")

                            //store data in variable
                            itemID = itemArray[0]
                            itemName = itemArrayNice[1]
                            price = itemArray[3].toFloat()
                            category = itemArray[4]
                            rack = itemArray[5]
                            binding.textViewID.text= itemID
                            quantity = itemArray[2].toInt()

                            //variable for sending email
                            var subject=""
                            var message=""
                            var quantityUp = 0

                            val email: Array<String> = arrayOf("hotayisItemManage@hotayi.com.my", "HotayiReturnDepartment@hotayi.com.my")
                            if(binding.checkBoxIncorrectQty.isChecked){
                                //Process Wrong Quantity selection

                                //prepare Email info
                                quantityUp = binding.editTextItemQuantity.text.toString().toInt()
                                subject = "Incorrect Quantity"
                                message = "There is incorrect quantity of items ID:$itemID in Rack ID: $rack The Quantity Now: $quantityUp Quantity Before: $quantity"
                                binding.editTextItemID.text.clear()
                                binding.editTextItemQuantity.text.clear()
                                binding.checkBoxBroken.isChecked = false
                                binding.checkBoxIncorrectQty.isChecked = false
                                binding.checkBoxOthers.isChecked = false
                                itemName = itemName.replace("\\s".toRegex(), "")

                                //update item db
                                itemViewModel.update(Item(itemID,itemName,quantityUp,price,category,rack))
                                Toast.makeText(this, "Items: $itemID Updated to $quantityUp", Toast.LENGTH_SHORT).show()

                                //send Email Activity
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:")
                                    putExtra(Intent.EXTRA_EMAIL, email)
                                    putExtra(Intent.EXTRA_SUBJECT, subject)
                                    putExtra(Intent.EXTRA_TEXT, message)

                                }

                                //see whatever have apps can handle or not
                                if(intent.resolveActivity(packageManager)!= null){
                                    val bIntent = Intent(this, MainActivity::class.java) //Pass complete number to Home Fragment
                                    val complete: String = "1"
                                    bIntent.putExtra("complete_No",complete)
                                    startActivity(bIntent)   //pass to Main Activity
                                    startActivity(intent)    //start send email
                                    finishAffinity()         //clear all history activity
                                }else{
                                    Toast.makeText(this,"Required App is not installed",Toast.LENGTH_SHORT).show()
                                }

                            }else{

                                //process other selection
                                var deduction = binding.editTextItemQuantity.text.toString().toInt()

                                if(quantity >= 0 && (quantity-deduction)>=0){
                                    quantityUp = quantity - binding.editTextItemQuantity.text.toString().toInt()

                                    if(binding.checkBoxOthers.isChecked){
                                        subject = binding.editTextOthers.text.toString()
                                        message = "There is deducted ${binding.editTextItemQuantity.text} from quantity of items ID:$itemID in Rack ID: $rack The Quantity Now: $quantityUp Quantity Before: $quantity"

                                    }else{
                                        subject = "Items Broken"
                                        message = "There is deducted ${binding.editTextItemQuantity.text} from quantity of items ID:$itemID in Rack ID: $rack The Quantity Now: $quantityUp Quantity Before: $quantity"

                                    }
                                    binding.editTextItemID.text.clear()
                                    binding.editTextItemQuantity.text.clear()
                                    binding.checkBoxBroken.isChecked = false
                                    binding.checkBoxIncorrectQty.isChecked = false
                                    binding.checkBoxOthers.isChecked = false
                                    itemName = itemName.replace("\\s".toRegex(), "")

                                    //update item db
                                    itemViewModel.update(Item(itemID,itemName,quantityUp,price,category,rack))
                                    Toast.makeText(this, "Items: $itemID Updated to:$quantityUp", Toast.LENGTH_SHORT).show()

                                    //prepare to send Email
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:")
                                        putExtra(Intent.EXTRA_EMAIL, email)
                                        putExtra(Intent.EXTRA_SUBJECT, subject)
                                        putExtra(Intent.EXTRA_TEXT, message)

                                    }
                                    //see wheter have apps can handle or not
                                    if(intent.resolveActivity(packageManager)!= null){
                                        val bIntent = Intent(this, MainActivity::class.java) //Pass complete number to Home Fragment
                                        val complete: String = "1"
                                        bIntent.putExtra("complete_No",complete)
                                        startActivity(bIntent)   //start send email
                                        startActivity(intent)    //pass to Home
                                        finishAffinity()         //clear all history activity
                                    }else{
                                        Toast.makeText(this,"Required App is not installed",Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    binding.editTextItemQuantity.error = getString(R.string.invalidQty)+ " Not Enough Quantity To Deduct"

                                }
                            }
                        }else{

                            Toast.makeText(this, "ID Not Found", Toast.LENGTH_SHORT).show()
                        }
                    }
                    })

                return@setOnClickListener
                }
            }
        }
    }

    //function to allow user only can choose 1 checkbox
    fun allow1CheckBox(){
        //Only Allow 1 checkbox checked
        binding.checkBoxOthers.setOnCheckedChangeListener {
                buttonView, isChecked -> binding.editTextOthers.isEnabled = isChecked
            binding.checkBoxBroken.isChecked = false
            binding.checkBoxIncorrectQty.isChecked = false

        }
        binding.checkBoxBroken.setOnCheckedChangeListener {
                buttonView, isChecked ->
            binding.checkBoxIncorrectQty.isChecked = false
            binding.checkBoxOthers.isChecked = false
        }
        binding.checkBoxIncorrectQty.setOnCheckedChangeListener {
                buttonView, isChecked ->
            binding.checkBoxBroken.isChecked = false
            binding.checkBoxOthers.isChecked = false
        }
    }

    //beck function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}