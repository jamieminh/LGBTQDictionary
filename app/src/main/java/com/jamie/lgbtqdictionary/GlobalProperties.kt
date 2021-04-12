package com.jamie.lgbtqdictionary

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.jamie.lgbtqdictionary.models.words.Word
import java.util.*

class GlobalProperties : Application() {


    var navStack = Stack<String>()
    var randomWords = mutableListOf<Word>()
    var ttsSpeed = 0.5F
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        val sharedPrefs = getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE)
        ttsSpeed = sharedPrefs.getFloat("soundSpeed", 0.5F)


    }


}