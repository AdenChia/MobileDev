package com.example.hotayisstore.Inbound

import android.content.Context
import com.example.hotayisstore.Inbound.InboundActivity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.example.hotayisstore.R

class InboundFragment : Fragment() {
    private var ctx: Context? = null
    private var self: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        ctx = container?.context
        self = LayoutInflater.from(ctx).inflate(R.layout.fragment_inbound, container, false)

        val bDaButton = self?.findViewById<Button>(R.id.button_continue_inbound)
        val bDaCheckBox = self?.findViewById<CheckBox>(R.id.checkBoxAgree)

        bDaButton?.setOnClickListener {

            if (bDaCheckBox != null) {
                if(bDaCheckBox.isChecked){ //checkbox checked
                    val bIntent = Intent(ctx,InboundActivity::class.java)
                    startActivity(bIntent)
                }else{
                    Toast.makeText(ctx, "Please agree with the rule!", Toast.LENGTH_LONG).show()
                }
            }
        }
        return self

    }

}