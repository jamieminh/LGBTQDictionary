package com.jamie.lgbtqdictionary

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment

class SimpleAlertDialog(
    private val mainContext: Context,
    private val title: String,
    private val message: String,
) :
    AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertBuilder = AlertDialog.Builder(mainContext)
        alertBuilder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> { }}

        return alertBuilder.create()
    }
}