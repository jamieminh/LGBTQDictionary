package com.jamie.lgbtqdictionary.views

import android.animation.ArgbEvaluator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.*
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.adapters.RandomWordsAdapter
import com.jamie.lgbtqdictionary.adapters.RecentSearchesAdapter
import com.jamie.lgbtqdictionary.models.words.Word
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModel
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModelFactory
import kotlin.random.Random


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var cardsPager: ViewPager
    private lateinit var recentSearchesRV: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: RandomWordsAdapter
    private lateinit var wordsDayDbRef: DatabaseReference
    private lateinit var globalProps : GlobalProperties
    private lateinit var colors: List<Int>
    private lateinit var mActivity: FragmentActivity
    var argbEvaluator = ArgbEvaluator()

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        globalProps = this.context?.applicationContext as GlobalProperties
        progressBar = view.findViewById(R.id.wordsDayProgressBar)
        recentSearchesRV = view.findViewById(R.id.rvRecentSearches)

        // fix the bug where on board screen not removed for the first time
        if (globalProps.isOnBoardInit()) {
            mActivity.supportFragmentManager.beginTransaction().apply {
                remove(globalProps.onBoardFragment)
                commit()
            }
        }

        // get data from firebase
        wordsDayDbRef = FirebaseDatabase.getInstance().getReference("words")

        cardsPager = view.findViewById(R.id.vpWordsOfDay)
        cardsPager.setPadding(40, 0, 40, 0)
        colors = listOf(
            ContextCompat.getColor(context!!, R.color.card01),
            ContextCompat.getColor(context!!, R.color.card02),
            ContextCompat.getColor(context!!, R.color.card03),
            ContextCompat.getColor(context!!, R.color.card04)
        )
        loadRandomWords()
        loadRecentSearches()

        return view
    }

    private fun loadRecentSearches() {
        val factory = RoomWordViewModelFactory(mActivity.application)
        val roomWordViewModel = ViewModelProvider(this, factory).get(RoomWordViewModel::class.java)

        val adapter = RecentSearchesAdapter(
//            roomWordViewModel,
            globalProps.navStack,
            mActivity.supportFragmentManager
        )
        recentSearchesRV.adapter = adapter
        recentSearchesRV.setHasFixedSize(true)
        recentSearchesRV.layoutManager = LinearLayoutManager(this.context)

        // remove old searches from db, only leave 10 most recent ones
        roomWordViewModel.deleteOldSearches()

        roomWordViewModel.getRecentSearches().observe(this, { words ->
            words.forEach { Log.i("Room Words", it.word) }
            adapter.setChangedWords(words)
        })
    }


    private fun loadRandomWords() {
        // if the app doesn't already generated random words
        // random words are only generated when user open the app and not every time Home is opened
        if (globalProps.randomWords.size == 0) {
            wordsDayDbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in 1..4) {
                        if (globalProps.randomWords.size == 4) {
                            break
                        }
                        val rand = Random.nextInt(0, snapshot.childrenCount.toInt())
                        globalProps.randomWords.add(
                            snapshot.children.elementAt(rand).getValue(Word::class.java)!!
                        )
                    }
                    globalProps.randomWords.forEach { Log.i("Random.Word", it.toString()) }
                    callAdapter(globalProps.randomWords)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
        else {
            callAdapter(globalProps.randomWords)
        }
    }


    private fun callAdapter(words: List<Word>) {
//        Log.i("NUll.Context", (globalProps.context == null).toString())
//        Log.i("NUll.words", (words.size).toString())
//        Log.i("NUll.navStack", (globalProps.navStack.size).toString())
//        Log.i("NUll.activity", (activity == null).toString())
//        Log.i("NUll.mActivity", (mActivity == null).toString())
        adapter = RandomWordsAdapter(
            words,
            globalProps.context,
            globalProps.navStack,
            mActivity.supportFragmentManager
        )
        cardsPager.adapter = adapter

        cardsPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position < (adapter.count - 1) && position < (colors.size - 1)) {
                    cardsPager.setBackgroundColor(
                        argbEvaluator.evaluate(
                            positionOffset,
                            colors[position],
                            colors[position + 1]
                        ) as Int
                    )
                } else {
                    cardsPager.setBackgroundColor(colors[colors.size - 1])
                }

            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        progressBar.visibility = ProgressBar.GONE
    }



}