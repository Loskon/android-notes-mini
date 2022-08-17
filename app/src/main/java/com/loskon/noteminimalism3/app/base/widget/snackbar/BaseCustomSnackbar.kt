package com.loskon.noteminimalism3.app.base.widget.snackbar

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources.getSystem
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout

@Suppress("unused")
class BaseCustomSnackbar {

    private var snackbar: Snackbar? = null

    fun make(view: View, message: String?, length: Int): BaseCustomSnackbar {
        snackbar = Snackbar.make(view, message ?: UNKNOWN_ERROR_MESSAGE, length)
        return this
    }

    fun make(context: Context, view: View, message: String?, length: Int): BaseCustomSnackbar {
        snackbar = Snackbar.make(context, view, message ?: UNKNOWN_ERROR_MESSAGE, length)
        return this
    }

    fun make(view: View, layout: View): BaseCustomSnackbar {
        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)
        addCustomSnackbarView(layout)
        return this
    }

    private fun addCustomSnackbarView(layout: View) {
        val snackbarLayout = snackbar?.view as Snackbar.SnackbarLayout?
        snackbarLayout?.addView(layout)
    }

    fun setAnchorView(anchorView: View): BaseCustomSnackbar {
        snackbar?.anchorView = anchorView
        return this
    }

    fun setAction(text: String, action: () -> Unit) {
        snackbar?.setAction(text) { action() }
    }

    fun setActionRippleColor(color: Int) {
        getSnackbarMaterialButton()?.rippleColor = ColorStateList.valueOf(color)
    }

    fun setActionStroke(width: Int, color: Int) {
        getSnackbarMaterialButton()?.strokeWidth = width.toPx()
        getSnackbarMaterialButton()?.strokeColor = ColorStateList.valueOf(color)
    }

    fun setBackgroundTintList(color: Int) {
        snackbar?.view?.backgroundTintList = ColorStateList.valueOf(color)
    }

    fun setFont(font: Typeface?) {
        getSnackbarTextView()?.typeface = font
        getSnackbarMaterialButton()?.typeface = font
    }

    fun setTextColor(color: Int) {
        getSnackbarTextView()?.setTextColor(color)
        getSnackbarMaterialButton()?.setTextColor(color)
    }

    fun enableHideByClickSnackbar() {
        snackbar?.view?.setOnClickListener { dismiss() }
    }

    private fun getSnackbarTextView(): TextView? {
        val snackbarLayout = snackbar?.view as Snackbar.SnackbarLayout?
        val snackContentLayout = snackbarLayout?.getChildAt(0) as SnackbarContentLayout?

        return snackContentLayout?.getChildAt(0) as TextView?
    }

    private fun getSnackbarMaterialButton(): MaterialButton? {
        val snackbarLayout = snackbar?.view as Snackbar.SnackbarLayout?
        val snackContentLayout = snackbarLayout?.getChildAt(0) as SnackbarContentLayout?

        return snackContentLayout?.getChildAt(1) as MaterialButton?
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