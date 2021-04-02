package com.jamie.lgbtqdictionary.views

import android.annotation.SuppressLint
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.jamie.lgbtqdictionary.MainActivity
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.databinding.ActivityMainBinding
import com.jamie.lgbtqdictionary.models.words.RoomWord
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
    private lateinit var tts: TextToSpeech

    private val addBookmarkDrawable = R.drawable.ic_word_definition_add_bookmark_24
    private val removeBookmarkDrawable = R.drawable.ic_word_definition_remove_bookmark_24

    private lateinit var roomWordViewModel: RoomWordViewModel
    private lateinit var roomWord: RoomWord

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

        // initialize TextToSpeech engine
        tts = TextToSpeech(this.context) {
            if (it == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.ENGLISH)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported")
                }
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }

        // if there's no flag attribute, display the label and the flag
        if (word.flag != "") {
            flagLabel = view.findViewById(R.id.tvFlagLabel)
            wordFlag = view.findViewById(R.id.ivSingleWordFlag)
            flagLabel.visibility = TextView.VISIBLE
            Glide.with(wordFlag.context).load(word.flag).into(wordFlag)
        }

        val factory = RoomWordViewModelFactory(activity!!.application)
        roomWordViewModel = ViewModelProvider(this, factory).get(RoomWordViewModel::class.java)


        roomWordViewModel.getOne(word.id).observe(this, {
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
        roomWord = RoomWord(
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
            roomWordViewModel.insert(roomWord)

            // show a badge on the bookmark nav icon
            bindingMain.bottomNavBar.showBadge(R.id.nav_bookmarks)

            // switch to 'remove' icon
            toggleBookmark.setImageResource(removeBookmarkDrawable)
            toggleBookmark.tag = removeBookmarkDrawable
        } else {
            Toast.makeText(this.context, "'${word.word}' has been removed from your bookmarks", Toast.LENGTH_LONG)
                .show()
            // REMOVE WORD FROM LOCAL STORAGE
            roomWordViewModel.delete(roomWord)

            // switch to 'add' icon
            toggleBookmark.setImageResource(addBookmarkDrawable)
            toggleBookmark.tag = addBookmarkDrawable
        }
    }

    // function to convert word text to speech for user
    private fun onTapHearPronunciation() {
        val a: Set<String> = HashSet()
        a.plus("female")

        val voice = Voice(
            "en-gb-x-rjs#female_1-local", Locale("en", "GB"),
            Voice.QUALITY_VERY_HIGH, Voice.LATENCY_NORMAL,
            true, a
        )
        tts.voice = voice
        tts.setSpeechRate(0.4F)
        tts.speak(wordWord.text.toString(), TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }
}