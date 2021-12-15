package com.example.hotayisstore.Profile

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(context: Context) : SQLiteOpenHelper(context,"USERDB",null,1){

    //employee DB
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT, UNAME TEXT, PWD TEXT, FULLNAME TEXT, JOB TEXT, PHONE TEXT)")
        db?.execSQL("INSERT INTO USERS(UNAME,PWD,FULLNAME,JOB,PHONE) VALUES('emp001','1111','Aden Chia Kit Yao','Senior Warehouse Worker','+60109018293')")
        db?.execSQL("INSERT INTO USERS(UNAME,PWD,FULLNAME,JOB,PHONE) VALUES('emp002','2222','Eddy Loh Hai Lun','Warehouse Worker','+60123456789')")
        db?.execSQL("INSERT INTO USERS(UNAME,PWD,FULLNAME,JOB,PHONE) VALUES('emp003','3333','Chong Jing Yung','Warehouse Worker','+60123453453')")
        db?.execSQL("INSERT INTO USERS(UNAME,PWD,FULLNAME,JOB,PHONE) VALUES('emp004','4444','Aston Chew Man Hee','Warehouse Worker','+60123542342')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}