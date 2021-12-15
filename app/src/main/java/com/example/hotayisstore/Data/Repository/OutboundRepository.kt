package com.example.hotayisstore.Data.Repository

import androidx.annotation.WorkerThread
import com.example.hotayisstore.Data.Dao.OutboundDao
import com.example.hotayisstore.Data.Entity.Outbound
import kotlinx.coroutines.flow.Flow

class OutboundRepository(private val outboundDao: OutboundDao) {

    val alOutbound: Flow<MutableList<Outbound>> = outboundDao.getOutbound()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(Outbound: Outbound){
        outboundDao.insert(Outbound)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(Outbound: Outbound){
        outboundDao.delete(Outbound)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(Outbound: Outbound){
        outboundDao.update(Outbound)
    }



}