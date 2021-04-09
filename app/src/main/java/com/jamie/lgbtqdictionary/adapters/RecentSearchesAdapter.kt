package com.jamie.lgbtqdictionary.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.models.words.RecentSearchWord
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModel
import com.jamie.lgbtqdictionary.views.SearchResultsFragment
import java.util.*

class RecentSearchesAdapter(
    var roomWordViewModel: RoomWordViewModel,
    var navItemBackStack: Stack<String>,
    private val supportFragmentManager: FragmentManager
) : RecyclerView.Adapter<RecentSearchesViewHolder>() {

    // a placeholder so the app wont crash, the value doesn't matter, it will later be replaced
    var words: List<RecentSearchWord> = listOf(RecentSearchWord("-1",  -1))
    private lateinit var recentWordCard: CardView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_layout_words,
            parent,
            false
        )
        recentWordCard = view.findViewById(R.id.cvWordGeneral)
        return RecentSearchesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentSearchesViewHolder, position: Int) {
        val currentWord = words[position]
        holder.setDetails(currentWord.word)

        // on clicking a bookmarked word, go to its definition page
        recentWordCard.setOnClickListener {
            Log.i("RECENTSEARCHES.Card", "Clicked")
            navItemBackStack.push("HOME")

            val bundle = Bundle()
            val searchResultsFragment = SearchResultsFragment()
            bundle.putSerializable("query", currentWord.word)
            searchResultsFragment.arguments = bundle

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragmentContainer, searchResultsFragment)
                addToBackStack(null)
                commit()
            }
        }

    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun setChangedWords(changedWords: List<RecentSearchWord>) {
        words = changedWords
        notifyDataSetChanged()
    }
}

class RecentSearchesViewHolder(itemVIew: View) :
    RecyclerView.ViewHolder(
        itemVIew
    ) {

    @SuppressLint("SetTextI18n")
    fun setDetails(word: String) {
        val recentWord = itemView.findViewById<TextView>(R.id.tvGeneralWord)
        recentWord.text = word
    }
}