package com.example.hotayisstore.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotayisstore.MyApplication
import com.example.hotayisstore.R
import com.example.hotayisstore.Scan_or_Search.ItemInfoActivity
import com.example.hotayisstore.databinding.ActivityAllItemBinding
import com.example.hotayisstore.Data.ViewModel.ItemViewModel
//import com.example.hotayisstore.Data.ViewModel.ItemsViewModel
import kotlinx.android.synthetic.main.activity_all_item.*



class AllItemActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityAllItemBinding
    private val myAdapter : ItemAdapter by lazy { ItemAdapter(this)}

    //itemViewModel from Myapplication
    private val itemViewModel: ItemViewModel by viewModels{
        ItemViewModel.ItemViewModelFactory((application as MyApplication).itemRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContentView(R.layout.activity_all_item)

        val actionBar = supportActionBar
        actionBar!!.title = "All Items"
        actionBar.setDisplayHomeAsUpEnabled(true)

        listViewItems.layoutManager = LinearLayoutManager(this)
        listViewItems.adapter = myAdapter //call adapter function

        val rintent = Intent(this, ItemInfoActivity::class.java)

        //click the adapter list and link to selected item info
        myAdapter.setOnItemClickListener(object : ItemAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val id :String = myAdapter.getCurrentItemID(position)
                rintent.putExtra("idItem", id)
                startActivity(rintent)
            }
        })

        //pass value into adapter
        itemViewModel.allItems.observe(this, {
            myAdapter.setData(it)
        })
    }

    //Search function
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val search = menu?.findItem(R.id.menu_search)
        val searchView = search?.actionView as? androidx.appcompat.widget.SearchView

        //Search Rack
        //pass rack ID received into search which is from item info
        var intent = intent
        val tempSearch = intent.getStringExtra("idRack")

        if(tempSearch!= null){
            searchView?.isSubmitButtonEnabled = true
            searchView?.setQuery("$tempSearch", true)
            searchItems(tempSearch)
            Toast.makeText(applicationContext, "Item in Rack $tempSearch", Toast.LENGTH_SHORT).show()
        }
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        return true
    }

    //Submit the search
    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!= null){
            searchItems(query)
        }
        return true
    }

    //change word in search
    override fun onQueryTextChange(query: String?): Boolean {
        if(query!= null){
            searchItems(query)
        }
        return true

    }

    //proceed search
    private fun searchItems(query: String){
        val searchQuery = "%$query%"

        itemViewModel.searchItems(searchQuery).observe(this, { list-> //search from Item DB
            list.let{
                myAdapter.setData(it)
            }
        })
    }

    //Back function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}