package com.example.hotayisstore.Data.Repository

import androidx.annotation.WorkerThread
import com.example.hotayisstore.Data.Dao.Outbound_ItemDao
import com.example.hotayisstore.Data.Entity.Outbound_Item
import kotlinx.coroutines.flow.Flow

class Outbound_ItemRepository(private val outbound_ItemDao: Outbound_ItemDao) {

    val alOutboundItem: Flow<MutableList<Outbound_Item>> = outbound_ItemDao.getOutboundItem()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(outbound_Item: Outbound_Item){
        outbound_ItemDao.insert(outbound_Item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(outbound_Item: Outbound_Item){
        outbound_ItemDao.delete(outbound_Item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(outbound_Item: Outbound_Item){
        outbound_ItemDao.update(outbound_Item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getSelectOutboundItem(outboundItem_key : String): Flow<MutableList<Outbound_Item>>{
        return outbound_ItemDao.getSelectOutboundItem(outboundItem_key)
    }

}