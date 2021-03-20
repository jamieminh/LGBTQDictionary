package com.jamie.lgbtqdictionary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jamie.lgbtqdictionary.databinding.ActivityMainBinding


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    lateinit var rlAbout: RelativeLayout
    lateinit var fragmentLabel : TextView
    lateinit var binding: ActivityMainBinding


    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        rlAbout = view.findViewById(R.id.rlAboutInfo)
        fragmentLabel = activity!!.findViewById(R.id.tvFragmentLabel)
        binding = ActivityMainBinding.inflate(layoutInflater)


        // on clicking 'about'
        rlAbout.setOnClickListener{
            fragmentLabel.text = "ABOUT"
            val aboutFragment = AboutFragment()
            val fragmentManager = activity!!.supportFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.flFragmentContainer, aboutFragment)
                addToBackStack(null)
                commit()
            }
        }

        return view
    }


}