package com.jamie.lgbtqdictionary.viewmodels.categories

import androidx.lifecycle.ViewModelProvider
import com.jamie.lgbtqdictionary.models.categories.CategoryRepository

class CategoriesViewModelFactory(private val categoryRepository: CategoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return CategoriesViewModel(categoryRepository) as T
//    }
}