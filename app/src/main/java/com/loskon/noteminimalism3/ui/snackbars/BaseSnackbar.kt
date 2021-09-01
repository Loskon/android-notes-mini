package com.loskon.noteminimalism3.ui.snackbars

import android.content.Context
import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.utils.setColorBackgroundSnackbar

/**
 *
 */

object BaseSnackbar {

    private var snackbar: Snackbar? = null

    fun makeSnackbar(
        context: Context, layout: View,
        anchorView: View, message: String, isSuccess: Boolean
    ) {
        snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG)

        snackbar?.setTextColor(Color.WHITE)
        val snackbarView: View? = snackbar?.view
        snackbarView?.setBackgroundResource(R.drawable.snackbar_background)
        snackbarView?.setColorBackgroundSnackbar(context, isSuccess)
        snackbarView?.setOnClickListener { snackbar?.dismiss() }
        snackbar?.anchorView = anchorView

        snackbar?.show()
    }

    fun dismissSnackbar() {
        snackbar?.dismiss()
    }
}
