package com.jamie.lgbtqdictionary.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jamie.lgbtqdictionary.GlobalProperties
import com.jamie.lgbtqdictionary.InternetConnection
import com.jamie.lgbtqdictionary.R
import com.jamie.lgbtqdictionary.adapters.CategoriesAdapter


class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private lateinit var categoriesRV: RecyclerView
    private lateinit var loader : ProgressBar
    private lateinit var globalProps : GlobalProperties
    private lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FragmentActivity) {
            mActivity = context
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
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

        val internetConnection = InternetConnection(mActivity)

        internetConnection.observe(this, { isConnected ->
            if (isConnected) {
                showCategories()
            }
        })

        return view
    }


    private fun showCategories() {
        val adapter = CategoriesAdapter(loader, globalProps.navStack, mActivity.supportFragmentManager)
        categoriesRV.adapter = adapter
        adapter.startListening()
    }


}
