package com.jamie.lgbtqdictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class GeneralWordFragment : Fragment(R.layout.fragment_word) {
    lateinit var tvID : TextView
    lateinit var tvTitle : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_word, container, false)

        tvID = view.findViewById(R.id.tvID)
        tvTitle = view.findViewById(R.id.tvTitle)
        val bundle = this.arguments
        tvID.text = bundle!!.get("id").toString()
        tvTitle.text = bundle.get("title").toString()

        return view
    }


}