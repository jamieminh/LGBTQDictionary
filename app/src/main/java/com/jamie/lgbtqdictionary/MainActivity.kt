package com.jamie.lgbtqdictionary

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jamie.lgbtqdictionary.databinding.ActivityMainBinding
import com.jamie.lgbtqdictionary.models.words.RecentSearchWord
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModel
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModelFactory
import com.jamie.lgbtqdictionary.views.*
import com.yinglan.shadowimageview.ShadowImageView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // create view binding for the bottom nav bar
    lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var categoriesFragment: CategoriesFragment
    private lateinit var bookmarksFragment: BookmarksFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var backBtn: ImageView
    private lateinit var searchBtn: ImageView
    private lateinit var homeAppText: ImageView
    private lateinit var ivNoConnection: ShadowImageView

    private lateinit var globalProps: GlobalProperties
    private var backPressTime: Long = 0

    private lateinit var roomWordViewModel: RoomWordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding = ActivityMainBinding.inflate(layoutInflater)


        // replace the splash screen theme with the main theme BEFORE setContentView
        setTheme(R.style.Theme_LGBTQDictionary)
        setContentView(binding.root)

        // check internet status and display an alert box if not connected
        ivNoConnection = findViewById(R.id.ivNoConnection)
        val internetConnection = InternetConnection(applicationContext)
        val noConnectionAlert = SimpleAlertDialog(
            this,
            "No Internet Connection",
            "Some features may be inaccessible while offline.",
        )
        internetConnection.observe(this, { isConnected ->
            if (!isConnected) {
                noConnectionAlert.show(supportFragmentManager, "No connection")
                ivNoConnection.visibility = ShadowImageView.VISIBLE
            }
            else {
                ivNoConnection.visibility = ShadowImageView.GONE
            }
        })


        // check user stored preference (dark mode and auto rotation)
        val sharedPrefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val isNightMode = sharedPrefs.getBoolean("isNightMode", false)

        if (isNightMode) {      // if user already set night mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        val isAutoRotate = sharedPrefs.getBoolean("isAutoRotate", true)
        if (!isAutoRotate) {      // if user already set lock portrait
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        homeFragment = HomeFragment()
        categoriesFragment = CategoriesFragment()
        bookmarksFragment = BookmarksFragment()
        settingsFragment = SettingsFragment()
        backBtn = findViewById(R.id.ivBackBtn)
        searchBtn = findViewById(R.id.ivSearchBtn)
        homeAppText = findViewById(R.id.HomeAppText)

        backBtn.setOnClickListener { onBackPressed() }
        searchBtn.setOnClickListener { searchForWord() }

        // use navStack as global variable to add labels of nav items to later display then when
        // user press back button. Using global prevent its destruction when orientation changes
        globalProps = application.applicationContext as GlobalProperties

        // if there's no preference of this key, set the val to true
        val isFirstTime = sharedPrefs.getBoolean("isFirstTime", true)

        // if it's user first time (true), show the on board screen, else show Home screen
        if (isFirstTime) {
            // hide headers and nav bar
            homeAppText.visibility = ImageView.GONE
            findViewById<ConstraintLayout>(R.id.clHeaderArea).visibility = ConstraintLayout.GONE
            findViewById<ConstraintLayout>(R.id.bottom_nav_bar).visibility = ConstraintLayout.GONE

            // set main_activity background for on board screen
            window.decorView.setBackgroundColor(R.drawable.on_board_bg)

            // load the onBoardFragment
            val onBoardFragment = OnBoardFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragmentContainer, onBoardFragment)
                commit()
            }
        } else {
            setInitTab()
        }

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
            showAppLogo(true)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragmentContainer, homeFragment)
                commit()
            }
        } else {
            showAppLogo(currentFragment is HomeFragment)
        }

    }

    fun showAppLogo(isHome: Boolean) {
        val guidelineMain = findViewById<Guideline>(R.id.guidelineHeaderArea)
        val params = guidelineMain.layoutParams as ConstraintLayout.LayoutParams

        // if the current tab is Home, show the logo area and hide the back btn
        if (isHome) {
            homeAppText.visibility = ImageView.VISIBLE
            backBtn.visibility = ImageView.GONE

            // enlarge the app logo area
            params.guidePercent = 0.4F
        } else {
            homeAppText.visibility = ImageView.GONE
            backBtn.visibility = ImageView.VISIBLE

            // shrink the app logo area
            params.guidePercent = 0.15F
        }
        guidelineMain.layoutParams = params

    }


    fun tabBarChangeHandler() {
        // this function is only called when user select a non-active nav item
        bottom_nav_bar.setOnItemSelectedListener {
            when (it) {
                R.id.nav_home -> {
                    transactNavigationFragment(homeFragment)
                }
                R.id.nav_categories -> transactNavigationFragment(categoriesFragment)
                R.id.nav_bookmarks -> {
                    binding.bottomNavBar.dismissBadge(R.id.nav_bookmarks)
                    transactNavigationFragment(bookmarksFragment)
                }
                R.id.nav_settings -> transactNavigationFragment(settingsFragment)
            }
        }

    }

    private fun transactNavigationFragment(fragment: Fragment) {
        // push the current label before transitioning
        if (fragment is HomeFragment) {
            // clear back stack
            val count = supportFragmentManager.backStackEntryCount
            for (i in 0 until count) {
                supportFragmentManager.popBackStack()
            }
            globalProps.navStack.clear()
            Log.i("NavStack", globalProps.navStack.size.toString())
        } else {
            pushNavStack()
        }
        globalProps.navStack.toArray().forEach { Log.i("NAVITEM", it.toString()) }

        // if the next fragment is Home, show the logo area and hide the back btn
        showAppLogo(fragment is HomeFragment)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.flFragmentContainer)
        val animations = transactFragmentAnimation(currentFragment!!, fragment)

        supportFragmentManager.beginTransaction().apply {
            // changing fragment animation
            setCustomAnimations(
                animations[0], animations[1], animations[2], animations[3]
            )
            replace(R.id.flFragmentContainer, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun transactFragmentAnimation(
        currentFragment: Fragment,
        nextFragment: Fragment
    ): List<Int> {
        // ensure fragments come in and out in the right directions
        if ((nextFragment is HomeFragment) ||
            (nextFragment is CategoriesFragment) && (currentFragment !is HomeFragment) ||
            (nextFragment is BookmarksFragment) && (currentFragment is SettingsFragment)
        ) {
            return listOf(
                R.anim.slide_in_from_left,
                R.anim.slide_out_from_left,
                R.anim.slide_in_from_right,
                R.anim.slide_out_from_right
            )
        } else {
            return listOf(
                R.anim.slide_in_from_right,
                R.anim.slide_out_from_right,
                R.anim.slide_in_from_left,
                R.anim.slide_out_from_left
            )
        }
    }

    // initiate search when user press Enter on the keyboard
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                searchForWord()
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }

    // function to look for the user input query in firebase
    private fun searchForWord() {
        val searchQuery = etSearchBox.text.toString().trim()
        if (searchQuery != "") {
            val factory = RoomWordViewModelFactory(application)
            roomWordViewModel = ViewModelProvider(this, factory).get(RoomWordViewModel::class.java)
            roomWordViewModel.insertOneRecentSearch(
                RecentSearchWord(
                    searchQuery, (System.currentTimeMillis() / 1000).toInt()
                )
            )
            // send query to the search results fragment, where it will be searched and displayed by the firebase adapter
            val searchResultsFragment = SearchResultsFragment()
            val bundle = Bundle()
            bundle.putString("query", searchQuery)

            pushNavStack()
            searchResultsFragment.arguments = bundle
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragmentContainer, searchResultsFragment)
                addToBackStack(null)
                commit()
            }
        }
        // hide the keyboard from screen
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

    }

    // push the current fragment type into nav name stack
    private fun pushNavStack() {
        when (supportFragmentManager.findFragmentById(R.id.flFragmentContainer)) {
            is HomeFragment -> globalProps.navStack.push("HOME")
            is CategoriesFragment -> globalProps.navStack.push("CATEGORIES")
            is BookmarksFragment -> globalProps.navStack.push("BOOKMARKS")
            is SettingsFragment -> globalProps.navStack.push("SETTINGS")

            // for other fragment, a placeholder item is added
            else -> {
                globalProps.navStack.push("")
            }
        }
    }


    override fun onBackPressed() {
        if (globalProps.navStack.isEmpty()) {
            // if the user press back again within 2s, exit the application
            // source: https://codinginflow.com/tutorials/android/press-back-again-to-exit
            if (backPressTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed()
                return
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_LONG).show()
            }
            backPressTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
            val prevHeader = globalProps.navStack.pop()
            globalProps.navStack.toArray().forEach { Log.i("BACKSTACK", it.toString()) }

            showAppLogo(prevHeader == "HOME")

            // listener when back-stack -> do nothing
            bottom_nav_bar.setOnItemSelectedListener {}

            // reverse navigation bar selected item
            when (prevHeader) {
                "HOME" -> bottom_nav_bar.setItemSelected(R.id.nav_home)
                "CATEGORIES" -> bottom_nav_bar.setItemSelected(R.id.nav_categories)
                "BOOKMARKS" -> bottom_nav_bar.setItemSelected(R.id.nav_bookmarks)
                "SETTINGS" -> bottom_nav_bar.setItemSelected(R.id.nav_settings)
                else -> bottom_nav_bar.setItemSelected(-1)
            }

            // after changing the checked nav item, resume the usual setOnItemSelectedListener
            tabBarChangeHandler()
        }
    }

}