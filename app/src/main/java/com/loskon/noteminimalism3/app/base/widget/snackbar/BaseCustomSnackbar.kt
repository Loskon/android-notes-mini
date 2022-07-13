package com.loskon.githubapi.app.base.widget.snackbar

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources.getSystem
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout

class BaseCustomSnackbar {

    private var snackbar: Snackbar? = null
    private var textView: TextView? = null
    private var button: MaterialButton? = null

    fun make(view: View, message: String?, length: Int) {
        snackbar = Snackbar.make(view, message ?: UNKNOWN_ERROR_MESSAGE, length)
        initializationSnackbarViews()
    }

    fun make(context: Context, view: View, message: String?, length: Int) {
        snackbar = Snackbar.make(context, view, message ?: UNKNOWN_ERROR_MESSAGE, length)
        initializationSnackbarViews()
    }

    private fun initializationSnackbarViews() {
        val snackbarLayout = snackbar?.view as Snackbar.SnackbarLayout
        val snackContentLayout = snackbarLayout.getChildAt(0) as SnackbarContentLayout

        textView = snackContentLayout.getChildAt(0) as TextView
        button = snackContentLayout.getChildAt(1) as MaterialButton
    }

    fun setAnchorView(anchorView: View) {
        snackbar?.anchorView = anchorView
    }

    fun setAction(text: String, action: () -> Unit) {
        snackbar?.setAction(text) { action() }
    }

    fun setActionRippleColor(color: Int) {
        button?.rippleColor = ColorStateList.valueOf(color)
    }

    fun setActionStroke(width: Int, color: Int) {
        button?.strokeWidth = width.toPx()
        button?.strokeColor = ColorStateList.valueOf(color)
    }

    fun setBackgroundTintList(color: Int) {
        snackbar?.view?.backgroundTintList = ColorStateList.valueOf(color)
    }

    fun setFont(font: Typeface?) {
        textView?.typeface = font
        button?.typeface = font
    }

    fun setTextColor(color: Int) {
        textView?.setTextColor(color)
        button?.setTextColor(color)
    }

    private fun Int.toPx(): Int = (this * getSystem().displayMetrics.density).toInt()

    inline fun create(functions: BaseCustomSnackbar.() -> Unit): BaseCustomSnackbar {
        this.functions()
        return this
    }

    fun show() = snackbar?.show()

    fun dismiss() = snackbar?.dismiss()

    companion object {
        private const val UNKNOWN_ERROR_MESSAGE = "Unknown error"
    }
}