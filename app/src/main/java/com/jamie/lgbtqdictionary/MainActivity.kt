package com.jamie.lgbtqdictionary

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.jamie.lgbtqdictionary.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // create view binding for the bottom nav bar
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var categoriesFragment: CategoriesFragment
    private lateinit var bookmarksFragment: BookmarksFragment
    private lateinit var settingsFragment: SettingsFragment


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

        // set initial active nav item in the nav bar
        binding.bottomNavBar.setItemSelected(R.id.nav_home)
        // setup home fragment as the first fragment user see
        Log.i("HeaderAreaOnCreate", clHeaderArea.visibility.toString())
        clHeaderArea.visibility = View.GONE;
        Log.i("HeaderAreaOnCreate", clHeaderArea.visibility.toString())

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentContainer, homeFragment)
            commit()
        }
//
        setUpTabBar()

    }

    private fun setUpTabBar() {
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.nav_home -> transactNavigationFragment(homeFragment, "HOME")
                R.id.nav_categories -> transactNavigationFragment(categoriesFragment, "CATEGORIES")
                R.id.nav_bookmarks -> transactNavigationFragment(bookmarksFragment, "BOOKMARKS")
                R.id.nav_settings -> transactNavigationFragment(settingsFragment, "SETTINGS")
            }
        }
    }

    private fun transactNavigationFragment(fragment: Fragment, label: String) {

        tvFragmentLabel.text = label
        Log.i("HeaderArea", clHeaderArea.visibility.toString())

        if (fragment is HomeFragment) {
            // hide the general header area
            clHeaderArea.visibility = View.GONE;
        }
        else {
//            val currentFragment = supportFragmentManager.findFragmentById(R.id.flFragmentContainer)
            // un-hide the header area
//            if (currentFragment is HomeFragment)
            clHeaderArea.visibility = View.VISIBLE;
        }


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentContainer, fragment)
            addToBackStack(null)
            commit()
        }
        Log.i("HeaderArea", clHeaderArea.visibility.toString())

    }

}