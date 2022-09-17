package com.loskon.noteminimalism3.base.clipboardmanager

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object ClipboardHelper {

    fun copyText(context: Context, url: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("copy_links_label", url)
        clipboardManager.setPrimaryClip(clipData)
    }

    fun getPastedText(context: Context): String? {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboardManager.hasPrimaryClip().not()) return null
        return clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()
    }
}