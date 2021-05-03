package com.jamie.lgbtqdictionary.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
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
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.MainActivity
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.databinding.ActivityMainBinding
import com.jamie.lgbtqdictionary.models.words.BookmarkedWord
import com.jamie.lgbtqdictionary.models.words.Word
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModel
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModelFactory
import java.util.*
import kotlin.math.floor


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

    private lateinit var tts: TextToSpeech
    private var isTTSInit = false


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
        (activity as MainActivity).showAppLogo(false)

        // deselect nav bar item
        mActivity.findViewById<ChipNavigationBar>(R.id.bottom_nav_bar).setItemSelected(-1)

        wordWord = view.findViewById(R.id.tvSingleWordWord)
        wordSpelling = view.findViewById(R.id.tvSingleWordSpelling)
        wordDefinition = view.findViewById(R.id.tvSingleWordDefinition)
        wordOffensive = view.findViewById(R.id.tvSingleWordOffensive)
        toggleBookmark = view.findViewById(R.id.ivToggleBookmark)
        hearPronunciation = view.findViewById(R.id.ivHearSpelling)

        // setup values on the fragment
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


        // if there's a 'flag' image attribute, display the label and the flag
        if (word.flag != "") {
            flagLabel = view.findViewById(R.id.tvFlagLabel)
            wordFlag = view.findViewById(R.id.ivSingleWordFlag)
            flagLabel.visibility = TextView.VISIBLE
            Glide.with(wordFlag.context).load(word.flag).into(wordFlag)
        }

        val factory = RoomWordViewModelFactory(mActivity.application)
        roomWordViewModel = ViewModelProvider(this, factory).get(RoomWordViewModel::class.java)


        // check if the word is in local storage (bookmarked) and display the icon accordingly
        Log.i("Definition.Word", word.word)
        roomWordViewModel.getOneBookmark(word.word).observe(this, {
            Log.i("Definition.Word.Exist", (it==null).toString())
            if (it == null) {
                toggleBookmark.setImageResource(addBookmarkDrawable)
                toggleBookmark.tag = addBookmarkDrawable
            }
            else {
                toggleBookmark.setImageResource(removeBookmarkDrawable)
                toggleBookmark.tag = removeBookmarkDrawable
            }

        })

        // setup text-to-speech engine
        val a: Set<String> = HashSet()
        a.plus("female")

        val voice = Voice(
            "en-gb-x-rjs#female_1-local", Locale("en", "GB"),
            Voice.QUALITY_VERY_HIGH, Voice.LATENCY_NORMAL,
            true, a
        )

        tts = TextToSpeech(this.context) {
            if (it == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.UK)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported")
                }
                else {
                    isTTSInit = true
                }

            } else {
                Log.e("TTS", "Initialization failed")
            }
        }

        globalProps = mActivity.application.applicationContext as GlobalProperties
        tts.voice = voice
        tts.setSpeechRate(globalProps.ttsSpeed)


        toggleBookmark.setOnClickListener { onTapBookmarkToggle(word) }
        hearPronunciation.setOnClickListener { onTapHearPronunciation() }

        return view
    }

    // function to adding the word to user bookmarks, which is in local storage
    private fun onTapBookmarkToggle(word: Word) {

        // if current icon is 'add', then add this word to the local storage
        if (toggleBookmark.tag == addBookmarkDrawable) {
            bookmarkedWord = BookmarkedWord(
                word.word,
                word.pronunciation,
                word.definition,
                word.extent,
                word.offensive,
                word.source,
                word.flag,
                floor(System.currentTimeMillis().toDouble() / 1000).toInt()
            )

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
            roomWordViewModel.deleteBookmark(word.word)

            // switch to 'add' icon
            toggleBookmark.setImageResource(addBookmarkDrawable)
            toggleBookmark.tag = addBookmarkDrawable
        }
    }

    // function to convert word text to speech for user
    private fun onTapHearPronunciation() {

        if (!isTTSInit) {
            return
        }

        tts.speak(wordWord.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)



    }

}