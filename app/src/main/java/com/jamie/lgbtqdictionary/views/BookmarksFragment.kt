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
    private lateinit var aphabetSortBtn: ImageView
    private lateinit var timeSortBtn: ImageView
    private lateinit var deleteAllBtn: ImageView
    private lateinit var adapter: BookmarksAdapter
    private lateinit var mActivity: FragmentActivity

    // although when launched, the words are not ordered by alphabet, but buy inserted time,
    // this var is set to desc so that if user tap sort, words will be sorted in ascending order
    private var currentSortOrder = "desc"

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
        aphabetSortBtn = view.findViewById(R.id.ivBookmarksAlphabetSort)
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


        roomWordViewModel.getAllBookmarks().observe(this, { words ->
            words.forEach { Log.i("Room Words", it.word) }
            adapter.setChangedWords(words)
        })

        aphabetSortBtn.setImageResource(R.drawable.ic_sort_asc)
        aphabetSortBtn.tag = R.drawable.ic_sort_asc
        aphabetSortBtn.setOnClickListener { onSortHandler() }
        timeSortBtn.setOnClickListener { Toast.makeText(this.context, "Time sort", Toast.LENGTH_SHORT).show() }
        deleteAllBtn.setOnClickListener { deleteAllHandler() }

        return view
    }

    private fun deleteAllHandler() {
        Toast.makeText(this.context, "DELETING ALL...", Toast.LENGTH_LONG).show()
        roomWordViewModel.deleteAllBookmarks()
    }

    private fun onSortHandler() {
        if (currentSortOrder == "asc") {
            currentSortOrder = "desc"
            aphabetSortBtn.setImageResource(R.drawable.ic_sort_asc)
            aphabetSortBtn.tag = R.drawable.ic_sort_asc

            roomWordViewModel.getAllBookmarksDesc().observe(this, { words ->
                words.forEach { Log.i("Room Words", it.word) }
                adapter.setChangedWords(words)
            })
        } else {
            currentSortOrder = "asc"
            aphabetSortBtn.setImageResource(R.drawable.ic_sort_desc)
            aphabetSortBtn.tag = R.drawable.ic_sort_desc

            roomWordViewModel.getAllBookmarksAsc().observe(this, { words ->
                words.forEach { Log.i("Room Words", it.word) }
                adapter.setChangedWords(words)
            })
        }
    }


}