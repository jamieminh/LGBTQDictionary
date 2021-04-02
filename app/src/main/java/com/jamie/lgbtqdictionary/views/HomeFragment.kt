package com.jamie.lgbtqdictionary.views

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.*
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.adapters.RandomWordsAdapter
import com.jamie.lgbtqdictionary.models.words.Word
import kotlin.random.Random


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var cardsPager: ViewPager
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: RandomWordsAdapter
    private lateinit var wordsDayDbRef: DatabaseReference
    private lateinit var globalProps : GlobalProperties
    private lateinit var colors: List<Int>
    var argbEvaluator = ArgbEvaluator()
    private var words = mutableListOf<Word>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        globalProps = this.context?.applicationContext as GlobalProperties
        progressBar = view.findViewById(R.id.wordsDayProgressBar)

        // get data from firebase
        wordsDayDbRef = FirebaseDatabase.getInstance().getReference("words")

        loadWords()
        colors = listOf(
            ContextCompat.getColor(context!!, R.color.card01),
            ContextCompat.getColor(context!!, R.color.card02),
            ContextCompat.getColor(context!!, R.color.card03),
            ContextCompat.getColor(context!!, R.color.card04)
        )

        cardsPager = view.findViewById(R.id.vpWordsOfDay)
        cardsPager.setPadding(40, 0, 40, 0)

        return view
    }


    private fun loadWords() {
        wordsDayDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in 1..4) {
                    val rand = Random.nextInt(0, snapshot.childrenCount.toInt())
                    words.add(snapshot.children.elementAt(rand).getValue(Word::class.java)!!)
                }

                adapter = RandomWordsAdapter(words, context!!, globalProps.navStack, activity!!.supportFragmentManager)
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

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}