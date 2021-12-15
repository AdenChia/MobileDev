package com.example.hotayisstore.Data.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hotayisstore.Data.Dao.OutboundDao
import com.example.hotayisstore.Data.Entity.Outbound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Outbound::class], version = 1, exportSchema = false)
public abstract class OutboundRoomDB : RoomDatabase(){

    abstract fun outboundDao() : OutboundDao

    companion object {

        @Volatile
        private var INSTANCE : OutboundRoomDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): OutboundRoomDB {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OutboundRoomDB::class.java,
                    "outbound_database"
                ).addCallback(OutboundCallback(scope))
                    .build()

                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    //Function to setup data when app installed
    private class OutboundCallback(val scope: CoroutineScope): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { outboundRoomDB ->
                scope.launch {

                    //pre-populate data
                    outboundRoomDB.outboundDao().insert(Outbound("HTY-OUT-004","-","I","S041 & S042, 2nd Floor"))
                    outboundRoomDB.outboundDao().insert(Outbound("HTY-OUT-003","-","I","2760, Kampung Cina"))
                    outboundRoomDB.outboundDao().insert(Outbound("HTY-OUT-002","-","P","Kampus Utama"))
                    outboundRoomDB.outboundDao().insert(Outbound("HTY-OUT-001","2021-12-10-13:23:44","C","KLCC, Lot No. 241"))

                }
            }
        }
    }
}