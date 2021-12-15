package com.example.hotayisstore.Data.ViewModel

import androidx.lifecycle.*
import com.example.hotayisstore.Data.Entity.Outbound_Item
import com.example.hotayisstore.Data.Repository.Outbound_ItemRepository
import kotlinx.coroutines.launch

class Outbound_ItemViewModel(private val Outbound_ItemRepository: Outbound_ItemRepository) : ViewModel() {

    val allOutbound_Item: LiveData<MutableList<Outbound_Item>> = Outbound_ItemRepository.alOutboundItem.asLiveData()

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insert(outbound_Item: Outbound_Item) = viewModelScope.launch {
        Outbound_ItemRepository.insert(outbound_Item)
    }

    fun delete(outbound_Item: Outbound_Item) = viewModelScope.launch {
        Outbound_ItemRepository.delete(outbound_Item)
    }

    fun update(outbound_Item: Outbound_Item) = viewModelScope.launch {
        Outbound_ItemRepository.update(outbound_Item)
    }

    fun getSelectOutboundItem(outboundItem_key : String): LiveData<MutableList<Outbound_Item>>{
        return Outbound_ItemRepository.getSelectOutboundItem(outboundItem_key).asLiveData()
    }

    //Function to instantiate and return the ViewModel object that survives configuration changes.
    class Outbound_ItemViewModelFactory(private val Outbound_ItemRepository: Outbound_ItemRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(Outbound_ItemViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return Outbound_ItemViewModel(Outbound_ItemRepository) as T
            }
            throw IllegalArgumentException("Unknown VieModel Class")
        }
    }
}