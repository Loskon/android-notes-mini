package com.loskon.noteminimalism3.auxiliary.note

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.loskon.noteminimalism3.auxiliary.other.MyIntent
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsStorageKt
import com.loskon.noteminimalism3.ui.fragments.NoteFragmentKt
import com.loskon.noteminimalism3.ui.snackbars.SnackbarNoteMessage.Companion.MSG_ERROR
import com.loskon.noteminimalism3.ui.snackbars.SnackbarNoteMessage.Companion.MSG_INVALID_FORMAT
import com.loskon.noteminimalism3.ui.snackbars.SnackbarNoteMessage.Companion.MSG_NEED_COPY_TEXT
import com.loskon.noteminimalism3.ui.snackbars.SnackbarNoteMessage.Companion.MSG_NOTE_IS_EMPTY
import com.loskon.noteminimalism3.ui.snackbars.SnackbarNoteMessage.Companion.MSG_NOTE_TEXT_COPIED
import com.loskon.noteminimalism3.utils.showToast

/**
 * Помощник для работы с текстом заметки
 */

class TextNoteAssistant(
    private val context: Context,
    private val noteFragment: NoteFragmentKt
) {

    private val editText = noteFragment.getEditText
    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    fun pasteText() {
        if (clipboard.hasPrimaryClip()) {
            mainMethodPasteText()
        } else {
            showSnackbar(MSG_NEED_COPY_TEXT, false)
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

                editText.setText(pasteText)
                editText.setSelection(pasteText.length)

            } else {
                showSnackbar(MSG_NEED_COPY_TEXT, false)
            }

        } catch (exception: Exception) {
            exception.printStackTrace()
            showSnackbar(MSG_INVALID_FORMAT, false)
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
            showSnackbar(MSG_NOTE_IS_EMPTY, false)
        }
    }

    private fun mainMethodCopyText() {
        try {
            val clipData = ClipData.newPlainText("label_copy_text", editText.text.toString())
            clipboard.setPrimaryClip(clipData)
            showSnackbar(MSG_NOTE_TEXT_COPIED, true)
        } catch (exception: Exception) {
            exception.printStackTrace()
            showSnackbar(MSG_ERROR, false)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun saveTextFile() {
        val isAccess = PermissionsStorageKt.isAccessMemory(context)
        if (isAccess) goSaveTextFile()
    }

    fun goSaveTextFile() {
        val string = getTextTrim()
        if (string.isNotEmpty()) {
            context.showToast("save")
            //TextFile((context as NoteActivity)).createTextFile(string)
        } else {
            showSnackbar(MSG_NOTE_IS_EMPTY, false)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun sendText() {
        val title = getTextTrim()

        if (title.isNotEmpty()) {
            goSendText()
        } else {
            showSnackbar(MSG_NOTE_IS_EMPTY, false)
        }
    }

    private fun goSendText() {
        try {
            MyIntent.startShareText(context, editText)
        } catch (exception: Exception) {
            exception.printStackTrace()
            showSnackbar(MSG_ERROR, false)
        }
    }
}