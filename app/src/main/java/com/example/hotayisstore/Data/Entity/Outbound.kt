package com.example.hotayisstore.Data.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Outbound")
data class Outbound(
    @PrimaryKey @ColumnInfo(name = "Outbound_ID") val Outbound_ID: String,
    @ColumnInfo(name = "Date") val Date: String,
    @ColumnInfo(name = "Status") val Status: String,
    @ColumnInfo(name = "Address") val Address: String

)