package com.example.hotayisstore.Outbound

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
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.hotayisstore.MainActivity
import com.example.hotayisstore.R
import com.example.hotayisstore.databinding.ActivityOutboundCompleteBinding
import com.example.hotayisstore.databinding.ActivityOutboundUploadBinding

class Outbound_Complete_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityOutboundCompleteBinding
    lateinit var preferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutboundCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Step 4: Outbound Complete"

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)  //get share preference
        val empID = preferences.getString("EmployeeID","")
        val empIDString = empID.toString()

        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)

        binding.textViewDate.text = formatedDate.toString()
        binding.textViewEIDO.text = empIDString

        binding.buttonTakePhoto.setOnClickListener {
            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i,123)
        }

        binding.buttonComplete.setOnClickListener {
            Toast.makeText(this, "Outbound Record Saved", Toast.LENGTH_SHORT).show()
            val bIntent = Intent(this, MainActivity::class.java)
            val complete: String = "1"
            bIntent.putExtra("complete_No",complete)
            startActivity(bIntent)
            finishAffinity()
        }
    }

    //set image captured as image view
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 123){
            var bmp = data?.extras?.get("data") as Bitmap
            val imageViewPhoto : ImageView = findViewById(R.id.imageViewPhoto)
            imageViewPhoto.setImageBitmap(bmp)
        }
    }

    //back function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}