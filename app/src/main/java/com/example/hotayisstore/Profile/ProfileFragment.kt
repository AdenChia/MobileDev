package com.example.hotayisstore.Profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.hotayisstore.Home.HomeFragment
import com.example.hotayisstore.MainActivity
import com.example.hotayisstore.R

class ProfileFragment : Fragment() {
    private var ctx: Context? = null
    private var self: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        ctx = container?.context
        self = LayoutInflater.from(ctx).inflate(R.layout.fragment_profile, container, false)

        //received info passed from MainActivity
        val bundle = arguments
        val EID = bundle!!.getString("EID") //Employee ID
        var EPW = bundle!!.getString("EPW") //Employee Password
        val ENE = bundle!!.getString("ENE") //Employee Name
        val EJB = bundle!!.getString("EJB") //Employee Job
        val EPH = bundle!!.getString("EPH") //Employee Phone No

        EPW = EPW?.replace(EPW,"****") //replace password with *

        val empID =self?.findViewById<TextView>(R.id.emp_ID)
        empID?.text = "ID: $EID"

        val profileFullName = self?.findViewById<TextView>(R.id.profile_fullname)
        val profileJob = self?.findViewById<TextView>(R.id.profile_job)
        val profilePhone = self?.findViewById<TextView>(R.id.profile_phone)
        val profilePWD = self?.findViewById<TextView>(R.id.profile_PWD)

        //set text view
        profileFullName?.text = ENE
        profileJob?.text = EJB
        profilePhone?.text = EPH
        profilePWD?.text = EPW

        val logoutButton = self?.findViewById<Button>(R.id.button_profile_logout)
        logoutButton?.setOnClickListener {
            Toast.makeText(ctx, "Successful Logout", Toast.LENGTH_SHORT).show()

            val intent = Intent(ctx, LoginActivity::class.java) //pass to login page
            val clear: String = "clear"
            intent.putExtra("clear",clear)         //clear all share preferences
            startActivity(intent)                        //start Login
            activity?.finishAffinity()                   //clear activity history
        }
        return self
    }

}