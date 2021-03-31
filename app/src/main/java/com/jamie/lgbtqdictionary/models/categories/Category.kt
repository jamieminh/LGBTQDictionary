package com.jamie.lgbtqdictionary.models.categories

data class Category(val title: String, val cover_img: String, val id: String) {

    constructor() : this("","", "")
}