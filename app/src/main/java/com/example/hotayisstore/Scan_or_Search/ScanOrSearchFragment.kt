package com.example.hotayisstore.Scan_or_Search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.hotayisstore.MyApplication
import com.example.hotayisstore.R
import com.example.hotayisstore.Data.ViewModel.ItemViewModel

private const val CAMERA_REQUEST_CODE = 101

class ScanOrSearchFragment : Fragment() {

    private var ctx: Context? = null
    private var self: View? = null

    private var codeScanner: CodeScanner? = null

    private val itemViewModel: ItemViewModel by viewModels{
        ItemViewModel.ItemViewModelFactory((activity?.application as MyApplication).itemRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        ctx = container?.context
        self = LayoutInflater.from(ctx).inflate(R.layout.fragment_scan_or_search, container, false)

        val scannerView = self?.findViewById<CodeScannerView>(R.id.scanner_view_search)  //camera area

        codeScanner = ctx?.let { self?.findViewById<CodeScannerView>(R.id.scanner_view_search)?.let { it1 ->
            CodeScanner(it,
                it1
            )
        } }
        codeScanner?.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner?.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner?.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner?.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner?.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner?.isFlashEnabled = false

        val buttonProceedScanSearch = self?.findViewById<Button>(R.id.buttonProceedScanSearch)
        val buttonShowScan = self?.findViewById<Button>(R.id.buttonShowScan)
        val editTextInputItemID = self?.findViewById<EditText>(R.id.editTextInputItemID)
        var scanSearchResult: String?

        codeScanner?.decodeCallback = DecodeCallback {
            runOnUiThread {
                scanSearchResult = it.text.toString()
                editTextInputItemID?.setText(scanSearchResult)
            }
        }

        //click to show scanner
        buttonShowScan?.setOnClickListener {
            val scanImage = self?.findViewById<ImageView>(R.id.imageViewScanImage)
            val cameraView = self?.findViewById<CodeScannerView>(R.id.scanner_view_search)
            if(cameraView?.visibility == View.INVISIBLE) {
                scanImage?.visibility = View.INVISIBLE
                cameraView?.visibility = View.VISIBLE
                buttonShowScan.setText(R.string.hide)
            }else{
                scanImage?.visibility = View.VISIBLE
                cameraView?.visibility = View.INVISIBLE
                buttonShowScan.setText(R.string.scanItemSearch)
            }
        }

        //click proceed
        buttonProceedScanSearch?.setOnClickListener {

            scanSearchResult = editTextInputItemID?.text.toString()

            //variable to store item in selected
            var itemID :String = ""

            var runOneTime:Int = 0

            //item info input/scanned validation
            itemViewModel.getScanItem(scanSearchResult!!).observe(this, { itemList ->
                itemList?.let {
                    val items = itemList.toString()
                    if (items != "[]") {
                        //val items = itemList.toString()
                        val itemsClean1 = items.replace("[Item(Item_ID=", "")    //result filter
                        val itemsClean2 = itemsClean1.replace("Item_Name=", "")
                        val itemsClean3 = itemsClean2.replace("Quantity=", "")
                        val itemsClean4 = itemsClean3.replace("Price=", "")
                        val itemsClean5 = itemsClean4.replace("Category_ID=", "")
                        val itemsClean6 = itemsClean5.replace("Rack_ID=", "")
                        val itemsClean7 = itemsClean6.replace(")]", "")
                        val itemsClean8 = itemsClean7.replace(" ", "")
                        val itemArrayNice: List<String> = itemsClean7.split(",")
                        val itemArray: List<String> = itemsClean8.split(",")
                        itemID = itemArray[0]
                        itemID.replace(" ","")
                        runOneTime++            //increase if processing getScanItem()

                        if(runOneTime == 1){    //result will return a loop, so we only run the first time
                            val bIntent = Intent(ctx, ItemInfoActivity::class.java)
                            bIntent.putExtra("itemCode",itemID)
                            startActivity(bIntent)
                        }

                    }else{
                        Toast.makeText(ctx, "Item ID Not Found", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        }

        //press camera to rescan
        scannerView?.setOnClickListener{
            codeScanner?.startPreview()
        }

        return self
    }

    //runOnUiThread function in fragment
    private fun Fragment?.runOnUiThread(action: () -> Unit) {
        this ?: return
        if (!isAdded) return // Fragment not attached to an Activity
        activity?.runOnUiThread(action)
    }

    //resume scan
    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    //pause scan
    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

}