package com.jamie.lgbtqdictionary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.jamie.lgbtqdictionary.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    // create view binding for the bottom nav bar
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var categoriesFragment: CategoriesFragment
    private lateinit var bookmarksFragment: BookmarksFragment
    private lateinit var settingsFragment: SettingsFragment
    private var backPressTime : Long = 0

    var backPressedOnce = false

    var mainHeaderStack = Stack<String>()

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


        setInitTab()
        tabBarChangeHandler()

    }

    @SuppressLint("SetTextI18n")
    private fun setInitTab() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.flFragmentContainer)
        Toast.makeText(this, (currentFragment == null).toString(), Toast.LENGTH_LONG).show()

        // only init when the fragmentContainer is empty aka. when app is opened
        // prevent reset when orientation changes
        if (currentFragment == null) {
            // set initial active nav item in the nav bar
            bottom_nav_bar.setItemSelected(R.id.nav_home)
            tvFragmentLabel.text = "HOME"

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
                R.id.nav_bookmarks -> transactNavigationFragment(bookmarksFragment, "BOOKMARKS")
                R.id.nav_settings -> transactNavigationFragment(settingsFragment, "SETTINGS")
            }
        }

        val currentFragment = supportFragmentManager.findFragmentById(R.id.flFragmentContainer)
        Toast.makeText(this, (currentFragment == null).toString(), Toast.LENGTH_LONG).show()
    }

    private fun transactNavigationFragment(fragment: Fragment, label: String) {
        // push the current label before transitioning
        mainHeaderStack.push(tvFragmentLabel.text.toString())
//        Toast.makeText(this, tvFragmentLabel.text.toString(), Toast.LENGTH_LONG).show()
        Toast.makeText(this, "Fragment transaction", Toast.LENGTH_LONG).show()

        tvFragmentLabel.text = label
        mainHeaderStack.toArray().forEach { Log.i("NEWNAV.StackElements", it.toString()) }

        if (fragment is HomeFragment) {
            clHeaderArea.visibility = View.GONE         // hide the general header area
        } else {
            clHeaderArea.visibility = View.VISIBLE      // un-hide the header area
        }


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentContainer, fragment)
            addToBackStack(null)
            commit()
        }

    }

    override fun onBackPressed() {
        if (mainHeaderStack.isEmpty()) {
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
            val prevHeader = mainHeaderStack.pop()
            mainHeaderStack.toArray().forEach { Log.i("BACKSTACK.StackElements", it.toString()) }

            if (prevHeader == "HOME") {
                clHeaderArea.visibility = View.GONE         // hide the general header area
            }
            tvFragmentLabel.text = prevHeader

            // listener when back-stack -> do nothing
            bottom_nav_bar.setOnItemSelectedListener {
            }

            // reverse navigation bar selected item
            when (prevHeader) {
                "HOME" -> bottom_nav_bar.setItemSelected(R.id.nav_home)
                "CATEGORIES" -> bottom_nav_bar.setItemSelected(R.id.nav_categories)
                "BOOKMARKS" -> bottom_nav_bar.setItemSelected(R.id.nav_bookmarks)
                "SETTINGS", "ABOUT" -> bottom_nav_bar.setItemSelected(R.id.nav_settings)
            }

            // after changing the checked nav item, resume the usual setOnItemSelectedListener
            tabBarChangeHandler()
        }
    }

}