package com.jamie.lgbtqdictionary.viewmodels.words

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jamie.lgbtqdictionary.models.words.BookmarkedWord
import com.jamie.lgbtqdictionary.models.words.RecentSearchWord
import com.jamie.lgbtqdictionary.repository.RoomWordRepository

class RoomWordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RoomWordRepository(application)

    fun getRecentSearches(): LiveData<List<RecentSearchWord>> {
        return repository.getRecentSearches()
    }

    fun deleteOldSearches() {
        return repository.deleteOldSearches()
    }

    fun insertOneRecentSearch(word: RecentSearchWord) {
        repository.insertRecentSearch(word)
    }

    fun getAllBookmarks(): LiveData<List<BookmarkedWord>> {
        return repository.getAllWordsAscTime()
    }

    fun getAllBookmarksAsc(): LiveData<List<BookmarkedWord>> {
        return repository.getAllWordsAscAlphabet()
    }

    fun getAllBookmarksDesc(): LiveData<List<BookmarkedWord>> {
        return repository.getAllWordsDescAlphabet()
    }

    fun getOneBookmark(wordId: String): LiveData<BookmarkedWord> {
        return repository.getOne(wordId)
    }

    fun insertBookmark(word: BookmarkedWord) {
        repository.insert(word)
    }

    fun deleteBookmark(word: String) {
        repository.delete(word)
    }

    fun deleteAllBookmarks() {
        repository.deleteAll()
    }
}