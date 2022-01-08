package com.loskon.noteminimalism3.ui.snackbars

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setColorBackgroundSnackbar
import com.loskon.noteminimalism3.utils.getShortFont

/**
 * Основа для Snackbar с текстом
 */

object WarningBaseSnackbar {

    private var snackbar: Snackbar? = null

    fun make(
        parent: ViewGroup, anchorView: View,
        message: String, isSuccess: Boolean
    ) {
        snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG)
        snackbar?.settingSnackbarView(parent, isSuccess)
        snackbar?.anchorView = anchorView
        snackbar?.show()
    }

    private fun Snackbar?.settingSnackbarView(parent: ViewGroup, isSuccess: Boolean) {
        val snackView: View? = this?.view
        val tvSnack: TextView? = snackView?.findViewById(R.id.snackbar_text)

        snackView?.setBackgroundResource(R.drawable.snackbar_background)
        snackView?.setColorBackgroundSnackbar(parent.context, isSuccess)
        snackView?.setOnClickListener { dismiss() }

        tvSnack?.setTextColor(Color.WHITE)
        tvSnack?.typeface = parent.context.getShortFont(R.font.roboto_light)
    }

    fun dismiss() {
        snackbar?.dismiss()
    }
}
