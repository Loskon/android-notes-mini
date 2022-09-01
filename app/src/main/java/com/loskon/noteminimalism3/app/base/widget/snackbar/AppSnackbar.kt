package com.loskon.noteminimalism3.app.base.widget.snackbar

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.context.getColorKtx
import com.loskon.noteminimalism3.app.base.extension.context.getFontKtx

class AppSnackbar : BaseSnackbar() {

    fun make(view: View, message: String, success: Boolean, anchorView: View? = null): AppSnackbar {
        make(view, message, Snackbar.LENGTH_LONG)
        setBackgroundTintList(view.context.getColorKtx(getSuccessColorId(success)))
        setFont(view.context.getFontKtx(R.font.roboto_light))
        setTextColor(Color.WHITE)
        enableHideByClickSnackbar()
        if (anchorView != null) setAnchorView(anchorView)
        return this
    }

    private fun getSuccessColorId(success: Boolean): Int {
        return if (success) {
            R.color.success_color
        } else {
            R.color.error_color
        }
    }
}