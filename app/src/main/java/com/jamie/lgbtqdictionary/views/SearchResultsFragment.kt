package com.jamie.lgbtqdictionary.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.adapters.SearchWordAdapter
import com.jamie.lgbtqdictionary.models.words.Word


class SearchResultsFragment : Fragment(R.layout.fragment_search_results) {

    private lateinit var searchLoader: ProgressBar
    private lateinit var globalProps : GlobalProperties
    private lateinit var searchResultsRV: RecyclerView
    private lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FragmentActivity) {
            mActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search_results, container, false)
        searchLoader = view.findViewById(R.id.searchProgressBar)
        searchResultsRV = view.findViewById(R.id.rvSearchResults)
        searchResultsRV.setHasFixedSize(true)
        searchResultsRV.layoutManager = LinearLayoutManager(this.context)

        globalProps = this.context?.applicationContext as GlobalProperties
        val query = arguments!!.getString("query")!!.trim()

        getResults(query)

        return view
    }

    private fun getResults(query: String) {
        val searchDbRef = FirebaseDatabase.getInstance().getReference("words")
            .orderByChild("word").startAt(query).endAt(query + "\uf8ff")

        val options = FirebaseRecyclerOptions.Builder<Word>()
            .setQuery(searchDbRef, Word::class.java)
            .build()

        val adapter = SearchWordAdapter(searchLoader, options, globalProps.navStack, mActivity.supportFragmentManager)
        searchResultsRV.adapter = adapter
        adapter.startListening()
    }


}