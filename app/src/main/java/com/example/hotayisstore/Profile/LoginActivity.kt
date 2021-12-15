package com.example.hotayisstore.Profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hotayisstore.MainActivity
import com.example.hotayisstore.R
import com.example.hotayisstore.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var sharedPreferences: SharedPreferences
    var isRemembered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar!!.hide()

        //User Database
        var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase

        //generate share preference if selected check box
        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)

        val clear = intent.getStringExtra("clear").toString()  //received from employee profile log out

        if (isRemembered){
            if(clear == "clear"){ //clear all share when click on log out
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

            }else{  //direct login next time if not click on log out
                val intent = Intent(this,MainActivity::class.java)
                val complete: String = "1"
                intent.putExtra("complete_No",complete)
                startActivity(intent)
                finishAffinity()
            }
        }

        binding.buttonLogin.setOnClickListener {
            val empID = binding.loginEmpId.text.toString()
            val empPassword: String = binding.loginEmpPassword.text.toString()


            // Argument to compare user input with DB
            var args = listOf<String>(empID, empPassword).toTypedArray()
            //Cursor
            var rs = db.rawQuery("SELECT * FROM USERS WHERE UNAME = ? AND PWD = ?", args)

            //if User valid
            if (rs.moveToNext()) {

                val rememberMe: Boolean = binding.rememberMe.isChecked //check box
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("EmployeeID", empID)
                editor.putBoolean("CHECKBOX", rememberMe)
                editor.apply()

                //proceed to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("empID", empID)
                intent.putExtra("complete_No","1")
                startActivity(intent)
                finishAffinity()

                Toast.makeText(applicationContext, "Successfully Log In", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "Incorrect ID and Password", Toast.LENGTH_LONG).show()
            }
        }
    }
}