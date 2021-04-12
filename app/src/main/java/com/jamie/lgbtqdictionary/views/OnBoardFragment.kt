package com.jamie.lgbtqdictionary.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.adapters.OnBoardPagerAdapter


class OnBoardFragment : Fragment() {

    private var currentPage = 0
    private lateinit var viewPager: ViewPager
    private lateinit var sliderBtn: Button
    private lateinit var pagerDots: LinearLayout
    private lateinit var sliderAdapter: OnBoardPagerAdapter
    private lateinit var globalProps : GlobalProperties

    private lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FragmentActivity) {
            mActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_board, container, false)
        globalProps = mActivity.applicationContext as GlobalProperties

        viewPager = view.findViewById(R.id.vpOnBoard)
        sliderBtn = view.findViewById(R.id.btnPagerBtn)
        pagerDots = view.findViewById(R.id.pagerDots)
        sliderAdapter = OnBoardPagerAdapter(globalProps.context)


        showOnBoard()

        return view
    }

    private fun showOnBoard() {
        viewPager.adapter = sliderAdapter
        addPagerDots(0)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                addPagerDots(position)
                currentPage = position
                sliderBtn.text = if (currentPage != 2) "Next" else "Start"

            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        // go to next page when user tap nex
        sliderBtn.setOnClickListener {
            if (currentPage != 2) {
                viewPager.currentItem = currentPage + 1
            }
            // at the last slide, tap "Start"
            else {
                val homeFragment = HomeFragment()
                mActivity.findViewById<ConstraintLayout>(R.id.bottom_nav_bar).visibility = ConstraintLayout.VISIBLE

                val sharedPrefs = mActivity.application.getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE)
                sharedPrefs.edit().putBoolean("isFirstTime", false).apply()     // set isFirstTime to false

                mActivity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragmentContainer, homeFragment)
                    commit()
                }
            }
        }
    }

    private fun addPagerDots(position: Int) {
        pagerDots.removeAllViews()      // prevent multiple render of the dots
        for (i in 0..2) {
            val dot = TextView(globalProps.context)
            dot.text = HtmlCompat.fromHtml("&#8226", HtmlCompat.FROM_HTML_MODE_COMPACT)
            dot.textSize = 35F
            dot.setTextColor(ContextCompat.getColor(globalProps.context, R.color.cardSlightTransparent))
            if (position == i) {
                dot.setTextColor(ContextCompat.getColor(globalProps.context, R.color.white))
            }
            pagerDots.addView(dot)
        }


    }


}