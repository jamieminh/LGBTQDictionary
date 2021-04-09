package com.jamie.lgbtqdictionary

import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jamie.lgbtqdictionary.models.words.Word
import java.util.*

class GlobalProperties : Application() {


    var navStack = Stack<String>()
    var randomWords = mutableListOf<Word>()
    lateinit var tts: TextToSpeech
    var ttsSpeed = 0.5F
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        val sharedPrefs = getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE)
        ttsSpeed = sharedPrefs.getFloat("soundSpeed", 0.5F)

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

    }

    fun ttsSpeak(word: String) {
        val a: Set<String> = HashSet()
        a.plus("female")

        val voice = Voice(
            "en-gb-x-rjs#female_1-local", Locale("en", "GB"),
            Voice.QUALITY_VERY_HIGH, Voice.LATENCY_NORMAL,
            true, a
        )


        tts.voice = voice
        tts.setSpeechRate(ttsSpeed)
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null)
    }






}