package com.jamie.lgbtqdictionary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.models.words.RoomWord
import java.util.*

class BookmarksAdapter(
//    var words: List<RoomWord>,
    var navItemBackStack: Stack<String>,
    private val supportFragmentManager: FragmentManager
): RecyclerView.Adapter<BookmarksViewHolder>() {

    // a sample init so the app wont crash, the value doesn't matter
    // since it will later be replaced
    var words: List<RoomWord> = listOf(RoomWord("-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.general_words_list_layout,
            parent,
            false
        )
        return BookmarksViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarksViewHolder, position: Int) {
        val currentWord = words[position]
        holder.setDetails(currentWord.word)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun setChangedWords(changedWords: List<RoomWord>) {
        words = changedWords
        notifyDataSetChanged()
    }
}

class BookmarksViewHolder(itemVIew: View) :
    RecyclerView.ViewHolder(
        itemVIew
    ) {

    fun setDetails(word: String) {
        val bookmarkWord = itemView.findViewById<TextView>(R.id.tvGeneralWord)
        bookmarkWord.text = word
    }
}