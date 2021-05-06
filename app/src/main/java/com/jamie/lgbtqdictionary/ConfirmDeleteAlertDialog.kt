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
    private val isRemoveAll: Boolean
) :
    AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertBuilder = AlertDialog.Builder(mainContext)
        val deleteSubject = if (isRemoveAll) "all words" else "\"${word.word}\""
        alertBuilder.setTitle("Are you sure")
            .setMessage("Are you sure you want to remove $deleteSubject from bookmarks?")
            .setPositiveButton("Yes") { _, _ ->
                if (!isRemoveAll) {
                    roomWordViewModel.deleteBookmark(word.word)
                }
                else {
                    roomWordViewModel.deleteAllBookmarks()
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss()}

        return alertBuilder.create()
    }
}