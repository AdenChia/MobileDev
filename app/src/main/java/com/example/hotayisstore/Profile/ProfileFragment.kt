package com.example.hotayisstore.Profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.hotayisstore.Home.HomeFragment
import com.example.hotayisstore.MainActivity
import com.example.hotayisstore.R

class ProfileFragment : Fragment() {
    private var ctx: Context? = null
    private var self: View? = null
    private val loginFragment = LoginFragment()
    private val homeFragment = HomeFragment()

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        ctx = container?.context
        self = LayoutInflater.from(ctx).inflate(R.layout.fragment_profile, container, false)
        val logoutButton = self?.findViewById<Button>(R.id.buttonLogout)
        logoutButton?.setOnClickListener {
            // Logout validation start here

            Toast.makeText(ctx, "Successful Logout", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).hideMenu()
            (activity as MainActivity).successLogout()
            (activity as MainActivity).replaceFragment(homeFragment)
            (activity as MainActivity).replaceFragmentFull(loginFragment)

        }
        return self
    }

}