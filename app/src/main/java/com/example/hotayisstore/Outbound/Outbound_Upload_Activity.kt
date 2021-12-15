package com.example.hotayisstore.Outbound

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.hotayisstore.MyApplication
import com.example.hotayisstore.databinding.ActivityOutboundUploadBinding
import com.example.hotayisstore.Data.Entity.Outbound
import com.example.hotayisstore.Data.ViewModel.OutboundViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class Outbound_Upload_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityOutboundUploadBinding

    private val outboundViewModel: OutboundViewModel by viewModels {
        OutboundViewModel.OutboundViewModelFactory((application as MyApplication).outboundRepository)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutboundUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Step 2: Upload Status"

        val outboundID = intent.getStringExtra("outboundID").toString()  //received from collect & outbound item activity
        val billString = intent.getStringExtra("billString").toString()

        //current time
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)
        val dateTime = formatedDate.toString()

        binding.textViewUploadID.text = outboundID

        var data :String = outboundID+", C,"+dateTime+" \n\n" + billString  //prepare QR code information

        //process generate QR code
        data = data.trim()
        if(data.isEmpty()){
            Toast.makeText(this, "Data Error", Toast.LENGTH_SHORT).show()
        }else{
            //system Qr code writer
            val writer = QRCodeWriter()
            try{
                val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE,512,512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565)

                for(x in 0 until width){
                    for(y in 0 until height){
                        bmp.setPixel(x,y,if(bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                    }
                }
                //set the QR code in image view
                binding.imageViewQRCode.setImageBitmap(bmp)
            }catch(e: WriterException){
            }
        }

        binding.buttonUpload.setOnClickListener {
            //upload outbound db
            outboundViewModel.update(Outbound(outboundID,"","C",""))

            Toast.makeText(this, "QR successful uploaded", Toast.LENGTH_LONG).show()
            val bIntent = Intent(this,Outbound_Complete_Activity::class.java)
            startActivity(bIntent)
        }
    }

    //back function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}