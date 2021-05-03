package com.jamie.lgbtqdictionary.models.words

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "word_table")
data class BookmarkedWord(
    @PrimaryKey val word: String,
    val pronunciation: String,
    val definition: String,
    val extent: String,
    val offensive: String,
    val source: String,
    val flag: String,
    val timeAdded: Date
) {
    override fun toString(): String {
        return "$word - $pronunciation - $definition - $extent - $offensive - $source - $flag - $timeAdded"
    }
}