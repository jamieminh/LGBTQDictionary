package com.jamie.lgbtqdictionary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.jamie.lgbtqdictionary.R

class OnBoardPagerAdapter(private val context: Context): PagerAdapter() {
    private lateinit var layoutInflater: LayoutInflater

    private val drawables = listOf(
        R.drawable.ic_on_board_communicate,
        R.drawable.ic_on_board_search,
        R.drawable.ic_on_board_bookmark)

    private val headings = listOf(
        "Welcome to LGBTQ Dictionary",
        "Lookup From Anywhere",
        "Bookmark Your Favorite Words")

    private val descriptions = listOf(
        "The first app you need to understand the LGBTQ+ community.",
        "We offer over a hundred words that you can look up from any screen.",
        "So you can review them anytime even when you’re offline!")

    override fun getCount(): Int {
        return headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.on_board_slide, container, false)

        val slideImage = view.findViewById<ImageView>(R.id.ivSlide)
        val slideHeading = view.findViewById<TextView>(R.id.tvSlideLabel)
        val slideDescription = view.findViewById<TextView>(R.id.tvSlideDescription)

        slideImage.setImageResource(drawables[position])
        slideHeading.text = headings[position]
        slideDescription.text = descriptions[position]

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}

// source code: https://www.youtube.com/watch?v=byLKoPgB7yA