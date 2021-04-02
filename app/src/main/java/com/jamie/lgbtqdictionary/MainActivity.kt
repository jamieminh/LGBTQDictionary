package com.jamie.lgbtqdictionary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.jamie.lgbtqdictionary.databinding.ActivityMainBinding
import com.jamie.lgbtqdictionary.views.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // create view binding for the bottom nav bar
    lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var categoriesFragment: CategoriesFragment
    private lateinit var bookmarksFragment: BookmarksFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var backBtn: ImageView

    private lateinit var globalProps : GlobalProperties

    private var backPressTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // replace the splash screen theme with the main theme BEFORE setContentView
        setTheme(R.style.Theme_LGBTQDictionary)
        setContentView(binding.root)

        // stop dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        homeFragment = HomeFragment()
        categoriesFragment = CategoriesFragment()
        bookmarksFragment = BookmarksFragment()
        settingsFragment = SettingsFragment()
        backBtn = findViewById(R.id.ivBackBtn)

        backBtn.setOnClickListener { onBackPressed() }

        // use navStack as global variable to add labels of nav items to later display then when
        // user press back button. Using global prevent its destruction when orientation changes
        globalProps = application.applicationContext as GlobalProperties

        setInitTab()
        tabBarChangeHandler()


    }

    @SuppressLint("SetTextI18n")
    private fun setInitTab() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.flFragmentContainer)

        // only init when the fragmentContainer is empty aka. when app is opened
        // prevent reset when orientation changes
        if (currentFragment == null) {
            // set initial active nav item in the nav bar
            bottom_nav_bar.setItemSelected(R.id.nav_home)
            // setup home fragment as the first fragment user see
            clHeaderArea.visibility = View.GONE

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragmentContainer, homeFragment)
                commit()
            }
        }

        // if the current tab is Home, prevent the general header from showing
        if (currentFragment is HomeFragment) {
            clHeaderArea.visibility = View.GONE
        }
    }


    private fun tabBarChangeHandler() {
        bottom_nav_bar.setOnItemSelectedListener {
            when (it) {
                R.id.nav_home -> transactNavigationFragment(homeFragment, "HOME")
                R.id.nav_categories -> transactNavigationFragment(categoriesFragment,"CATEGORIES")
                R.id.nav_bookmarks -> {
                    binding.bottomNavBar.dismissBadge(R.id.nav_bookmarks)
                    transactNavigationFragment(bookmarksFragment, "BOOKMARKS")
                }
                R.id.nav_settings -> transactNavigationFragment(settingsFragment, "SETTINGS")
            }
        }

    }

    private fun transactNavigationFragment(fragment: Fragment, label: String) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.flFragmentContainer)
//        Toast.makeText(this, currentFragment.toString(), Toast.LENGTH_LONG).show()

        // add the current fragment label to a stack to later pop them and set selected item accordingly
        when(currentFragment) {
            is HomeFragment -> globalProps.navStack.push("HOME")
            is CategoriesFragment, is WordsFragment -> globalProps.navStack.push("CATEGORIES")
            is BookmarksFragment -> globalProps.navStack.push("BOOKMARKS")
            is SettingsFragment, is AboutFragment -> globalProps.navStack.push("SETTINGS")
            // handle cases for WordDefinitionFragment, because this one can be init from multiple places
            else -> {
                val fromFragment = globalProps.navStack.peek()
                globalProps.navStack.push(fromFragment)
            }
        }
        // push the current label before transitioning
        globalProps.navStack.toArray().forEach { Log.i("NAVITEM", it.toString()) }

        if (fragment is HomeFragment) {
            clHeaderArea.visibility = View.GONE         // hide the general header area
        } else {
            clHeaderArea.visibility = View.VISIBLE      // un-hide the header area
        }


        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_from_right,
                R.anim.slide_in_from_left,
                R.anim.slide_out_from_left
            )
            replace(R.id.flFragmentContainer, fragment)
            addToBackStack(null)
            commit()
        }

    }

    override fun onBackPressed() {
        if (globalProps.navStack.isEmpty()) {
            // if the user press back again within 2s, exit the application
            // source: https://codinginflow.com/tutorials/android/press-back-again-to-exit
            if (backPressTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed()
                return
            }
            else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_LONG).show()
            }
            backPressTime = System.currentTimeMillis()
        }
        else {
            super.onBackPressed()
            val prevHeader = globalProps.navStack.pop()
            globalProps.navStack.toArray().forEach { Log.i("BACKSTACK", it.toString()) }

            if (prevHeader == "HOME") {
                clHeaderArea.visibility = View.GONE         // hide the general header area
            }

            // listener when back-stack -> do nothing
            bottom_nav_bar.setOnItemSelectedListener {
            }

            // reverse navigation bar selected item
            when (prevHeader) {
                "HOME" -> bottom_nav_bar.setItemSelected(R.id.nav_home)
                "CATEGORIES" -> bottom_nav_bar.setItemSelected(R.id.nav_categories)
                "BOOKMARKS" -> bottom_nav_bar.setItemSelected(R.id.nav_bookmarks)
                "SETTINGS" -> bottom_nav_bar.setItemSelected(R.id.nav_settings)
            }

            // after changing the checked nav item, resume the usual setOnItemSelectedListener
            tabBarChangeHandler()
        }
    }

}