package com.jamie.lgbtqdictionary.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.models.words.RoomWord
import com.jamie.lgbtqdictionary.models.words.Word
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModel
import com.jamie.lgbtqdictionary.views.WordDefinitionFragment
import java.util.*

class BookmarksAdapter(
//    var words: List<RoomWord>,
    var roomWordViewModel: RoomWordViewModel,
    var navItemBackStack: Stack<String>,
    private val supportFragmentManager: FragmentManager
) : RecyclerView.Adapter<BookmarksViewHolder>() {

    // a sample init so the app wont crash, the value doesn't matter
    // since it will later be replaced
    var words: List<RoomWord> = listOf(RoomWord("-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1"))
    private lateinit var bookmarkedCard: CardView
    private lateinit var removeBookmark: ImageView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.bookmarked_words_list_layout,
            parent,
            false
        )
        bookmarkedCard = view.findViewById(R.id.cvBookmarkedWord)
        removeBookmark = view.findViewById(R.id.ivRemoveBookmarked)
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
            roomWordViewModel.delete(words[position])
        }
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

    @SuppressLint("SetTextI18n")
    fun setDetails(word: String, definition: String) {
        val bookmarkWord = itemView.findViewById<TextView>(R.id.tvBookmarkedWord)
        val bookmarkDefinition = itemView.findViewById<TextView>(R.id.tvBookmarkedWordDefinition)
        bookmarkWord.text = word
        if (definition.length > 80) {
            bookmarkDefinition.text = definition.substring(0, 80) + "..."
        } else {
            bookmarkDefinition.text = definition
        }
    }
}