package com.jamie.lgbtqdictionary.models.words

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecentSearchWordDao {

    @Query("SELECT * FROM recent_searches ORDER BY searchedAt DESC")
    fun getAllMostRecent(): LiveData<List<RecentSearchWord>>

    @Insert
    fun insert(word: RecentSearchWord)

    // the table only holds at most 10 recent searches, old record will be removed to add new one
    @Query("DELETE FROM recent_searches WHERE searchedAt NOT IN (SELECT searchedAt from recent_searches ORDER BY searchedAt DESC LIMIT 10)")
    fun deleteOldSearches()
}