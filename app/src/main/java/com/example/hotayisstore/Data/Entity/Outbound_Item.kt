package com.example.hotayisstore.Data.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Outbound_Item")
data class Outbound_Item(
    @PrimaryKey @ColumnInfo(name = "No") val No: String,    //No of Outbound Item
    @ColumnInfo(name = "Outbound_ID") val Outbound_ID: String,
    @ColumnInfo(name = "Item_ID") val Item_ID: String,
    @ColumnInfo(name = "Quantity") val Quantity: Int,
    @ColumnInfo(name = "Status") val Status: String

)