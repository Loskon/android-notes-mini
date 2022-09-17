package com.loskon.noteminimalism3.ui.basedialogs

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.R.style.DialogBackground
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.setLayoutParamsForInsertedView
import com.loskon.noteminimalism3.utils.setVisibilityKtx

/**
 * Основа для материальных диалоговых окон
 */

open class BaseMaterialDialog(
    private val dialogContext: Context,
    private val insertViewId: Int? = null
) {

    private val dialogView: View = View.inflate(dialogContext, R.layout.dialog_base, null)
    private val tvTitle: TextView = dialogView.findViewById(R.id.tv_base_dialog_title)
    private val frameLayout: FrameLayout = dialogView.findViewById(R.id.container_base_dialog)
    private val buttonOk: MaterialButton = dialogView.findViewById(R.id.btn_base_dialog_ok)
    private val btnCancel: MaterialButton = dialogView.findViewById(R.id.btn_base_dialog_cancel)

    //----------------------------------------------------------------------------------------------
    private val alertDialog: AlertDialog = builderAlertDialog()

    private fun builderAlertDialog(): AlertDialog {
        val alertDialog: AlertDialog = MaterialAlertDialogBuilder(dialogContext, DialogBackground)
            .setView(dialogView)
            .create()

        val width: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        alertDialog.window?.setLayout(width, height)
        alertDialog.window?.setGravity(Gravity.CENTER)

        return alertDialog
    }

    fun show() = alertDialog.show()

    fun dismiss() = alertDialog.dismiss()

    fun setCancelable(isCancel: Boolean) = alertDialog.setCancelable(isCancel)

    val context: Context get() = dialogContext

    //----------------------------------------------------------------------------------------------
    private var appColor: Int = 0

    init {
        establishViewsColor()
        setupViewsListeners()
        addInsertedView()
    }

    private fun establishViewsColor() {
        appColor = AppPreference.getColor(dialogContext)
        buttonOk.setBackgroundColor(appColor)
        btnCancel.setTextColor(appColor)
    }

    private fun setupViewsListeners() {
        btnCancel.setDebounceClickListener { alertDialog.dismiss() }
    }

    private fun addInsertedView() {
        if (insertViewId != null) {
            val insertView: View = View.inflate(dialogContext, insertViewId, null)
            insertView.setLayoutParamsForInsertedView()
            frameLayout.addView(insertView)
        } else {
            frameLayout.setVisibilityKtx(false)
        }
    }


    //----------------------------------------------------------------------------------------------
    fun setTitleDialog(@StringRes stringId: Int) {
        tvTitle.text = dialogContext.getString(stringId)
    }

    fun setTitleDialog(string: String) {
        tvTitle.text = string
    }

    fun setTextTitleVisibility(isVisible: Boolean) {
        tvTitle.setVisibilityKtx(isVisible)
    }

    fun setBtnOkVisibility(isVisible: Boolean) {
        buttonOk.setVisibilityKtx(isVisible)
    }

    fun setBtnCancelVisibility(isVisible: Boolean) {
        btnCancel.setVisibilityKtx(isVisible)
    }

    fun setProgressLayoutParameters() {
        val width: Int = (dialogContext.resources.displayMetrics.widthPixels * 0.38).toInt()
        val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        alertDialog.window?.setLayout(width, height)
    }

    //----------------------------------------------------------------------------------------------
    val btnOk: MaterialButton
        get() {
            return buttonOk
        }

    val view: View
        get() {
            return dialogView
        }

    val color: Int
        get() {
            return appColor
        }
}