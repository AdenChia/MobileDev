package com.example.hotayisstore

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hotayisstore.Home.HomeFragment
import com.example.hotayisstore.Inbound.InboundFragment
import com.example.hotayisstore.Outbound.OutboundFragment
import com.example.hotayisstore.Profile.LoginActivity
import com.example.hotayisstore.Profile.MyDBHelper
import com.example.hotayisstore.Profile.ProfileFragment
import com.example.hotayisstore.Scan_or_Search.ScanOrSearchFragment
import com.example.hotayisstore.databinding.ActivityMainBinding
import com.ismaeldivita.chipnavigation.ChipNavigationBar
private const val CAMERA_REQUEST_CODE = 101
class MainActivity : AppCompatActivity() {

    // Navigation fragment variable
    private val homeFragment = HomeFragment()
    private val outboundFragment = OutboundFragment()
    private val inboundFragment = InboundFragment()
    private val scanOrSearchFragment = ScanOrSearchFragment()
    private val profileFragment = ProfileFragment()
    val menu: ChipNavigationBar by lazy { findViewById(R.id.nav_menu_bar) }   //bottom menu

    private var complete_No :String = "0"  //complete number received if every activity is completed

    private lateinit var binding: ActivityMainBinding
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPermission()                   //ask user for permission on Camera

        complete_No = intent.getStringExtra("complete_No").toString()
        if(complete_No == "null"){        //if complete number is not null, proceed to Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()

        }else{  //if completed number have value, continue activity
            menuInstruction()
            replaceFragment(homeFragment)
        }

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val empID = preferences.getString("EmployeeID","")   //get employee ID from share preference
        val empIDString = empID.toString()

        //employee database helper
        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase

        var args = listOf(empIDString).toTypedArray()

        //variable to store employee data
        var empPWD:String = ""
        var empName:String = ""
        var empJob :String = ""
        var empPhone:String = ""

        var rs = db.rawQuery("SELECT * FROM USERS WHERE UNAME = ?", args)
        if (rs.moveToNext()) {

            val empPWDINT :Int = rs.getColumnIndex("PWD")
            empPWD = rs.getString(empPWDINT)

            val empNameINT :Int = rs.getColumnIndex("FULLNAME")
            empName = rs.getString(empNameINT)

            val empJobINT :Int = rs.getColumnIndex("JOB")
            empJob = rs.getString(empJobINT)

            val empPhoneINT : Int = rs.getColumnIndex("PHONE")
            empPhone = rs.getString(empPhoneINT)

        }

        //pass employee variable value to fragment
        val mBundle = Bundle()
        mBundle.putString("EID",empID)
        mBundle.putString("EPW",empPWD)
        mBundle.putString("ENE",empName)
        mBundle.putString("EJB",empJob)
        mBundle.putString("EPH",empPhone)
        homeFragment.arguments = mBundle //pass to home fragment to display Welcome, Name.
        profileFragment.arguments = mBundle //pass to employee profile to display Employee info

    }

    //fragment in upper of the menu
    //replace fragment with each fragment activity
    fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container,fragment)
            transaction.commit()

        }
    }

    //select menu option
    private fun menuInstruction(){
        //menu instruction
        menu.setItemSelected(R.id.home)
        menu.setOnItemSelectedListener { id ->
            when (id) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.outbound -> replaceFragment(outboundFragment)
                R.id.inbound -> replaceFragment(inboundFragment)
                R.id.scan_or_search -> replaceFragment(scanOrSearchFragment)
                R.id.profile -> replaceFragment(profileFragment)
            }
        }
    }

    //Camera permission
    public fun setupPermission(){
        val permission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    //request permission
    private fun makeRequest(){
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    //user select result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE ->{
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"You need permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}