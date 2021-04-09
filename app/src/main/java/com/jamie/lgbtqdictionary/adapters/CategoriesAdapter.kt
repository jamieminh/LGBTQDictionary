package com.jamie.lgbtqdictionary.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.models.categories.Category
import com.jamie.lgbtqdictionary.views.WordsFragment
import java.util.*

val dbRef = FirebaseDatabase.getInstance().getReference("categories")
val options = FirebaseRecyclerOptions.Builder<Category>()
    .setQuery(dbRef, Category::class.java)
    .build()

class CategoriesAdapter(
    private val loader : ProgressBar,
    var navItemBackStack: Stack<String>,
    private val supportFragmentManager: FragmentManager
) : FirebaseRecyclerAdapter<Category, CategoriesViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_layout_categories,
            parent,
            false
        )

        return CategoriesViewHolder(view)
    }



    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int, model: Category) {
        holder.setDetails(model.title, model.cover_img)

        // remove progress bar when the data is available
        loader.visibility = ConstraintLayout.GONE

        holder.itemView.setOnClickListener {
            val id = getRef(position).key.toString()        // key of item in firebase
            navItemBackStack.push("CATEGORIES")
            navItemBackStack.toArray().forEach { Log.i("CATEGORIES.STACK", it.toString()) }

            val bundle = Bundle()
            bundle.putString("id", model.id)
            bundle.putString("title", model.title)

            val wordFragment = WordsFragment()
            wordFragment.arguments = bundle

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragmentContainer, wordFragment)
                addToBackStack(null)
                commit()
            }
        }
    }


}


class CategoriesViewHolder(itemVIew: View) :
    RecyclerView.ViewHolder(
        itemVIew
    ) {

    fun setDetails(title: String, cover: String) {
        val catTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val catCover = itemView.findViewById<ImageView>(R.id.ivCover)

        catTitle.text = title
        Glide.with(catCover.context).load(cover).into(catCover)

    }
}