package com.example.hotayisstore.Scan_or_Search


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.hotayisstore.Home.AllItemActivity
import com.example.hotayisstore.MyApplication
import com.example.hotayisstore.R
import com.example.hotayisstore.databinding.ActivityItemInfoBinding
import com.example.hotayisstore.Data.ViewModel.ItemViewModel

class ItemInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemInfoBinding

    private val itemViewModel: ItemViewModel by viewModels{
        ItemViewModel.ItemViewModelFactory((application as MyApplication).itemRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Item Information"

        var display_item_info:String = ""
        val item_id: String = intent.getStringExtra("itemCode").toString()
        val tempIdItem:String = intent.getStringExtra("idItem").toString()

        //if value received is not null
        if (tempIdItem != "null"){
            display_item_info = tempIdItem
        }else if (item_id != "null"){
            display_item_info = item_id
        }

        //get item with item ID
        itemViewModel.getScanItem(display_item_info).observe(this,{ itemList ->
            itemList?.let {
                val items = itemList.toString()
                val itemsClean1 = items.replace("[Item(Item_ID=","")    //result Filter
                val itemsClean2 = itemsClean1.replace("Item_Name=","")
                val itemsClean3 = itemsClean2.replace("Quantity=","")
                val itemsClean4 = itemsClean3.replace("Price=","")
                val itemsClean5 = itemsClean4.replace("Category_ID=","")
                val itemsClean6 = itemsClean5.replace("Rack_ID=","")
                val itemsClean7 = itemsClean6.replace(")]","")
                val itemsClean8 = itemsClean7.replace(" ","")
                val itemArray: List<String> = itemsClean8.split(",")

                //set text view
                binding.textViewShowItemID.text = itemArray[0]
                binding.textViewShowItemName.text = itemArray[1]
                binding.textViewItemShowPrice.text = itemArray[3]
                binding.textViewShowRackId.text = itemArray[5]
                binding.textViewShowItemQtty.text = itemArray[2]

                when (itemArray[4]) {
                    "C1" -> {
                        binding.textViewShowCategory.text = "C1 Small Accessories"
                    }
                    "C2" -> {
                        binding.textViewShowCategory.text = "C2 Produce Accessories"
                    }
                    "C3" -> {
                        binding.textViewShowCategory.text = "C3 Electronic Accessories"
                    }
                    "C4" -> {
                        binding.textViewShowCategory.text = "C4 Cable Accessories"
                    }
                    "C5" -> {
                        binding.textViewShowCategory.text = "C5 Machine"
                    }
                    else -> {
                        binding.textViewShowCategory.text = itemArray[4]
                    }
                }

                //change rack button to green following by rack ID
                when(binding.textViewShowRackId.text){
                    "R-001" ->{binding.imageViewRack1.setImageResource(R.drawable.ic_rack_on)}
                    "R-002" ->{binding.imageViewRack2.setImageResource(R.drawable.ic_rack_on)}
                    "R-003" ->{binding.imageViewRack3.setImageResource(R.drawable.ic_rack_on)}
                    "R-004" ->{binding.imageViewRack4.setImageResource(R.drawable.ic_rack_on)}
                    "R-005" ->{binding.imageViewRack5.setImageResource(R.drawable.ic_rack_on)}
                    "R-006" ->{binding.imageViewRack6.setImageResource(R.drawable.ic_rack_on)}
                    "R-007" ->{binding.imageViewRack7.setImageResource(R.drawable.ic_rack_on)}
                    "R-008" ->{binding.imageViewRack8.setImageResource(R.drawable.ic_rack_on)}
                    "R-009" ->{binding.imageViewRack9.setImageResource(R.drawable.ic_rack_on)}
                    "R-010" ->{binding.imageViewRack10.setImageResource(R.drawable.ic_rack_on)}
                    "R-011" ->{binding.imageViewRack11.setImageResource(R.drawable.ic_rack_on)}
                    "R-012" ->{binding.imageViewRack12.setImageResource(R.drawable.ic_rack_on)}
                    "R-013" ->{binding.imageViewRack13.setImageResource(R.drawable.ic_rack_on)}
                    "R-014" ->{binding.imageViewRack14.setImageResource(R.drawable.ic_rack_on)}
                    "R-015" ->{binding.imageViewRack15.setImageResource(R.drawable.ic_rack_on)}
                    "R-016" ->{binding.imageViewRack16.setImageResource(R.drawable.ic_rack_on)}
                    "R-017" ->{binding.imageViewRack17.setImageResource(R.drawable.ic_rack_on)}
                    "R-018" ->{binding.imageViewRack18.setImageResource(R.drawable.ic_rack_on)}
                    "R-019" ->{binding.imageViewRack19.setImageResource(R.drawable.ic_rack_on)}
                    "R-020" ->{binding.imageViewRack20.setImageResource(R.drawable.ic_rack_on)}
                    "R-021" ->{binding.imageViewRack21.setImageResource(R.drawable.ic_rack_on)}
                    "R-022" ->{binding.imageViewRack22.setImageResource(R.drawable.ic_rack_on)}
                    "R-023" ->{binding.imageViewRack23.setImageResource(R.drawable.ic_rack_on)}
                    "R-024" ->{binding.imageViewRack24.setImageResource(R.drawable.ic_rack_on)}
                    "R-025" ->{binding.imageViewRack25.setImageResource(R.drawable.ic_rack_on)}
                }
            }
        })
        //Click on rack and pass rack id
        binding.rack1Click.setOnClickListener { pass_rack_id() }
        binding.rack2Click.setOnClickListener { pass_rack_id() }
        binding.rack3Click.setOnClickListener { pass_rack_id() }
        binding.rack4Click.setOnClickListener { pass_rack_id() }
        binding.rack5Click.setOnClickListener { pass_rack_id() }
        binding.rack6Click.setOnClickListener { pass_rack_id() }
        binding.rack7Click.setOnClickListener { pass_rack_id() }
        binding.rack8Click.setOnClickListener { pass_rack_id() }
        binding.rack9Click.setOnClickListener { pass_rack_id() }
        binding.rack10Click.setOnClickListener { pass_rack_id() }
        binding.rack11Click.setOnClickListener { pass_rack_id() }
        binding.rack12Click.setOnClickListener { pass_rack_id() }
        binding.rack13Click.setOnClickListener { pass_rack_id() }
        binding.rack14Click.setOnClickListener { pass_rack_id() }
        binding.rack15Click.setOnClickListener { pass_rack_id() }
        binding.rack16Click.setOnClickListener { pass_rack_id() }
        binding.rack17Click.setOnClickListener { pass_rack_id() }
        binding.rack18Click.setOnClickListener { pass_rack_id() }
        binding.rack19Click.setOnClickListener { pass_rack_id() }
        binding.rack20Click.setOnClickListener { pass_rack_id() }
        binding.rack21Click.setOnClickListener { pass_rack_id() }
        binding.rack22Click.setOnClickListener { pass_rack_id() }
        binding.rack23Click.setOnClickListener { pass_rack_id() }
        binding.rack24Click.setOnClickListener { pass_rack_id() }
        binding.rack25Click.setOnClickListener { pass_rack_id() }

    }

    //function to pass rack id to search function in all item list
    private fun pass_rack_id(){
        val idRack:String = "R-0" + binding.textViewShowRackId.text.takeLast(2)
        //Toast.makeText(this, idRack, Toast.LENGTH_LONG).show()
        val rintent = Intent(this, AllItemActivity::class.java)
        rintent.putExtra("idRack", idRack)
        startActivity(rintent)
    }

    //back function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}