package com.jamie.lgbtqdictionary.models.words

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookmarkedWordDao {

    @Query("SELECT * FROM word_table")
    fun getAllWordsAscTime(): LiveData<List<BookmarkedWord>>

    @Query("SELECT * FROM word_table WHERE word = :word")
    fun getWordByName(word: String): LiveData<BookmarkedWord>

    @Insert
    fun insert(word: BookmarkedWord)

    //    @Delete
//    fun delete(word: BookmarkedWord)

    @Query("DELETE FROM word_table WHERE word = :word")
    fun delete(word: String)

    @Query("DELETE FROM word_table")
    fun deleteAllWords()

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAllWordsAscAlphabet(): LiveData<List<BookmarkedWord>>

    @Query("SELECT * FROM word_table ORDER BY word DESC")
    fun getAllWordsDescAlphabet(): LiveData<List<BookmarkedWord>>

    @Query("SELECT * FROM word_table ORDER BY timeAdded DESC")
    fun getAllWordsDescTime(): LiveData<List<BookmarkedWord>>

}