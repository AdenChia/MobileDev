package com.example.hotayisstore.Inbound

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.hotayisstore.R

class InboundFragment : Fragment() {
    private var ctx: Context? = null
    private var self: View? = null

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        ctx = container?.context
        self = LayoutInflater.from(ctx).inflate(R.layout.fragment_inbound, container, false)
        val bDaButton = self?.findViewById<Button>(R.id.button_test)
        bDaButton?.setOnClickListener {
            Toast.makeText(ctx, "button works!", Toast.LENGTH_SHORT).show()

        }
        return self

    }

}