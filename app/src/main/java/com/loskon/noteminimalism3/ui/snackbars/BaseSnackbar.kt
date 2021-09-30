package com.loskon.noteminimalism3.ui.snackbars

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.utils.ripple
import com.loskon.noteminimalism3.utils.setColorBackgroundSnackbar


/**
 * Конструктор для Snackbar
 */

object BaseSnackbar {

    private var snackbar: Snackbar? = null

    fun make(
        context: Context, layout: View,
        anchorView: View, message: String,
        isSuccess: Boolean
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

    fun make(
        context: Context, layout: View,
        anchorView: View, message: String
    ) {
        snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG)

        val snackButton: Button? = snackbar?.view?.findViewById(R.id.snackbar_action)

        snackButton?.ripple()
        snackbar?.setActionTextColor(AppPref.getAppColor(context))
        snackbar?.setAction(context.getString(android.R.string.ok)) { snackbar?.dismiss() }
        val snackbarView: View? = snackbar?.view
        snackbarView?.setBackgroundResource(R.drawable.snackbar_background)
        snackbar?.anchorView = anchorView

        snackbar?.show()
    }

    fun dismiss() {
        snackbar?.dismiss()
    }
}
