package com.jamie.lgbtqdictionary

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage

class CategoriesAdapter(options : FirebaseRecyclerOptions<Categories>) :
    FirebaseRecyclerAdapter<Categories, CategoriesViewHolder>(options) {

    private val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.categories_list_layout,
            parent,
            false
        )
        Log.i("onCreateViewHolder", view.toString())

        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int, model: Categories) {
        Log.i("MODEL", model.title + '-' + model.cover_img)
        val url =
            "https://images.theconversation.com/files/161211/original/image-20170316-10898-1jrtrw4.jpg?ixlib=rb-1.1.0&rect=0%2C977%2C5306%2C2573&q=45&auto=format&w=1356&h=668&fit=crop"

//        storageRef.child(model.cover_img).downloadUrl.addOnSuccessListener {
//            Log.i("COVER_URI", it.toString())
//            holder.setDetails(model.title, it.toString(), thisContext)
//        }.addOnFailureListener {
//            holder.setDetails(model.title, url, thisContext)
//        }
    }
}

class CategoriesViewHolder(itemVIew: View) :
    RecyclerView.ViewHolder(
        itemVIew
    ) {

    fun setDetails(title: String, cover: String, context: Context) {
        val catTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val catCover = itemView.findViewById<ImageView>(R.id.ivCover)

        catTitle.text = title
        Glide.with(context).load(cover).into(catCover)

        itemView.setOnClickListener {
            val pos = adapterPosition;

        }
    }


}