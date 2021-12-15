package com.example.hotayisstore.Data.ViewModel

import androidx.lifecycle.*
import com.example.hotayisstore.Data.Entity.Outbound
import com.example.hotayisstore.Data.Repository.OutboundRepository
import kotlinx.coroutines.launch

class OutboundViewModel(private val outboundRepository: OutboundRepository) : ViewModel() {

    val allOutbound : LiveData<MutableList<Outbound>> = outboundRepository.alOutbound.asLiveData()

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insert(outbound: Outbound) = viewModelScope.launch {
        outboundRepository.insert(outbound)
    }

    fun delete(outbound: Outbound) = viewModelScope.launch {
        outboundRepository.delete(outbound)
    }

    fun update(outbound: Outbound) = viewModelScope.launch {
        outboundRepository.update(outbound)
    }

    //Function to instantiate and return the ViewModel object that survives configuration changes.
    class OutboundViewModelFactory(private val outboundRepository: OutboundRepository)
        : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OutboundViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return OutboundViewModel(outboundRepository) as T
            }
            throw IllegalArgumentException("Unknown VieModel Class")
        }

    }
}