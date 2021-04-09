package com.jamie.lgbtqdictionary.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.models.words.Word
import com.jamie.lgbtqdictionary.views.WordDefinitionFragment
import java.util.*


class WordsAdapter(
    private val loader : ProgressBar,
    options: FirebaseRecyclerOptions<Word>,
    private var navItemBackStack: Stack<String>,
    private val supportFragmentManager: FragmentManager
) : FirebaseRecyclerAdapter<Word, CategoryWordsViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryWordsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_layout_words,
            parent,
            false
        )

        return CategoryWordsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryWordsViewHolder, position: Int, model: Word) {
        holder.setDetails(model.word)

        // remove progress bar when the data is available
        loader.visibility = ConstraintLayout.GONE

        holder.itemView.setOnClickListener {
            navItemBackStack.push("CATEGORIES")

            val bundle = Bundle()
            val wordDefinitionFragment = WordDefinitionFragment()
            model.id = getRef(position).key.toString()
            bundle.putSerializable("word", model )
            wordDefinitionFragment.arguments = bundle

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragmentContainer, wordDefinitionFragment)
                addToBackStack(null)
                commit()
            }
        }
    }
}

class CategoryWordsViewHolder(itemVIew: View) :
    RecyclerView.ViewHolder(
        itemVIew
    ) {

    fun setDetails(word: String) {
        val catWord = itemView.findViewById<TextView>(R.id.tvGeneralWord)
        catWord.text = word
    }
}
