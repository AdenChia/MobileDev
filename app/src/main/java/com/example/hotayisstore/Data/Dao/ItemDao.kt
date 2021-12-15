package com.example.hotayisstore.Data.Dao

import androidx.room.*
import com.example.hotayisstore.Data.Entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    //Select All
    @Query("SELECT * FROM Item ORDER BY Item_ID")
    fun getItems():Flow<MutableList<Item>>

    //Search function
    @Query("SELECT * FROM Item WHERE Item_ID LIKE :searchQuery OR Item_Name LIKE :searchQuery OR Rack_ID LIKE :searchQuery")
    fun searchItems(searchQuery: String): Flow<List<Item>>

    //Select scan item
    @Query("SELECT * FROM Item WHERE Item_ID = :scan_key")
    fun getScanItem(scan_key: String): Flow<MutableList<Item>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(Item: Item)

    @Delete
    suspend fun delete(Item: Item)

    @Update
    suspend fun update(Item: Item)



}