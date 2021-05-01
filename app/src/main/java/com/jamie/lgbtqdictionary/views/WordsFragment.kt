package com.jamie.lgbtqdictionary.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.adapters.WordsAdapter
import com.jamie.lgbtqdictionary.models.words.Word

class WordsFragment : Fragment(R.layout.fragment_words) {

    private lateinit var wordsRV : RecyclerView
    private lateinit var label: TextView
    private lateinit var loader : ProgressBar
    private lateinit var globalProps : GlobalProperties
    private lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FragmentActivity) {
            mActivity = context
        }
    }

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

        // deselect nav bar item
        mActivity.findViewById<ChipNavigationBar>(R.id.bottom_nav_bar).setItemSelected(-1)

        label = view.findViewById(R.id.tvWordsFragmentLabel)
        label.text = arguments!!.get("title").toString()
        val categoryId = arguments!!.get("id")

        loader = view.findViewById(R.id.wordsProgressBar)
        globalProps = this.context?.applicationContext as GlobalProperties

        showCategoryWords(categoryId.toString())

        return view

    }

    private fun showCategoryWords(categoryId: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("words")
            .orderByChild("categories/$categoryId").equalTo("true")

        val options = FirebaseRecyclerOptions.Builder<Word>()
            .setQuery(dbRef, Word::class.java)
            .build()

        val adapter = WordsAdapter(loader, options, globalProps.navStack, mActivity.supportFragmentManager)
        wordsRV.adapter = adapter
        adapter.startListening()
    }

}