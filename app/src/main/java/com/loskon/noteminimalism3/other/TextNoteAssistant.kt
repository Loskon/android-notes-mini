package com.loskon.noteminimalism3.other

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.EditText
import android.widget.ScrollView
import com.loskon.noteminimalism3.files.SaveTextFile
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.request.storage.ResultAccessStorage
import com.loskon.noteminimalism3.ui.fragments.NoteFragment
import com.loskon.noteminimalism3.ui.snackbars.SnackbarManager
import com.loskon.noteminimalism3.utils.IntentUtil

/**
 * Помощник для работы с текстом заметки
 */

class TextNoteAssistant(
    private val context: Context,
    private val fragment: NoteFragment
) {

    private val note: Note = fragment.getNote
    private val editText: EditText = fragment.getEditText
    private val scrollView: ScrollView = fragment.getScrollView

    private val clipboard: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    fun pasteText() {
        if (clipboard.hasPrimaryClip()) {
            performPasteText()
        } else {
            showSnackbar(SnackbarManager.MSG_NEED_COPY_TEXT)
        }
    }

    private fun showSnackbar(message: String) = fragment.showSnackbar(message)

    private fun performPasteText() {
        val text: String = textTrim
        val pasteText: String

        try {
            val textToPaste: CharSequence? = clipboard.primaryClip?.getItemAt(0)?.text

            if (textToPaste.toString().trim().isNotEmpty()) {

                pasteText = if (text.isEmpty()) {
                    textToPaste as String
                } else {
                    text + "\n\n" + textToPaste
                }

                note.title = pasteText
                editText.setText(pasteText)
                editText.setSelection(pasteText.length)
                editText.scrollBottom()
                showSnackbar(SnackbarManager.MSG_INSERTED_TEXT)
            } else {
                showSnackbar(SnackbarManager.MSG_NEED_COPY_TEXT)
            }
        } catch (exception: Exception) {
            showSnackbar(SnackbarManager.MSG_INVALID_FORMAT)
        }
    }

    private val textTrim: String get() = editText.text.toString().trim()

    private fun EditText.scrollBottom() {
        scrollView.post { scrollView.scrollTo(0, bottom) }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun copyText() {
        val text: String = textTrim

        if (text.isNotEmpty()) {
            performCopyText()
        } else {
            showSnackbar(SnackbarManager.MSG_NOTE_IS_EMPTY)
        }
    }

    private fun performCopyText() {
        try {
            val clipData = ClipData.newPlainText("label_copy_text", editText.text.toString())
            clipboard.setPrimaryClip(clipData)
            showSnackbar(SnackbarManager.MSG_NOTE_TEXT_COPIED)
        } catch (exception: Exception) {
            showSnackbar(SnackbarManager.MSG_UNKNOWN_ERROR)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun saveTextFile() {
        val hasAccess: Boolean = ResultAccessStorage.hasAccessStorageRequest(context)
        if (hasAccess) performSaveTextFile()
    }

    fun performSaveTextFile() {
        val text: String = textTrim

        if (text.isNotEmpty()) {
            SaveTextFile(context, fragment).creationFolderTextFiles(text)
        } else {
            showSnackbar(SnackbarManager.MSG_NOTE_IS_EMPTY)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun shareText() {
        val text: String = textTrim

        if (text.isNotEmpty()) {
            performShareText()
        } else {
            showSnackbar(SnackbarManager.MSG_NOTE_IS_EMPTY)
        }
    }

    private fun performShareText() {
        try {
            IntentUtil.launcherShareText(context, editText)
        } catch (exception: Exception) {
            showSnackbar(SnackbarManager.MSG_UNKNOWN_ERROR)
        }
    }
}