package com.example.hotayisstore.Data.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hotayisstore.Data.Dao.Outbound_ItemDao
import com.example.hotayisstore.Data.Entity.Outbound_Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Outbound_Item::class], version = 1, exportSchema = false)
public abstract class Outbound_ItemRoomDB : RoomDatabase(){

    abstract fun outbound_ItemDao() : Outbound_ItemDao

    companion object {

        @Volatile
        private var INSTANCE : Outbound_ItemRoomDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): Outbound_ItemRoomDB {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Outbound_ItemRoomDB::class.java,
                    "outbound_item_database"
                ).addCallback(Outbound_ItemCallback(scope))
                    .build()

                INSTANCE = instance

                // return instance
                instance
            }
        }
    }

    //Function to setup data when app installed
    private class Outbound_ItemCallback(val scope: CoroutineScope): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { outbound_ItemRoomDB ->
                scope.launch {

                    //pre-populate data
                    outbound_ItemRoomDB.outbound_ItemDao().insert(Outbound_Item("8","HTY-OUT-004","S-C1-000001",2,"I"))
                    outbound_ItemRoomDB.outbound_ItemDao().insert(Outbound_Item("7","HTY-OUT-004","S-C2-000002",2,"I"))
                    outbound_ItemRoomDB.outbound_ItemDao().insert(Outbound_Item("6","HTY-OUT-003","S-C3-000001",4,"I"))
                    outbound_ItemRoomDB.outbound_ItemDao().insert(Outbound_Item("5","HTY-OUT-003","S-C1-000003",3,"I"))
                    outbound_ItemRoomDB.outbound_ItemDao().insert(Outbound_Item("4","HTY-OUT-002","S-C5-000001",2,"C"))
                    outbound_ItemRoomDB.outbound_ItemDao().insert(Outbound_Item("3","HTY-OUT-002","S-C2-000008",4,"C"))
                    outbound_ItemRoomDB.outbound_ItemDao().insert(Outbound_Item("2","HTY-OUT-001","S-C5-000005",2,"C"))
                    outbound_ItemRoomDB.outbound_ItemDao().insert(Outbound_Item("1","HTY-OUT-001","S-C4-000003",2,"C"))
                }
            }
        }
    }
}