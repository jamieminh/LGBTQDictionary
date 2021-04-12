package com.jamie.lgbtqdictionary.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.jamie.lgbtqdictionary.models.words.*


class RoomWordRepository(application: Application?) {
    private var bookmarkedWordDao: BookmarkedWordDao
    private var recentSearchesDao: RecentSearchWordDao
    private var allWords: LiveData<List<BookmarkedWord>>

    init {
        val database = RoomWordDatabase.getInstance(application!!.applicationContext)
        bookmarkedWordDao = database!!.bookmarkedWordDao()
        recentSearchesDao = database.recentSearchesDao()
        allWords = bookmarkedWordDao.getAll()
    }

    fun getRecentSearches(): LiveData<List<RecentSearchWord>> {
        return recentSearchesDao.getAllMostRecent()
    }

    fun insertRecentSearch(word: RecentSearchWord) {
        InsertRecentSearchAsyncTask(recentSearchesDao).execute(word)
    }

    fun deleteOldSearches() {
        DeleteNotRecentSearches(recentSearchesDao).execute()
    }


    // Live data are auto handled by room, but not other data
    fun getAllWords(): LiveData<List<BookmarkedWord>> {
        return allWords
    }

    fun getAllWordsAsc(): LiveData<List<BookmarkedWord>> {
        return bookmarkedWordDao.getAllWordsAsc()
    }

    fun getAllWordsDesc(): LiveData<List<BookmarkedWord>> {
        return bookmarkedWordDao.getAllWordsDesc()
    }

    fun getOne(word: String): LiveData<BookmarkedWord> {
        return bookmarkedWordDao.getWordByName(word)
    }

    fun insert(word: BookmarkedWord) {
        InsertWordAsyncTask(bookmarkedWordDao).execute(word)
    }

    fun delete(word: BookmarkedWord) {
        DeleteWordAsyncTask(bookmarkedWordDao).execute(word)
    }

    fun deleteAll() {
        DeleteAllWordsAsyncTask(bookmarkedWordDao).execute()
    }

    private class InsertWordAsyncTask(private val wordDao: BookmarkedWordDao) :
        AsyncTask<BookmarkedWord, Void, Void>() {
        override fun doInBackground(vararg params: BookmarkedWord): Void? {
            wordDao.insert(params[0])
            return null
        }
    }

    private class InsertRecentSearchAsyncTask(private val wordDao: RecentSearchWordDao) :
        AsyncTask<RecentSearchWord, Void, Void>() {
        override fun doInBackground(vararg params: RecentSearchWord): Void? {
            wordDao.insert(params[0])
            return null
        }
    }


    private class DeleteWordAsyncTask(private val wordDao: BookmarkedWordDao) :
        AsyncTask<BookmarkedWord, Void, Void>() {
        override fun doInBackground(vararg params: BookmarkedWord): Void? {
            wordDao.delete(params[0])
            return null
        }
    }

    private class DeleteAllWordsAsyncTask(private val wordDao: BookmarkedWordDao) :
        AsyncTask<BookmarkedWord, Void, Void>() {
        override fun doInBackground(vararg params: BookmarkedWord): Void? {
            wordDao.deleteAllWords()
            return null
        }
    }

    private class DeleteNotRecentSearches(private val wordDao: RecentSearchWordDao) :
        AsyncTask<RecentSearchWord, Void, Void>() {
        override fun doInBackground(vararg params: RecentSearchWord): Void? {
            wordDao.deleteOldSearches()
            return null
        }
    }

}



// source code: https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-4-repository