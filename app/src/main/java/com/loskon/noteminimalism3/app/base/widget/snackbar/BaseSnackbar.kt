package com.loskon.noteminimalism3.app.base.widget.snackbar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout

@Suppress("unused")
open class BaseSnackbar {

    private var snackbar: Snackbar? = null

    private var onDismissedListener: (() -> Unit)? = null
    private var onShowListener: (() -> Unit)? = null

    fun make(view: View, message: String?, length: Int): BaseSnackbar {
        snackbar = Snackbar.make(view, message ?: UNKNOWN_ERROR_MESSAGE, length)
        setupSnackbarTransientListener()
        return this
    }

    fun make(context: Context, view: View, message: String?, length: Int): BaseSnackbar {
        snackbar = Snackbar.make(context, view, message ?: UNKNOWN_ERROR_MESSAGE, length)
        setupSnackbarTransientListener()
        return this
    }

    fun make(view: View, addedView: View): BaseSnackbar {
        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)
        (snackbar?.view as Snackbar.SnackbarLayout?)?.addView(addedView)
        setupSnackbarTransientListener()
        return this
    }

    private fun setupSnackbarTransientListener() {
        snackbar?.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onShown(transientBottomBar: Snackbar?) {
                onShowListener?.invoke()
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                onDismissedListener?.invoke()
            }
        })
    }

    fun setAnchorView(anchorView: View): BaseSnackbar {
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
        getSnackbarMaterialButton()?.strokeWidth = width
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

    fun show() = snackbar?.show()

    fun dismiss() = snackbar?.dismiss()

    fun setOnShowListener(onShowListener: (() -> Unit)? = null) {
        this.onShowListener = onShowListener
    }

    fun setOnDismissedListener(onDismissedListener: (() -> Unit)? = null) {
        this.onDismissedListener = onDismissedListener
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

    inline fun create(functions: BaseSnackbar.() -> Unit): BaseSnackbar {
        this.functions()
        return this
    }

    companion object {
        private const val UNKNOWN_ERROR_MESSAGE = "Unknown error"
    }
}