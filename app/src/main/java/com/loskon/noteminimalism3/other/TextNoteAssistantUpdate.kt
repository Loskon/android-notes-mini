package com.loskon.noteminimalism3.other

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.EditText
import com.loskon.noteminimalism3.auxiliary.other.MyIntent
import com.loskon.noteminimalism3.files.SaveTextFileUpdate
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.permissions.PermissionsStorageUpdate
import com.loskon.noteminimalism3.ui.fragments.update.NoteFragmentUpdate
import com.loskon.noteminimalism3.ui.snackbars.SnackbarMessage

/**
 * Помощник для работы с текстом заметки
 */

class TextNoteAssistantUpdate(
    private val context: Context,
    private val noteFragment: NoteFragmentUpdate
) {

    private val note: Note2 = noteFragment.getNote
    private val editText: EditText = noteFragment.getEditText
    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    fun pasteText() {
        if (clipboard.hasPrimaryClip()) {
            mainMethodPasteText()
        } else {
            showSnackbar(SnackbarMessage.MSG_NEED_COPY_TEXT, false)
        }
    }

    private fun showSnackbar(message: String, isSuccess: Boolean) {
        noteFragment.showSnackbar(message, isSuccess)
    }

    private fun mainMethodPasteText() {
        val title = getTextTrim()
        val pasteText: String

        try {
            val textToPaste = clipboard.primaryClip?.getItemAt(0)?.text

            if (textToPaste.toString().trim().isNotEmpty()) {

                pasteText = if (title.isEmpty()) {
                    textToPaste as String
                } else {
                    title + "\n\n" + textToPaste
                }

                note.title = pasteText
                editText.setText(pasteText)
                editText.setSelection(pasteText.length)

            } else {
                showSnackbar(SnackbarMessage.MSG_NEED_COPY_TEXT, false)
            }

        } catch (exception: Exception) {
            exception.printStackTrace()
            showSnackbar(SnackbarMessage.MSG_INVALID_FORMAT, false)
        }
    }

    private fun getTextTrim(): String = editText.text.toString().trim()

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun copyText() {
        val title = getTextTrim()

        if (title.isNotEmpty()) {
            mainMethodCopyText()
        } else {
            showSnackbar(SnackbarMessage.MSG_NOTE_IS_EMPTY, false)
        }
    }

    private fun mainMethodCopyText() {
        try {
            val clipData = ClipData.newPlainText("label_copy_text", editText.text.toString())
            clipboard.setPrimaryClip(clipData)
            showSnackbar(SnackbarMessage.MSG_NOTE_TEXT_COPIED, true)
        } catch (exception: Exception) {
            exception.printStackTrace()
            showSnackbar(SnackbarMessage.MSG_ERROR, false)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun saveTextFile() {
        if (PermissionsStorageUpdate.isAccessMemory(context)) mainMethodSaveTextFile()
    }

    fun mainMethodSaveTextFile() {
        val string = getTextTrim()

        if (string.isNotEmpty()) {
            SaveTextFileUpdate(context, noteFragment).createTextFile(string)
        } else {
            showSnackbar(SnackbarMessage.MSG_NOTE_IS_EMPTY, false)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun sendText() {
        val title = getTextTrim()

        if (title.isNotEmpty()) {
            goSendText()
        } else {
            showSnackbar(SnackbarMessage.MSG_NOTE_IS_EMPTY, false)
        }
    }

    private fun goSendText() {
        try {
            MyIntent.startShareText(context, editText)
        } catch (exception: Exception) {
            exception.printStackTrace()
            showSnackbar(SnackbarMessage.MSG_ERROR, false)
        }
    }
}