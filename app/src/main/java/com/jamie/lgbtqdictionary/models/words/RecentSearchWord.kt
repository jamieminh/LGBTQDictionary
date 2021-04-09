package com.jamie.lgbtqdictionary.models.words

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_searches")
data class RecentSearchWord(
    val word: String,
    @PrimaryKey val searchedAt: Int
) {
    override fun toString(): String {
        return "$word - $searchedAt"
    }
}