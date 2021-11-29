package com.example.hotayisstore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.hotayisstore.Home.HomeFragment
import com.example.hotayisstore.Inbound.InboundFragment
import com.example.hotayisstore.Outbound.OutboundFragment
import com.example.hotayisstore.Profile.LoginFragment
import com.example.hotayisstore.Profile.ProfileFragment
import com.example.hotayisstore.Scan_or_Search.ScanOrSearchFragment
import com.example.hotayisstore.databinding.ActivityMainBinding
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {

    // Navigation variable
    private val homeFragment = HomeFragment()
    private val outboundFragment = OutboundFragment()
    private val inboundFragment = InboundFragment()
    private val scanOrSearchFragment = ScanOrSearchFragment()
    private val profileFragment = ProfileFragment()
    private val menu by lazy { findViewById<ChipNavigationBar>(R.id.nav_menu_bar) }

    private val loginFragment = LoginFragment()


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideMenu()
        val fm: FragmentManager = supportFragmentManager
        fm.beginTransaction().add(R.id.fragment_container_full, loginFragment).commit()

        menuInstruction()


    }

    fun hideMenu(){
        binding.navMenuBar.visibility = View.INVISIBLE
        binding.fragmentContainer.visibility = View.INVISIBLE
    }

    fun showMenu(){
        binding.navMenuBar.visibility = View.VISIBLE
        binding.fragmentContainer.visibility = View.VISIBLE
    }

    fun successLogin(){
        binding.fragmentContainerFull.visibility = View.INVISIBLE
        menu.setItemSelected(R.id.home)
    }

    fun successLogout(){
        binding.fragmentContainerFull.visibility = View.VISIBLE
    }

    fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container,fragment)
            transaction.commit()
        }
    }

    fun replaceFragmentFull(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_full,fragment)
            transaction.commit()
        }
    }

    private fun menuInstruction(){
        //menu instruction
        menu.setItemSelected(R.id.home)
        menu.setOnItemSelectedListener { id ->
            when (id) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.outbound -> replaceFragment(outboundFragment)
                R.id.inbound -> replaceFragment(inboundFragment)
                R.id.scan_or_search -> replaceFragment(scanOrSearchFragment)
                R.id.profile -> replaceFragment(profileFragment)
            }
        }
    }
}