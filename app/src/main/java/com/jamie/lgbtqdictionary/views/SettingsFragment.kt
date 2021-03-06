package com.jamie.lgbtqdictionary.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.R


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var clDarkMode: ConstraintLayout
    private lateinit var clAutoRotate: ConstraintLayout
    private lateinit var clReport: ConstraintLayout
    private lateinit var clShareApp: ConstraintLayout
    private lateinit var clAbout: ConstraintLayout
    private lateinit var darkModeSwitch: SwitchCompat
    private lateinit var autoRotateSwitch: SwitchCompat

    private lateinit var rgSoundSpeed: RadioGroup

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var globalProps : GlobalProperties

    private lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            mActivity = context
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        clDarkMode = view.findViewById(R.id.clToggleDarkMode)
        clAutoRotate = view.findViewById(R.id.clToggleAutoRotation)
        clReport = view.findViewById(R.id.clReport)
        clAbout = view.findViewById(R.id.clAboutInfo)
        darkModeSwitch = view.findViewById(R.id.swDarkMode)
        autoRotateSwitch = view.findViewById(R.id.swAutoRotate)
        rgSoundSpeed = view.findViewById(R.id.rgSoundSpeed)

        globalProps = this.context?.applicationContext as GlobalProperties
        sharedPrefs = mActivity.getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE)

        // set the checked radio button
        when (globalProps.ttsSpeed ) {
            0.1F -> rgSoundSpeed.check(R.id.rbSoundSpeedSlower)
            0.3F -> rgSoundSpeed.check(R.id.rbSoundSpeedSlow)
            0.5F -> rgSoundSpeed.check(R.id.rbSoundSpeedNormal)
        }

        // set state of dark mode and auto rotate switches
        if (sharedPrefs.getBoolean("isNightMode", false)) {
            darkModeSwitch.isChecked = true
        }
        if (sharedPrefs.getBoolean("isAutoRotate", true)) {
            autoRotateSwitch.isChecked = true
        }



        // on clicking 'toggle dark mode'
        clDarkMode.setOnClickListener { toggleDarkMode(true) }
        darkModeSwitch.setOnClickListener { toggleDarkMode(false) }

        // on clicking 'toggle auto rotation'
        clAutoRotate.setOnClickListener { toggleAutoRotation(true) }
        autoRotateSwitch.setOnClickListener { toggleAutoRotation(false) }
        // onClick change sound speed
        changeSoundSpeed()

        clReport.setOnClickListener { sendMail() }


        // on clicking 'about'
        clAbout.setOnClickListener { about() }

        return view
    }

    private fun toggleDarkMode(tapCard: Boolean) {
        val isNightModeOn = sharedPrefs.getBoolean("isNightMode", false)

        if ((!tapCard && darkModeSwitch.isChecked) || (tapCard && !isNightModeOn)) {
            // turn on night mode and set isNightMode to false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPrefs.edit().putBoolean("isNightMode", true).apply()
            darkModeSwitch.isChecked = true
        }
        else if((!tapCard && !darkModeSwitch.isChecked) || (tapCard && isNightModeOn)) {
            // turn off night mode and set isNightMode to false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPrefs.edit().putBoolean("isNightMode", false).apply()     // set isFirstTime to false
            darkModeSwitch.isChecked = false
        }
    }

    private fun toggleAutoRotation(tapCard: Boolean) {
        val isAutoRotate = sharedPrefs.getBoolean("isAutoRotate", true)
        if ((!tapCard && autoRotateSwitch.isChecked) || (tapCard && !isAutoRotate)) {
            // auto rotate based on user phone setting and set isAutoRotate to true
            activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            sharedPrefs.edit().putBoolean("isAutoRotate", true).apply()
            autoRotateSwitch.isChecked = true
        }
        else if ((!tapCard && !autoRotateSwitch.isChecked) || (tapCard && isAutoRotate)){
            // no rotate, always in portrait mode and set isAutoRotate to false
            activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            sharedPrefs.edit().putBoolean("isAutoRotate", false).apply()
            autoRotateSwitch.isChecked = false
        }
    }

    private fun changeSoundSpeed() {
        rgSoundSpeed.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.rbSoundSpeedNormal -> {
                    globalProps.ttsSpeed = 0.5F
                    sharedPrefs.edit().putFloat("soundSpeed", 0.5F).apply()
                }
                R.id.rbSoundSpeedSlow -> {
                    globalProps.ttsSpeed = 0.3F
                    sharedPrefs.edit().putFloat("soundSpeed", 0.3F).apply()
                }
                R.id.rbSoundSpeedSlower -> {
                    globalProps.ttsSpeed = 0.1F
                    sharedPrefs.edit().putFloat("soundSpeed", 0.1F).apply()
                }
            }
        }
    }


    private fun sendMail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("lgbtqdictionary2021@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Report Bugs/Make Suggestions")
        intent.putExtra(Intent.EXTRA_TEXT,
            "Please explain the bug(s) you have encountered, " +
                "or any suggestion to improve the app in details. " +
                "We will try our best to fix/improve the app as soon as possible. Thank you.")

        intent.type = "message/rfc822"

        // what user see when the app chooser is opened
        startActivity(Intent.createChooser(intent, "Choose an email service"))
    }


    private fun about() {
        globalProps.navStack.push("SETTINGS")
        mActivity.findViewById<ChipNavigationBar>(R.id.bottom_nav_bar).setItemSelected(-1)

        val aboutFragment = AboutFragment()
        val fragmentManager = mActivity.supportFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentContainer, aboutFragment)
            addToBackStack(null)
            commit()
        }
    }


}