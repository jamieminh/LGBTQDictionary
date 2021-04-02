package com.jamie.lgbtqdictionary.viewmodels.words

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RoomWordViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomWordViewModel::class.java)) {
            return RoomWordViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}