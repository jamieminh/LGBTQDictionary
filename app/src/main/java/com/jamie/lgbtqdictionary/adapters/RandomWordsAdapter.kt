package com.jamie.lgbtqdictionary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.models.words.Word
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class RandomWordsAdapter (private val cards: List<Word>, private val context: Context) : PagerAdapter() {

    private lateinit var layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return cards.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.word_of_day_card, container, false)

        // get card items
        val word = view.findViewById<TextView>(R.id.tvRandomWordWord)
        val pronunciation = view.findViewById<TextView>(R.id.tvRandomWordSpelling)
        val today = view.findViewById<TextView>(R.id.tvRandomWordDate)
        val see = view.findViewById<TextView>(R.id.tvRandomWordSee)

        // populate card items data
        val date = LocalDateTime.now();
        word.text = cards[position].word
        pronunciation.text = cards[position].pronunciation
        today.text = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

        container.addView(view, 0)

        // set on tapping a card
        see.setOnClickListener {
            Toast.makeText(this.context, "See definitions", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}



// made with changes from source: https://codinginsight.live/t_android_swipe_viewpager.html?i=1