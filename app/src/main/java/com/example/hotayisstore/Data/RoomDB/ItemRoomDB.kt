package com.example.hotayisstore.Data.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hotayisstore.Data.Dao.ItemDao
import com.example.hotayisstore.Data.Entity.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Item::class], version = 1, exportSchema = false)
public abstract class ItemRoomDB : RoomDatabase(){

    abstract fun ItemDao() : ItemDao

    companion object {

        @Volatile
        private var INSTANCE : ItemRoomDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ItemRoomDB {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDB::class.java,
                    "item_database"
                ).addCallback(ItemCallback(scope))
                    .build()

                INSTANCE = instance

                // return instance
                instance
            }
        }
    }

    //Function to setup data when app installed
    private class ItemCallback(val scope: CoroutineScope):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { itemRoomDB ->
                scope.launch {

                    //pre-populate data
                    //C1 Item
                    itemRoomDB.ItemDao().insert(Item("S-C1-000001","B1 Sensor Bulb",10,12f,"C1","R-001"))
                    itemRoomDB.ItemDao().insert(Item("S-C1-000002","B7 Sensor Wire",8,14f,"C1","R-001"))
                    itemRoomDB.ItemDao().insert(Item("S-C1-000003","Engine Oil Fuel",17,15f,"C1","R-001"))
                    itemRoomDB.ItemDao().insert(Item("S-C1-000004","MAX-11 Handle Cover",10,10f,"C1","R-001"))
                    itemRoomDB.ItemDao().insert(Item("S-C1-000005","B77 Screen Frame",17,17f,"C1","R-002"))
                    itemRoomDB.ItemDao().insert(Item("S-C1-000006","B1 Notification Bulb",20,20f,"C1","R-002"))
                    itemRoomDB.ItemDao().insert(Item("S-C1-000007","ACER-NITRO Plug Switch",12,12f,"C1","R-003"))
                    itemRoomDB.ItemDao().insert(Item("S-C1-000008","A3GG7 AIR Filter",15,15f,"C1","R-004"))
                    itemRoomDB.ItemDao().insert(Item("S-C1-000009","Engine Cooler Oil(Tin)",17,17f,"C1","R-005"))
                    itemRoomDB.ItemDao().insert(Item("S-C1-000010","Gomu Gomu Magnet",18,18f,"C1","R-005"))

                    //C2 Item
                    itemRoomDB.ItemDao().insert(Item("S-C2-000001","Universal Headrest Hanger Holder",18,30f,"C2","R-006"))
                    itemRoomDB.ItemDao().insert(Item("S-C2-000002","Smallest Vacuum Engine",25,30f,"C2","R-006"))
                    itemRoomDB.ItemDao().insert(Item("S-C2-000003","ProX 950 Gear",22,45f,"C2","R-006"))
                    itemRoomDB.ItemDao().insert(Item("S-C2-000004","ProJet 7000 HD Cover",19,43f,"C2","R-006"))
                    itemRoomDB.ItemDao().insert(Item("S-C2-000005","Selective Laser Sintering (SLS)",15,47f,"C2","R-007"))
                    itemRoomDB.ItemDao().insert(Item("S-C2-000006","sPro 230 Absorber",12,48f,"C2","R-008"))
                    itemRoomDB.ItemDao().insert(Item("S-C2-000007","HTY MultiJet Printers (MJP)",15,50f,"C2","R-008"))
                    itemRoomDB.ItemDao().insert(Item("S-C2-000008","ProJet CJP 660Pro Handle",17,40f,"C2","R-009"))
                    itemRoomDB.ItemDao().insert(Item("S-C2-000009","HTY Stereolithography Printers (SLA)",15,43f,"C2","R-009"))
                    itemRoomDB.ItemDao().insert(Item("S-C2-000010","HTY 3D Jewelry Gear",14,40f,"C2","R-010"))

                    //C3 Item
                    itemRoomDB.ItemDao().insert(Item("S-C3-000001","Gigabyte Motherboard Accessory GC-TPM2.0 SPI 2.0 TPM Module",15,70f,"C3","R-011"))
                    itemRoomDB.ItemDao().insert(Item("S-C3-000002","GIGABYTE GC-TPM2.0_S 2.0 TPM Module (12 Pin 12-1) LPC TPM 2.0 Trusted",17,80f,"C3","R-012"))
                    itemRoomDB.ItemDao().insert(Item("S-C3-000003","Micro Connectors M.2 SSD Mounting Screws Kit for Gigabyte & MSI",25,100f,"C3","R-013"))
                    itemRoomDB.ItemDao().insert(Item("S-C3-000004","RSRUT MSI MPG Z390I GAMING EDGE AC LGA 1151 (300 Series) Intel Z390 HD",19,200f,"C3","R-014"))
                    itemRoomDB.ItemDao().insert(Item("S-C3-000005","PCI-E Express 16x to 1x Powered Riser Card w/ 60cm USB3.0 Cable & MOLEX",25,210f,"C3","R-015"))

                    //C4 Item
                    itemRoomDB.ItemDao().insert(Item("S-C4-000001","HTY Cables for electric panels",15,70f,"C4","R-016"))
                    itemRoomDB.ItemDao().insert(Item("S-C4-000002","HTY Power cables",17,80f,"C4","R-017"))
                    itemRoomDB.ItemDao().insert(Item("S-C4-000003","HTY Armoured cables",25,100f,"C4","R-018"))
                    itemRoomDB.ItemDao().insert(Item("S-C4-000004","HTY Rubber cables",19,200f,"C4","R-019"))
                    itemRoomDB.ItemDao().insert(Item("S-C4-000005","HTY Control cables",25,210f,"C4","R-020"))

                    //C5 Item
                    itemRoomDB.ItemDao().insert(Item("S-C5-000001","Solder Paste Printer",15,700f,"C4","R-021"))
                    itemRoomDB.ItemDao().insert(Item("S-C5-000002","Panasonic Chip Mounter (NPM DX)",17,1500f,"C4","R-022"))
                    itemRoomDB.ItemDao().insert(Item("S-C5-000003","X-Ray Machine",25,23000f,"C4","R-023"))
                    itemRoomDB.ItemDao().insert(Item("S-C5-000004","Digital Microscope",19,25000f,"C4","R-024"))
                    itemRoomDB.ItemDao().insert(Item("S-C5-000005","BGA Rework Station",25,18000f,"C4","R-025"))

                }
            }
        }
    }
}