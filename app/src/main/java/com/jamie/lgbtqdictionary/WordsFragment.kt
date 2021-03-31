package com.jamie.lgbtqdictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.jamie.lgbtqdictionary.models.generalword.Word

class WordsFragment : Fragment(R.layout.fragment_words) {

    lateinit var wordsRV : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_words, container, false)
        wordsRV = view.findViewById(R.id.rvCategoryWords)
        wordsRV.setHasFixedSize(true)
        wordsRV.layoutManager = LinearLayoutManager(this.context)

        val categoryId = arguments!!.get("id")

        showCategoryWords(categoryId.toString())

        return view

    }

    private fun showCategoryWords(categoryId: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("words").orderByChild("category").equalTo(categoryId)

        val options = FirebaseRecyclerOptions.Builder<Word>()
            .setQuery(dbRef, Word::class.java)
            .build()

        val adapter = object : FirebaseRecyclerAdapter<Word, CategoryWordsViewHolder>(options) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): CategoryWordsViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.general_words_list_layout,
                    parent,
                    false
                )

                return CategoryWordsViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: CategoryWordsViewHolder,
                position: Int,
                model: Word
            ) {
                holder.setDetails(model.word)
            }

        }

        wordsRV.adapter = adapter
        adapter.startListening()
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
}