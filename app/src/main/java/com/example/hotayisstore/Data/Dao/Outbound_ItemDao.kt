package com.example.hotayisstore.Data.Dao

import androidx.room.*
import com.example.hotayisstore.Data.Entity.Outbound_Item
import kotlinx.coroutines.flow.Flow

@Dao
interface Outbound_ItemDao {

    //select all
    @Query("SELECT * FROM Outbound_Item")
    fun getOutboundItem(): Flow<MutableList<Outbound_Item>>

    //select only outbound item
    @Query("SELECT * FROM Outbound_Item WHERE Outbound_ID = :outboundItem_key")
    fun getSelectOutboundItem(outboundItem_key : String): Flow<MutableList<Outbound_Item>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(outbound_Item: Outbound_Item)

    @Delete
    suspend fun delete(outbound_Item: Outbound_Item)

    @Update
    suspend fun update(outbound_Item: Outbound_Item)


}