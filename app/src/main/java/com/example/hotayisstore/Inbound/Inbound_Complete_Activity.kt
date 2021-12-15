package com.example.hotayisstore.Inbound

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.hotayisstore.MainActivity
import com.example.hotayisstore.R
import com.example.hotayisstore.databinding.ActivityInboundCompleteBinding

class Inbound_Complete_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityInboundCompleteBinding
    lateinit var preferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInboundCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Step 4: Inbound Complete"

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)  //get share Preference
        val empID = preferences.getString("EmployeeID","") //get employee ID from share preference
        val empIDString = empID.toString()

        val buttonTakePicture : Button = binding.buttonTakePhoto //button to capture image
        val buttonComplete : Button = binding.buttonComplete     //button to complete inbound process

        //Current Time
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)

        val textViewDate : TextView = binding.textViewDate
        textViewDate.text = formatedDate.toString()  //set Date

        binding.textViewEIDI.text = empIDString      //set Employee ID

        buttonTakePicture.setOnClickListener {
            //CAMERA function
            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i,123)
        }

        buttonComplete.setOnClickListener {
            Toast.makeText(this, "Inbound Record Saved", Toast.LENGTH_SHORT).show()
            val bIntent = Intent(this, MainActivity::class.java)
            val complete: String = "1"
            bIntent.putExtra("complete_No",complete) //pass complete Number to Min Activity
            startActivity(bIntent)
            finishAffinity()
        }
    }

    //function to set image captured to image view
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 123){
            var bmp = data?.extras?.get("data") as Bitmap
            val imageViewPhoto : ImageView = binding.imageViewPhoto
            imageViewPhoto.setImageBitmap(bmp)
        }
    }

    //back function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
