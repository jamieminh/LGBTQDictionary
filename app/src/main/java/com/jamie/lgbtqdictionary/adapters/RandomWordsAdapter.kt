package com.jamie.lgbtqdictionary.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.models.words.Word
import com.jamie.lgbtqdictionary.views.WordDefinitionFragment
import java.text.SimpleDateFormat
import java.util.*

class RandomWordsAdapter(
    private val cards: List<Word>,
    private val context: Context,
    private var navItemBackStack: Stack<String>,
    private val supportFragmentManager: FragmentManager
) : PagerAdapter() {

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
        val date = Locale.getDefault()
        word.text = cards[position].word
        pronunciation.text = cards[position].pronunciation
        today.text = SimpleDateFormat("MMM d, yyyy", date).format(Date())

        container.addView(view, 0)

        // set on tapping a card, go to that word definition
        see.setOnClickListener {
            Toast.makeText(this.context, "See definitions", Toast.LENGTH_SHORT).show()
            navItemBackStack.push("HOME")
            val bundle = Bundle()
            val wordDefinitionFragment = WordDefinitionFragment()
            Log.i("Random.Word.Clicked", cards[position].toString())
            bundle.putSerializable("word", cards[position])
            wordDefinitionFragment.arguments = bundle

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragmentContainer, wordDefinitionFragment)
                addToBackStack(null)
                commit()
            }
        }

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}


// made with changes from source: https://codinginsight.live/t_android_swipe_viewpager.html?i=1