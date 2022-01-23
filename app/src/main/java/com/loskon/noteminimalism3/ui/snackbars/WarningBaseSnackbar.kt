package com.loskon.noteminimalism3.ui.snackbars

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setBackgroundTintColor
import com.loskon.noteminimalism3.utils.getShortFont

/**
 * Основа для показа Snackbar с предупреждениями/уведомлениями
 */

object WarningBaseSnackbar {

    private var snackbar: Snackbar? = null

    fun make(
        parentLayout: ViewGroup, anchorView: View,
        message: String, isSuccess: Boolean
    ) {
        snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
        snackbar?.nullifySnackbarWhenDismissed()
        snackbar?.settingSnackbarView(parentLayout.context, isSuccess)
        snackbar?.anchorView = anchorView
        snackbar?.show()
    }

    private fun Snackbar?.nullifySnackbarWhenDismissed() {
        this?.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onShown(transientBottomBar: Snackbar?) {}

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                snackbar = null
            }
        })
    }

    private fun Snackbar?.settingSnackbarView(
        context: Context,
        isSuccess: Boolean
    ) {
        val snackView: View? = this?.view
        val tvSnack: TextView? = snackView?.findViewById(R.id.snackbar_text)

        snackView?.setBackgroundResource(R.drawable.snackbar_background)
        snackView?.setBackgroundTintColor(context, isSuccess)
        snackView?.setOnClickListener { dismiss() }

        tvSnack?.setTextColor(Color.WHITE)
        tvSnack?.typeface = context.getShortFont(R.font.roboto_light)
    }

    fun dismiss() {
        snackbar?.dismiss()
    }
}
