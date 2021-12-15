package com.example.hotayisstore.Home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.hotayisstore.MainActivity
import com.example.hotayisstore.MyApplication
import com.example.hotayisstore.Outbound.OutboundFragment
import com.example.hotayisstore.R

import com.example.hotayisstore.databinding.FragmentHomeBinding
import com.example.hotayisstore.Data.ViewModel.OutboundViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //Calling Outbound View Model from Myapplication
    private val outboundViewModel: OutboundViewModel by viewModels {
        OutboundViewModel.OutboundViewModelFactory((activity?.application as MyApplication).outboundRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val bundle = arguments
        val ENE = bundle!!.getString("ENE") //Name from MainActivity.

        //Welcome Name
        binding.textViewWelcome.text = ENE

        //collect all outbound data from Outbound Room db with outbound view model
        outboundViewModel.allOutbound.observe(this, { outboundList ->
            outboundList?.let {

                var P :Int = 0 //Processing count
                var I :Int = 0 //Incomplete count
                var C :Int = 0 //Completed count

                for (Outbound in outboundList) {

                    val items = Outbound.toString()

                    val itemsClean1 = items.replace("Outbound(Outbound_ID=","")     // Filter the result returned
                    val itemsClean3 = itemsClean1.replace("Date=","")
                    val itemsClean4 = itemsClean3.replace("Status=","")
                    val itemsClean5 = itemsClean4.replace("Address=","")
                    val itemsClean6 = itemsClean5.replace(")","")
                    val itemsClean7 = itemsClean6.replace(" ","")
                    val itemArray: List<String> = itemsClean7.split(",")

                    val outboundStatus: String = itemArray[2]

                    if(outboundStatus == "I"){
                        I++
                    }

                    if(outboundStatus == "P"){
                        P++
                    }

                    if(outboundStatus == "C"){
                        C++
                    }
                }

                binding.textViewCCount.text = C.toString()
                binding.textViewPCount.text = P.toString()
                binding.textViewICount.text = I.toString()

            }
        })

        //Click and link to outbound fragment
        binding.outboundItem.setOnClickListener {
            (activity as MainActivity).menu.setItemSelected(R.id.outbound)
            (activity as MainActivity).replaceFragment(OutboundFragment())
        }

        //Click and link to report Item problem
        binding.reportItem.setOnClickListener {
            val intent = Intent(context, ReportActivity::class.java)
            startActivity(intent)
        }

        //Click and link to All Item List
        binding.allItem.setOnClickListener {
            val intent = Intent(context, AllItemActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}