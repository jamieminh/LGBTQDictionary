package com.jamie.lgbtqdictionary.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.R


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    lateinit var rlAbout: RelativeLayout
    private lateinit var globalProps : GlobalProperties


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        rlAbout = view.findViewById(R.id.rlAboutInfo)

        globalProps = this.context?.applicationContext as GlobalProperties


        // on clicking 'about'
        rlAbout.setOnClickListener {
            globalProps.navStack.push("SETTINGS")
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