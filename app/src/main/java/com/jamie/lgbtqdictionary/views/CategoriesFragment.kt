package com.jamie.lgbtqdictionary.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.adapters.CategoriesAdapter


class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    lateinit var categoriesRV: RecyclerView
    lateinit var loader : ProgressBar
    private lateinit var globalProps : GlobalProperties

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

        loader = view.findViewById(R.id.categoriesProgressBar)
        globalProps = this.context?.applicationContext as GlobalProperties

        showCategories()
        return view
    }


    private fun showCategories() {
        val adapter = CategoriesAdapter(loader, globalProps.navStack, activity!!.supportFragmentManager)
        categoriesRV.adapter = adapter
        adapter.startListening()
    }


}
