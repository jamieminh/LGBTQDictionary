package com.jamie.lgbtqdictionary

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.jamie.lgbtqdictionary.models.words.BookmarkedWord
import com.jamie.lgbtqdictionary.viewmodels.words.RoomWordViewModel

class ConfirmDeleteAlertDialog(
    private val mainContext: Context,
    private val word: BookmarkedWord,
    private var roomWordViewModel: RoomWordViewModel,
) :
    AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertBuilder = AlertDialog.Builder(mainContext)
        alertBuilder.setTitle("Are you sure")
            .setMessage("Are you sure you want to remove \"${word.word}\" from bookmarks?")
            .setPositiveButton("Yes") { _, _ ->
                roomWordViewModel.deleteBookmark(word)
            }
            .setNegativeButton("No") { _, _ -> {  }}

        return alertBuilder.create()
    }
}