package com.jamie.lgbtqdictionary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    lateinit var categoriesRV: RecyclerView
    lateinit var fragmentLabel : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        categoriesRV = view.findViewById(R.id.rvCategories)
        categoriesRV.setHasFixedSize(true)
        categoriesRV.layoutManager = LinearLayoutManager(this.context)
        showCategories()
        return view
    }


    private fun showCategories() {
        val dbRef = FirebaseDatabase.getInstance().getReference("categories")
        val storageRef = FirebaseStorage.getInstance().reference
        Log.i("DB.REF", dbRef.toString())

        val options = FirebaseRecyclerOptions.Builder<Categories>()
            .setQuery(dbRef, Categories::class.java)
            .build()
        Log.i("DB.OPTIONS", options.toString())


        val adapter = object : FirebaseRecyclerAdapter<Categories, CategoriesViewHolder>(options) {

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): CategoriesViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.categories_list_layout,
                    parent,
                    false
                )
                Log.i("onCreateViewHolder", view.toString())

                return CategoriesViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: CategoriesViewHolder,
                position: Int,
                model: Categories
            ) {
                Log.i("MODEL", model.title + '-' + model.cover_img)

                holder.setDetails(model.title, model.cover_img)

                holder.itemView.setOnClickListener {
                    val id = getRef(position).key.toString()
                    Log.i("POS.KEY", id)

                    val bundle = Bundle()
                    bundle.putString("id", id)
                    bundle.putString("title", model.title)

                    val wordFragment = GeneralWordFragment()
                    wordFragment.arguments = bundle
                    fragmentLabel = activity!!.findViewById(R.id.tvFragmentLabel)
                    fragmentLabel.text = model.title

                    val fragmentManager = activity!!.supportFragmentManager
                    fragmentManager.beginTransaction().apply {
                        replace(R.id.flFragmentContainer, wordFragment)
                        addToBackStack(null)
                        commit()
                    }
                }
            }

        }

        categoriesRV.adapter = adapter
        adapter.startListening()
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



}
