package com.example.hotayisstore.Inbound

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.hotayisstore.Inbound.InboundActivity
import com.example.hotayisstore.R
import com.example.hotayisstore.databinding.ActivityInboundStoringBinding

private const val CAMERA_REQUEST_CODE = 101

class Inbound_Storing_Activity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: ActivityInboundStoringBinding

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInboundStoringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Step 3: Storing Inbound Item"

        val rackCode = intent.getStringExtra("rackCode").toString() //rack code received from inbound record activity
        val scanID : TextView = binding.tvTextViewRack

        scanID.text = "Scan $rackCode Code"

        val scannerViewRack = binding.scannerViewRack    //camera area
        codeScanner = CodeScanner(this, scannerViewRack)

        // Parameters (default values)
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
                if(scanResult == rackCode){
                    scanID.text = scanResult
                }else{
                    Toast.makeText(this, "This is not an $rackCode Code", Toast.LENGTH_SHORT).show()
                }

                val buttonProceedInfoRack : Button = binding.buttonProceedInfoRack
                buttonProceedInfoRack.setOnClickListener {
                    //transfer to record activity
                    if(scanResult == rackCode){
                        Toast.makeText(this, "Item successful store in rack", Toast.LENGTH_LONG).show()
                        val bIntent = Intent(this,Inbound_Complete_Activity::class.java)
                        startActivity(bIntent)
                    }else{
                        Toast.makeText(this, "Please Scan Again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //press camera area to rescan
        scannerViewRack.setOnClickListener {
            codeScanner.startPreview()
        }

        val imageButtonRackPopup: ImageButton = binding.imageButtonRackPopup

        // Click to pop up Rack location
        imageButtonRackPopup.setOnClickListener {
            // Initialize a new layout inflater instance
            val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // Inflate a custom view using layout inflater
            val view = inflater.inflate(R.layout.rackpopup, null)
            // Initialize a new instance of popup window

            val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )
            // Set an elevation for the popup window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.elevation = 10.0F
            }

            // If API level 23 or higher then execute the code
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.BOTTOM
                popupWindow.exitTransition = slideOut

                popupWindow.isOutsideTouchable = true
                popupWindow.isFocusable = true

            }

            val inboundRackPopup = binding.inboundRackPopup
            // Show the popup window on app
            TransitionManager.beginDelayedTransition(inboundRackPopup)
            popupWindow.showAtLocation(
                inboundRackPopup, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                50 // Y offset
            )
        }
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

    //back function
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}