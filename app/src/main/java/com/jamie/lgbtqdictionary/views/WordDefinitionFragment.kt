package com.jamie.lgbtqdictionary.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.MainActivity
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.databinding.ActivityMainBinding
import com.jamie.lgbtqdictionary.models.words.BookmarkedWord
import com.jamie.lgbtqdictionary.models.words.Word
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModel
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModelFactory
import java.util.*


class WordDefinitionFragment : Fragment(R.layout.fragment_word_definition) {

    private lateinit var wordWord: TextView
    private lateinit var wordSpelling: TextView
    private lateinit var wordDefinition: TextView
    private lateinit var wordOffensive: TextView
    private lateinit var wordFlag: ImageView
    private lateinit var toggleBookmark: ImageView
    private lateinit var hearPronunciation: ImageView
    private lateinit var flagLabel: TextView
    private lateinit var bindingMain: ActivityMainBinding
    private lateinit var globalProps: GlobalProperties


    private val addBookmarkDrawable = R.drawable.ic_word_definition_add_bookmark_24
    private val removeBookmarkDrawable = R.drawable.ic_word_definition_remove_bookmark_24

    private lateinit var roomWordViewModel: RoomWordViewModel
    private lateinit var bookmarkedWord: BookmarkedWord

    private lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            mActivity = context
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_word_definition, container, false)
        bindingMain = (activity as MainActivity).binding
        val word = arguments!!.getSerializable("word") as Word
        val clHeader = mActivity.findViewById<ConstraintLayout>(R.id.clHeaderArea)
        // un-hide the header area when loading words in case user came from HOME
        clHeader.visibility = ConstraintLayout.VISIBLE

        wordWord = view.findViewById(R.id.tvSingleWordWord)
        wordSpelling = view.findViewById(R.id.tvSingleWordSpelling)
        wordDefinition = view.findViewById(R.id.tvSingleWordDefinition)
        wordOffensive = view.findViewById(R.id.tvSingleWordOffensive)
        toggleBookmark = view.findViewById(R.id.ivToggleBookmark)
        hearPronunciation = view.findViewById(R.id.ivHearSpelling)


        wordWord.text = word.word
        wordSpelling.text = word.pronunciation
        wordDefinition.text =
            "${word.definition.capitalize(Locale.ROOT)}\n${word.extent.capitalize(Locale.ROOT)}"
        wordOffensive.text = when (word.offensive) {
            "0" -> "This word is neutral."
            "0.5" -> "This word can be offensive depends circumstances and individuals."
            "1" -> "This word is very offensive."
            else -> "TBD"
        }

        globalProps = mActivity.application.applicationContext as GlobalProperties

        // if there's no flag attribute, display the label and the flag
        if (word.flag != "") {
            flagLabel = view.findViewById(R.id.tvFlagLabel)
            wordFlag = view.findViewById(R.id.ivSingleWordFlag)
            flagLabel.visibility = TextView.VISIBLE
            Glide.with(wordFlag.context).load(word.flag).into(wordFlag)
        }

        val factory = RoomWordViewModelFactory(mActivity.application)
        roomWordViewModel = ViewModelProvider(this, factory).get(RoomWordViewModel::class.java)


        roomWordViewModel.getOneBookmark(word.id).observe(this, {
            // true means the word is not found in db, thus this word is not bookmarked
            if (it == null) {
                toggleBookmark.setImageResource(addBookmarkDrawable)
                toggleBookmark.tag = addBookmarkDrawable
            }
            else {
                toggleBookmark.setImageResource(removeBookmarkDrawable)
                toggleBookmark.tag = removeBookmarkDrawable
            }

        })


        toggleBookmark.setOnClickListener { onTapBookmarkToggle(word) }
        hearPronunciation.setOnClickListener { onTapHearPronunciation() }

        return view
    }

    // function to adding the word to user bookmarks, which is in local storage
    private fun onTapBookmarkToggle(word: Word) {
        bookmarkedWord = BookmarkedWord(
            word.id,
            word.word,
            word.pronunciation,
            word.definition,
            word.extent,
            word.offensive,
            word.source,
            word.flag
        )

        // if current icon is 'add', then add this word to the local storage
        if (toggleBookmark.tag == addBookmarkDrawable) {
            Toast.makeText(this.context, "'${word.word}' has been added to your bookmarks", Toast.LENGTH_LONG).show()
            // ADD WORD TO LOCAL STORAGE
            roomWordViewModel.insertBookmark(bookmarkedWord)

            // show a badge on the bookmark nav icon
            bindingMain.bottomNavBar.showBadge(R.id.nav_bookmarks)

            // switch to 'remove' icon
            toggleBookmark.setImageResource(removeBookmarkDrawable)
            toggleBookmark.tag = removeBookmarkDrawable
        } else {
            Toast.makeText(this.context, "'${word.word}' has been removed from your bookmarks", Toast.LENGTH_LONG)
                .show()
            // REMOVE WORD FROM LOCAL STORAGE
            roomWordViewModel.deleteBookmark(bookmarkedWord)

            // switch to 'add' icon
            toggleBookmark.setImageResource(addBookmarkDrawable)
            toggleBookmark.tag = addBookmarkDrawable
        }
    }

    // function to convert word text to speech for user
    private fun onTapHearPronunciation() {
        globalProps.ttsSpeak(wordWord.text.toString())
    }

}