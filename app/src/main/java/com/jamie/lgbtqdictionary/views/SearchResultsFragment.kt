package com.jamie.lgbtqdictionary.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.adapters.SearchWordAdapter
import com.jamie.lgbtqdictionary.models.words.Word


class SearchResultsFragment : Fragment(R.layout.fragment_search_results) {

    private lateinit var searchLoader: ProgressBar
    private lateinit var noResultCard: CardView
    private lateinit var noResultTitle: TextView
    private lateinit var noResultDesc: TextView
    private lateinit var globalProps: GlobalProperties
    private lateinit var searchResultsRV: RecyclerView
    private lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FragmentActivity) {
            mActivity = context
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search_results, container, false)
        searchLoader = view.findViewById(R.id.searchProgressBar)
        noResultCard = view.findViewById(R.id.cvNoResult)
        noResultTitle = view.findViewById(R.id.tvNoResultTitle)
        noResultDesc = view.findViewById(R.id.tvNoResultDesc)

        searchResultsRV = view.findViewById(R.id.rvSearchResults)
        searchResultsRV.setHasFixedSize(true)
        searchResultsRV.layoutManager = LinearLayoutManager(this.context)

        globalProps = this.context?.applicationContext as GlobalProperties
        val query = arguments!!.getString("query")!!.trim()

        // deselect nav bar item
        mActivity.findViewById<ChipNavigationBar>(R.id.bottom_nav_bar).setItemSelected(-1)

        // make sure the back button is visible instead of the logo in case search was made in Home
        mActivity.findViewById<ImageView>(R.id.HomeAppText).visibility = ImageView.GONE
        mActivity.findViewById<ImageView>(R.id.ivBackBtn).visibility = ImageView.VISIBLE

        // check for internet connection
        if (!globalProps.isInternetConnected) {
            noResultTitle.text = "Connection Required"
            noResultDesc.text = "You need Internet connection to look for words."
            noResultCard.visibility = CardView.VISIBLE
        }

        getResults(query)

        return view
    }


    private fun getResults(query: String) {
        val searchDbRef = FirebaseDatabase.getInstance().getReference("words")
            .orderByChild("word").startAt(query).endAt(query + "\uf8ff")

        searchDbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {

                // if the there is result
                if (snapshot.exists()) {
                    // remove spinner and 'no result' card view
                    noResultCard.visibility = CardView.GONE
                    searchLoader.visibility = ProgressBar.GONE

                    val options = FirebaseRecyclerOptions.Builder<Word>()
                        .setQuery(searchDbRef, Word::class.java)
                        .build()

                    val adapter = SearchWordAdapter(options, globalProps.navStack, mActivity.supportFragmentManager)
                    searchResultsRV.adapter = adapter
                    adapter.startListening()
                }
                // no result for the query
                else {
                    searchLoader.visibility = ProgressBar.GONE

                    noResultTitle.text = "No Result Found"
                    noResultDesc.text = "There is no word that starts with your entered phrase."
                    noResultCard.visibility = CardView.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })


    }


}