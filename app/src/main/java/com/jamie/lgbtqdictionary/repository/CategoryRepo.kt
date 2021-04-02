package com.jamie.lgbtqdictionary.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jamie.lgbtqdictionary.models.categories.Category


class CategoryRepo constructor(private val catModels: MutableList<Category>){
    companion object {
        @Volatile
        private var instance: CategoryRepo? = null

        fun getInstance(catModels: MutableList<Category>) = instance ?: synchronized(this) {
            instance ?: CategoryRepo(catModels).also { instance = it }
        }
    }

    private fun getCategories(): MutableLiveData<MutableList<Category>> {
        loadNames()

        val cats = MutableLiveData<MutableList<Category>>()
        cats.value = catModels

        return cats
    }

    private fun loadNames() {
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("categories")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{snap ->
                    snap.getValue(Category::class.java)?.let { catModels.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}