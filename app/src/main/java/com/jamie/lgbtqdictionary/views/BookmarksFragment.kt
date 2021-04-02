package com.jamie.lgbtqdictionary.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
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
    private lateinit var globalProps : GlobalProperties
    lateinit var bookmarksRv : RecyclerView

    private lateinit var sortBtn: ImageView
    private lateinit var deleteAllBtn: ImageView
    private lateinit var adapter: BookmarksAdapter

    // although when launched, the words are not ordered by alphabet, but buy inserted time,
    // this var is set to desc so that if user tap sort, words will be sorted in ascending order
    private var currentSortOrder = "desc"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_bookmarks, container, false)

        globalProps = this.context?.applicationContext as GlobalProperties
        bookmarksRv = view.findViewById(R.id.rvBookmarks)
        sortBtn = view.findViewById(R.id.ivBookmarksSort)
        deleteAllBtn = view.findViewById(R.id.ivBookmarksDeleteAll)

        adapter = BookmarksAdapter(globalProps.navStack, activity!!.supportFragmentManager)
        bookmarksRv.adapter = adapter
        bookmarksRv.setHasFixedSize(true)
        bookmarksRv.layoutManager = LinearLayoutManager(this.context)

        val factory = RoomWordViewModelFactory(activity!!.application)
        roomWordViewModel = ViewModelProvider(this, factory).get(RoomWordViewModel::class.java)
        roomWordViewModel.getAllWords().observe(this, { words ->
            words.forEach{ Log.i("Room Words", it.word)}
            adapter.setChangedWords(words)
        })

        sortBtn.setOnClickListener { onSortHandler() }
        deleteAllBtn.setOnClickListener { deleteAllHandler() }

        return view
    }

    private fun deleteAllHandler() {
        Toast.makeText(this.context, "DELETING ALL...", Toast.LENGTH_LONG).show()
        roomWordViewModel.deleteAll()
    }

    private fun onSortHandler() {
        if (currentSortOrder == "asc") {
            currentSortOrder = "desc"
            Toast.makeText(this.context, "SORTING IN DESCENDING ORDER", Toast.LENGTH_LONG).show()
            roomWordViewModel.getAllWordsDesc().observe(this, { words ->
                words.forEach{ Log.i("Room Words", it.word)}
                adapter.setChangedWords(words)
            })
        }
        else {
            currentSortOrder = "asc"
            Toast.makeText(this.context, "SORTING IN ASCENDING ORDER", Toast.LENGTH_LONG).show()
            roomWordViewModel.getAllWordsAsc().observe(this, { words ->
                words.forEach{ Log.i("Room Words", it.word)}
                adapter.setChangedWords(words)
            })
        }
    }


}