package com.jamie.lgbtqdictionary.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.models.words.BookmarkedWord
import com.jamie.lgbtqdictionary.models.words.Word
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModel
import com.jamie.lgbtqdictionary.views.WordDefinitionFragment
import java.util.*

class BookmarksAdapter(
    var roomWordViewModel: RoomWordViewModel,
    var navItemBackStack: Stack<String>,
    private val supportFragmentManager: FragmentManager
) : RecyclerView.Adapter<BookmarksViewHolder>() {

    // a placeholder so the app wont crash, the value doesn't matter, it will later be replaced
    var words: List<BookmarkedWord> = listOf(BookmarkedWord("", "", "", "", "", "", "", ""))
    private lateinit var bookmarkedCard: ConstraintLayout
    private lateinit var removeBookmark: RelativeLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_layout_bookmarked_words,
            parent,
            false
        )
        bookmarkedCard = view.findViewById(R.id.clBookmarkedWord)
        removeBookmark = view.findViewById(R.id.rlRemoveBookmark)
        return BookmarksViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarksViewHolder, position: Int) {
        val currentWord = words[position]
        holder.setDetails(currentWord.word, currentWord.definition)

        // on clicking a bookmarked word, go to its definition page
        bookmarkedCard.setOnClickListener {
            Log.i("Bookmark.Card", "Clicked")
            navItemBackStack.push("BOOKMARKS")

            val bundle = Bundle()
            val wordDefinitionFragment = WordDefinitionFragment()
            val word = Word(
                currentWord.word,
                currentWord.id,
                currentWord.pronunciation,
                currentWord.definition,
                currentWord.extent,
                "",
                currentWord.offensive,
                currentWord.source,
                currentWord.flag
            )
            bundle.putSerializable("word", word)
            wordDefinitionFragment.arguments = bundle

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragmentContainer, wordDefinitionFragment)
                addToBackStack(null)
                commit()
            }
        }

        removeBookmark.setOnClickListener {
            roomWordViewModel.deleteBookmark(words[position])
        }
    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun setChangedWords(changedWords: List<BookmarkedWord>) {
        words = changedWords
        notifyDataSetChanged()
    }
}

class BookmarksViewHolder(itemVIew: View) :
    RecyclerView.ViewHolder(
        itemVIew
    ) {

    @SuppressLint("SetTextI18n")
    fun setDetails(word: String, definition: String) {
        val bookmarkWord = itemView.findViewById<TextView>(R.id.tvBookmarkedWord)
        val bookmarkDefinition = itemView.findViewById<TextView>(R.id.tvBookmarkedWordDefinition)
        bookmarkWord.text = word
        if (definition.length > 70) {
            bookmarkDefinition.text = definition.substring(0, 70) + "..."
        } else {
            bookmarkDefinition.text = definition
        }
    }
}