package com.jamie.lgbtqdictionary.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.adapters.BookmarksAdapter
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModel
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModelFactory


class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {

    private lateinit var roomWordViewModel: RoomWordViewModel
    private lateinit var globalProps: GlobalProperties
    private lateinit var bookmarksRv: RecyclerView
    private lateinit var alphabetSortBtn: ImageView
    private lateinit var timeSortBtn: ImageView
    private lateinit var deleteAllBtn: ImageView
    private lateinit var adapter: BookmarksAdapter
    private lateinit var mActivity: FragmentActivity

    // although when launched, the words are not ordered by alphabet, but by inserted time,
    // this var is set to desc so that if user tap sort, words will be sorted in ascending order
    private var currentAlphabetOrder =
        "desc"       // technically, it's undecided, but for visual purpose, set it to 'desc'
    private var currentTimeOrder =
        "asc"            // words taken from db are already sorted in 'asc' order

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
        val view = inflater.inflate(R.layout.fragment_bookmarks, container, false)

        globalProps = this.context?.applicationContext as GlobalProperties
        bookmarksRv = view.findViewById(R.id.rvBookmarks)
        alphabetSortBtn = view.findViewById(R.id.ivBookmarksAlphabetSort)
        timeSortBtn = view.findViewById(R.id.ivBookmarksTimeSort)
        deleteAllBtn = view.findViewById(R.id.ivBookmarksDeleteAll)

        val factory = RoomWordViewModelFactory(mActivity.application)
        roomWordViewModel = ViewModelProvider(this, factory).get(RoomWordViewModel::class.java)

        adapter = BookmarksAdapter(
            roomWordViewModel,
            globalProps.navStack,
            mActivity.supportFragmentManager,
            mActivity
        )
        bookmarksRv.adapter = adapter
        bookmarksRv.setHasFixedSize(true)
        bookmarksRv.layoutManager = LinearLayoutManager(this.context)


        roomWordViewModel.getAllBookmarksAscTime().observe(this, { words ->
            words.forEach { Log.i("Room Words", it.word) }
            adapter.setChangedWords(words)
        })

        // must be programmatically set first
//        alphabetSortBtn.setImageResource(R.drawable.ic_sort_asc)
//        alphabetSortBtn.tag = R.drawable.ic_sort_asc
        alphabetSortBtn.setOnClickListener { onAlphabetSortHandler() }
        timeSortBtn.setOnClickListener { onTimeSortHandler() }
        deleteAllBtn.setOnClickListener { deleteAllHandler() }

        return view
    }

    private fun deleteAllHandler() {
        Toast.makeText(this.context, "DELETING ALL...", Toast.LENGTH_LONG).show()
        roomWordViewModel.deleteAllBookmarks()
    }

    private fun onAlphabetSortHandler() {
        // set currentTimeOrder to 'desc' and the icon to undecided
        timeSortBtn.setImageResource(R.drawable.ic_sort_clockwise_not_decided)
        timeSortBtn.tag = R.drawable.ic_sort_clockwise_not_decided

        // sort in alphabetical order
        if (currentAlphabetOrder == "desc") {
            currentAlphabetOrder = "asc"
            alphabetSortBtn.setImageResource(R.drawable.ic_sort_asc)
            alphabetSortBtn.tag = R.drawable.ic_sort_asc

            roomWordViewModel.getAllBookmarksAscAlphabet().observe(this, { words ->
                adapter.setChangedWords(words)
            })

        } else {
            currentAlphabetOrder = "desc"
            alphabetSortBtn.setImageResource(R.drawable.ic_sort_desc)
            alphabetSortBtn.tag = R.drawable.ic_sort_desc

            roomWordViewModel.getAllBookmarksDescAlphabet().observe(this, { words ->
                adapter.setChangedWords(words)
            })
        }
    }

    private fun onTimeSortHandler() {
        // set currentTimeOrder to 'desc' and the icon to undecided
        alphabetSortBtn.setImageResource(R.drawable.ic_sort_not_decided)
        alphabetSortBtn.tag = R.drawable.ic_sort_not_decided

        // sort in time added order
        if (currentTimeOrder == "asc") {
            currentTimeOrder = "desc"
            timeSortBtn.setImageResource(R.drawable.ic_sort_clockwise_reverse)
            timeSortBtn.tag = R.drawable.ic_sort_clockwise_reverse

            roomWordViewModel.getAllBookmarksDescTime().observe(this, { words ->
                adapter.setChangedWords(words)
            })
        } else {
            currentTimeOrder = "asc"
            timeSortBtn.setImageResource(R.drawable.ic_sort_clockwise)
            timeSortBtn.tag = R.drawable.ic_sort_clockwise

            roomWordViewModel.getAllBookmarksAscTime().observe(this, { words ->
                adapter.setChangedWords(words)
            })
        }
    }


}