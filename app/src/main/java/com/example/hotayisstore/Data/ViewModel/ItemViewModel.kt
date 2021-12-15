package com.example.hotayisstore.Data.ViewModel

import androidx.lifecycle.*
import com.example.hotayisstore.Data.Entity.Item
import com.example.hotayisstore.Data.Repository.ItemRepository
import kotlinx.coroutines.launch

class ItemViewModel(private val ItemRepository: ItemRepository) : ViewModel() {

    val allItems : LiveData<MutableList<Item>> = ItemRepository.alItems.asLiveData()

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insert(Item: Item) = viewModelScope.launch {
        ItemRepository.insert(Item)
    }

    fun delete(Item: Item) = viewModelScope.launch {
        ItemRepository.delete(Item)
    }

    fun update(Item: Item) = viewModelScope.launch {
        ItemRepository.update(Item)
    }

    fun getScanItem(scan_key: String): LiveData<MutableList<Item>>{
        return ItemRepository.getScanItem(scan_key).asLiveData()
    }

    fun searchItems(searchQuery: String): LiveData<List<Item>>{
        return ItemRepository.searchItems(searchQuery).asLiveData()
    }

    //Function to instantiate and return the ViewModel object that survives configuration changes.
    class ItemViewModelFactory(private val ItemRepository: ItemRepository) :ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ItemViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ItemViewModel(ItemRepository) as T
            }
            throw IllegalArgumentException("Unknown VieModel Class")
        }
    }
}