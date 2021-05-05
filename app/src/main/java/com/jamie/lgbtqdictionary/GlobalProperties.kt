package com.jamie.lgbtqdictionary

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.jamie.lgbtqdictionary.models.words.Word
import com.jamie.lgbtqdictionary.views.OnBoardFragment
import java.util.*

class GlobalProperties : Application() {


    var navStack = Stack<String>()
    var randomWords = mutableListOf<Word>()
    var ttsSpeed = 0.5F
    var isInternetConnected = true
    lateinit var context: Context

    lateinit var onBoardFragment: OnBoardFragment

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        val sharedPrefs = getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE)
        ttsSpeed = sharedPrefs.getFloat("soundSpeed", 0.5F)

        isInternetConnected = true


    }

    fun isOnBoardInit(): Boolean {
        return this::onBoardFragment.isInitialized
    }


}