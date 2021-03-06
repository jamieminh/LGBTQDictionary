package com.jamie.lgbtqdictionary.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.models.words.Word
import com.jamie.lgbtqdictionary.views.WordDefinitionFragment
import java.util.*


class SearchWordAdapter(
    options: FirebaseRecyclerOptions<Word>,
    private var navItemBackStack: Stack<String>,
    private val supportFragmentManager: FragmentManager
) : FirebaseRecyclerAdapter<Word, SearchWordsViewHolder>(options) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchWordsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_layout_search_result,
            parent,
            false
        )

        return SearchWordsViewHolder(view)
    }



    override fun onBindViewHolder(holder: SearchWordsViewHolder, position: Int, model: Word) {
        holder.setDetails(model)

        holder.itemView.setOnClickListener {
            navItemBackStack.push("")

            val bundle = Bundle()
            val wordDefinitionFragment = WordDefinitionFragment()
//            model.id = getRef(position).key.toString()
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


class SearchWordsViewHolder(itemVIew: View) :
    RecyclerView.ViewHolder(
        itemVIew
    ) {

    fun setDetails(word: Word) {
        val searchWord = itemView.findViewById<TextView>(R.id.tvRecentSearchWord)
        val searchDefinition = itemView.findViewById<TextView>(R.id.tvRecentSearchDefinition)

        searchWord.text = word.word
        val def = word.definition
        searchDefinition.text = if (def.length > 80) def.substring(0, 80) else def
    }
}