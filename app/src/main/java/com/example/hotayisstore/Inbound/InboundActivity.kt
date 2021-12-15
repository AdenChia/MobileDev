package com.example.hotayisstore.Inbound

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.hotayisstore.R
import com.example.hotayisstore.databinding.ActivityInboundBinding

private const val CAMERA_REQUEST_CODE = 101

class InboundActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: ActivityInboundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInboundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Step 1: Scan Item Bar Code"

        val scannerView = binding.scannerView  //Camera Area
        codeScanner = CodeScanner(this, scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {

                val scanResult = it.text.toString()
                val scanID : TextView = binding.tvTextView
                if(scanResult.take(3) == "S-C"){ //simple barcode validation
                    scanID.text = scanResult
                }else{
                    Toast.makeText(this, "This is not an Item Code", Toast.LENGTH_SHORT).show()
                }

                //button to proceed to record item
                val buttonProceedInfo : Button = binding.buttonProceedInfo
                buttonProceedInfo.setOnClickListener {

                    //transfer to record activity
                    if(scanResult.take(3) == "S-C"){
                        val bIntent = Intent(this,Inbound_Record_Activity::class.java)
                        bIntent.putExtra("itemCode",scanResult)
                        startActivity(bIntent)
                    }else{
                        Toast.makeText(this, "Please Scan Again", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

        //press camera area to continue scan
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    //back function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    //resume scan
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    //pause scan
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

}