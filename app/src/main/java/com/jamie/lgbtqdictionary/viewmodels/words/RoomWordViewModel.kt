package com.jamie.lgbtqdictionary.viewmodels.words

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jamie.lgbtqdictionary.models.words.RoomWord
import com.jamie.lgbtqdictionary.repository.RoomWordRepository

class RoomWordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RoomWordRepository(application)
    private var allWords = repository.getAllWords()


    fun getAllWords(): LiveData<List<RoomWord>> {
        return repository.getAllWords()
    }

    fun getAllWordsAsc(): LiveData<List<RoomWord>> {
        return repository.getAllWordsAsc()
    }

    fun getAllWordsDesc(): LiveData<List<RoomWord>> {
        return repository.getAllWordsDesc()
    }

    fun getOne(wordId: String): LiveData<RoomWord> {
        return repository.getOne(wordId)
    }

    fun insert(word: RoomWord) {
        repository.insert(word)
    }

    fun delete(word: RoomWord) {
        repository.delete(word)
    }

    fun deleteAll() {
        repository.deleteAll()
    }
}