package com.example.hotayisstore.Data.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Item")
data class Item(
    @PrimaryKey @ColumnInfo(name = "Item_ID") val Item_ID: String,
    @ColumnInfo(name = "Item_Name") val Item_Name: String,
    @ColumnInfo(name = "Quantity") val Quantity: Int,
    @ColumnInfo(name = "Price") val Price: Float,
    @ColumnInfo(name = "Category_ID") val Category_ID: String,
    @ColumnInfo(name = "Rack_ID") val Rack_ID: String
)
