package com.loskon.noteminimalism3.app.base.widget.snackbar

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.context.getColorKtx
import com.loskon.noteminimalism3.app.base.extension.context.getFontKtx

object CustomizedSnackbar {

    private var snackbar: BaseCustomSnackbar? = null

    fun makeShow(view: View, message: String, success: Boolean, anchorView: View? = null) {
        val successColor = if (success) R.color.success_color else R.color.error_color

        snackbar = BaseCustomSnackbar().create {
            make(view, message, Snackbar.LENGTH_LONG)
            if (anchorView != null) setAnchorView(anchorView)
            setBackgroundTintList(view.context.getColorKtx(successColor))
            setTextColor(Color.WHITE)
            setFont(view.context.getFontKtx(R.font.roboto_light))
            hideOnClick()
        }

        snackbar?.show()
    }

    fun dismiss() = snackbar?.dismiss()
}