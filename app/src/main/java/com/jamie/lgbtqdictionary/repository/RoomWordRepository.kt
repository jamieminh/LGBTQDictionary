package com.jamie.lgbtqdictionary.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.jamie.lgbtqdictionary.models.words.RoomWord
import com.jamie.lgbtqdictionary.models.words.RoomWordDao
import com.jamie.lgbtqdictionary.models.words.RoomWordDatabase


class RoomWordRepository(application: Application?) {
    private var wordDao: RoomWordDao
    private var allWords: LiveData<List<RoomWord>>

    init {
        val database = RoomWordDatabase.getInstance(application!!.applicationContext)
        wordDao = database!!.noteDao()
        allWords = wordDao.getAll()
    }


    // Live data are auto handled by room, but not other data
    fun getAllWords(): LiveData<List<RoomWord>> {
        return allWords
    }

    fun getAllWordsAsc(): LiveData<List<RoomWord>> {
        return wordDao.getAllWordsAsc()
    }

    fun getAllWordsDesc(): LiveData<List<RoomWord>> {
        return wordDao.getAllWordsDesc()
    }

    fun getOne(wordId: String): RoomWord {
        return wordDao.getWordById(wordId)
    }

    fun insert(word: RoomWord) {
        InsertWordAsyncTask(wordDao).execute(word)
    }

    fun delete(word: RoomWord) {
        DeleteWordAsyncTask(wordDao).execute(word)
    }

    fun deleteById(wordId: String) {

    }

    fun deleteAll() {
        DeleteAllWordsAsyncTask(wordDao).execute()
    }

    private class InsertWordAsyncTask(private val wordDao: RoomWordDao) :
        AsyncTask<RoomWord, Void, Void>() {
        override fun doInBackground(vararg params: RoomWord): Void? {
            wordDao.insert(params[0])
            return null
        }
    }

    private class DeleteWordAsyncTask(private val wordDao: RoomWordDao) :
        AsyncTask<RoomWord, Void, Void>() {
        override fun doInBackground(vararg params: RoomWord): Void? {
            wordDao.delete(params[0])
            return null
        }
    }

    private class DeleteAllWordsAsyncTask(private val wordDao: RoomWordDao) :
        AsyncTask<RoomWord, Void, Void>() {
        override fun doInBackground(vararg params: RoomWord): Void? {
            wordDao.deleteAllWords()
            return null
        }
    }

}



// source code: https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-4-repository