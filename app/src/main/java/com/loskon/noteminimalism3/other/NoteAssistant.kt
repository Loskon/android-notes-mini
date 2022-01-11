package com.loskon.noteminimalism3.other

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.EditText
import android.widget.ScrollView
import com.loskon.noteminimalism3.files.SaveTextFile
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.requests.storage.ResultStorageAccess
import com.loskon.noteminimalism3.ui.fragments.NoteFragment
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar
import com.loskon.noteminimalism3.utils.scrollBottom

/**
 * Помощник для работы с текстом заметки
 */

class NoteAssistant(
    private val context: Context,
    private val fragment: NoteFragment,
    private val storageAccess: ResultStorageAccess,
    private val editText: EditText,
    private val scrollView: ScrollView
) {

    private val clipboard: ClipboardManager = context.getClipboardManager()

    //--- Вставить текст ---------------------------------------------------------------------------
    fun pasteText() {
        if (clipboard.hasPrimaryClip()) {
            performPasteText()
        } else {
            showSnackbar(WarningSnackbar.MSG_NEED_COPY_TEXT)
        }
    }

    private fun showSnackbar(message: String) = fragment.showSnackbar(message)

    private fun performPasteText() {
        val text: String = textTrim
        val pastedText: String

        try {
            val pasteData: CharSequence? = clipboard.primaryClip?.getItemAt(0)?.text

            if (pasteData.toString().trim().isNotEmpty()) {

                pastedText = if (text.isEmpty()) {
                    pasteData as String
                } else {
                    text + "\n\n" + pasteData
                }

                editText.setText(pastedText)
                editText.setSelection(pastedText.length)
                editText.scrollBottom(scrollView)
            } else {
                showSnackbar(WarningSnackbar.MSG_NEED_COPY_TEXT)
            }
        } catch (exception: Exception) {
            showSnackbar(WarningSnackbar.MSG_INVALID_FORMAT)
        }
    }

    private val textTrim: String get() = editText.text.toString().trim()

    //--- Копировать текст -------------------------------------------------------------------------
    fun copyText() {
        val text: String = textTrim

        if (text.isNotEmpty()) {
            performCopyText()
        } else {
            showSnackbar(WarningSnackbar.MSG_NOTE_IS_EMPTY)
        }
    }

    private fun performCopyText() {
        try {
            val clipData = ClipData.newPlainText("label_copy_text", editText.text.toString())
            clipboard.setPrimaryClip(clipData)
            showSnackbar(WarningSnackbar.MSG_NOTE_TEXT_COPIED)
        } catch (exception: Exception) {
            showSnackbar(WarningSnackbar.MSG_UNKNOWN_ERROR)
        }
    }

    //--- Сохранить текст в текстовом файле --------------------------------------------------------
    fun saveTextFile() {
        val hasAccessStorage: Boolean = storageAccess.hasAccessStorageRequest()
        if (hasAccessStorage) performSaveTextFile()
    }

    fun performSaveTextFile() {
        val text: String = textTrim

        if (text.isNotEmpty()) {
            SaveTextFile(context, fragment).creationFolderTextFiles(text)
        } else {
            showSnackbar(WarningSnackbar.MSG_NOTE_IS_EMPTY)
        }
    }

    //--- Поделиться текстом -----------------------------------------------------------------------
    fun shareText() {
        val text: String = textTrim

        if (text.isNotEmpty()) {
            performShareText()
        } else {
            showSnackbar(WarningSnackbar.MSG_NOTE_IS_EMPTY)
        }
    }

    private fun performShareText() {
        IntentManager.launchShareText(context, textTrim)
    }
}

// Extension functions
private fun Context.getClipboardManager(): ClipboardManager {
    return getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
}