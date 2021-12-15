package com.example.hotayisstore.Data.Dao

import androidx.room.*
import com.example.hotayisstore.Data.Entity.Outbound
import kotlinx.coroutines.flow.Flow

@Dao
interface OutboundDao {

    //select all
    @Query("SELECT * FROM Outbound ORDER BY Outbound_ID")
    fun getOutbound(): Flow<MutableList<Outbound>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(outbound: Outbound)

    @Delete
    suspend fun delete(outbound: Outbound)

    @Update
    suspend fun update(outbound: Outbound)

}