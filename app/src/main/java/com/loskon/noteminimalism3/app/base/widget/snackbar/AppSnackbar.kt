package com.loskon.noteminimalism3.app.base.widget.snackbar

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.context.getColorKtx
import com.loskon.noteminimalism3.app.base.extension.context.getFontKtx

object AppSnackbar {

    private var snackbar: BaseCustomSnackbar? = null

    fun make(view: View, message: String, success: Boolean, anchorView: View? = null): BaseCustomSnackbar? {
        snackbar = BaseCustomSnackbar().create {
            make(view, message, Snackbar.LENGTH_LONG)
            if (anchorView != null) setAnchorView(anchorView)
            setBackgroundTintList(view.context.getColorKtx(getSuccessColor(success)))
            setFont(view.context.getFontKtx(R.font.roboto_light))
            setTextColor(Color.WHITE)
            enableHideByClickSnackbar()
            setOnDismissedListener { snackbar = null }
        }

        return snackbar
    }

    private fun getSuccessColor(success: Boolean): Int {
        return if (success) {
            R.color.success_color
        } else {
            R.color.error_color
        }
    }

    fun dismiss() = snackbar?.dismiss()
}