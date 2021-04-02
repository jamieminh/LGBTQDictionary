package com.jamie.lgbtqdictionary.models.words

import java.io.Serializable

data class Word(
    val word: String,
    var id: String,
    val pronunciation: String,
    val definition: String,
    val extent: String,
    val categories: Any,
    val offensive: String,
    val source: String,
    val flag: String
) : Serializable {

    constructor() : this("", "", "", "", "", "", "", "", "")

    override fun toString(): String {
        return "$word - $pronunciation"
    }
}