package com.example.hotayisstore.Data.Repository

import androidx.annotation.WorkerThread
import com.example.hotayisstore.Data.Dao.ItemDao
import com.example.hotayisstore.Data.Entity.Item
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val ItemDao: ItemDao) {

    val alItems:Flow<MutableList<Item>> = ItemDao.getItems()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(Item: Item){
        ItemDao.insert(Item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(Item: Item){
        ItemDao.delete(Item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(Item: Item){
        ItemDao.update(Item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getScanItem(scan_key:String): Flow<MutableList<Item>>{
        return ItemDao.getScanItem(scan_key)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun searchItems(searchQuery: String): Flow<List<Item>> {
        return ItemDao.searchItems(searchQuery)
    }

}