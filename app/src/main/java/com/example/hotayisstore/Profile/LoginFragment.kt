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

class LoginFragment : Fragment() {
    private var ctx: Context? = null
    private var self: View? = null
    private val homeFragment = HomeFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ctx = container?.context
        self = LayoutInflater.from(ctx).inflate(R.layout.fragment_login, container, false)
        val loginButton = self?.findViewById<Button>(R.id.buttonLogin)
        loginButton?.setOnClickListener {
            // Login validation start here

            Toast.makeText(ctx, "Successful Login!", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).showMenu()
            (activity as MainActivity).successLogin()
            (activity as MainActivity).replaceFragment(homeFragment)

        }
        return self
    }

}