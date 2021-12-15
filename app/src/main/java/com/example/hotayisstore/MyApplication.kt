package com.example.hotayisstore

import android.app.Application
import com.example.hotayisstore.Data.Repository.ItemRepository
import com.example.hotayisstore.Data.Repository.OutboundRepository
import com.example.hotayisstore.Data.Repository.Outbound_ItemRepository
import com.example.hotayisstore.Data.RoomDB.ItemRoomDB
import com.example.hotayisstore.Data.RoomDB.OutboundRoomDB
import com.example.hotayisstore.Data.RoomDB.Outbound_ItemRoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication : Application() {

    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts

    //outbound room db
    private val outboundDatabase by lazy { OutboundRoomDB.getDatabase(this, applicationScope) }
    val outboundRepository by lazy { OutboundRepository(outboundDatabase.outboundDao()) }

    //item room db
    private val itemDatabase by lazy { ItemRoomDB.getDatabase(this, applicationScope) }
    val itemRepository by lazy { ItemRepository(itemDatabase.ItemDao()) }

    //outbound item room db
    private val Outbound_ItemDatabase by lazy { Outbound_ItemRoomDB.getDatabase(this, applicationScope) }
    val outbound_ItemRepository by lazy { Outbound_ItemRepository(Outbound_ItemDatabase.outbound_ItemDao()) }
}