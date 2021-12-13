package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.R.style.DialogBackground
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setLayoutParams
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Единая форма для диалога
 */

class BaseMaterialDialog(private val context: Context) {

    private val dialogView: View = View.inflate(context, R.layout.base_dialog, null)
    private val alertDialog: AlertDialog = builderAlertDialog(dialogView)

    private val tvTitle: TextView = dialogView.findViewById(R.id.tv_base_dialog_title)
    private val linLayout: LinearLayout = dialogView.findViewById(R.id.container_base_dialog)
    private val btnOk: MaterialButton = dialogView.findViewById(R.id.btn_base_dialog_ok)
    private val btnCancel: MaterialButton = dialogView.findViewById(R.id.btn_base_dialog_cancel)

    private fun builderAlertDialog(view: View): AlertDialog {
        val alertDialog: AlertDialog = MaterialAlertDialogBuilder(context, DialogBackground)
            .setView(view)
            .create()

        val width: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT

        alertDialog.window?.setLayout(width, height)
        alertDialog.window?.setGravity(Gravity.CENTER)

        return alertDialog
    }

    fun show() {
        establishColorViews()
        installHandlers()
        hideContainer()
        alertDialog.show()
    }

    fun show(insertView: View) {
        establishColorViews()
        installHandlers()
        setInsertView(insertView)
        alertDialog.show()
    }

    private fun establishColorViews() {
        val color: Int = PrefManager.getAppColor(context)
        btnOk.setBackgroundColor(color)
        btnCancel.setTextColor(color)
    }

    private fun installHandlers() {
        btnCancel.setOnSingleClickListener { alertDialog.dismiss() }
    }

    private fun hideContainer() {
        linLayout.setVisibleView(false)
    }

    private fun setInsertView(insertView: View) {
        insertView.setLayoutParams()
        if (insertView.parent != null) linLayout.removeView(insertView)
        linLayout.addView(insertView)
    }

    fun dismiss() {
        alertDialog.dismiss()
    }

    // Внешние методы
    fun setTextTitle(stringId: Int) {
        tvTitle.text = context.getString(stringId)
    }

    fun setTextTitle(title: String) {
        tvTitle.text = title
    }

    fun setTextTitleVisibility(isVisible: Boolean) {
        tvTitle.setVisibleView(isVisible)
    }

    fun setBtnOkVisibility(isVisible: Boolean) {
        btnOk.setVisibleView(isVisible)
    }

    fun setBtnCancelVisibility(isVisible: Boolean) {
        btnCancel.setVisibleView(isVisible)
    }

    fun setProgressLayoutParametrs() {
        val width: Int = (context.resources.displayMetrics.widthPixels * 0.38).toInt()
        val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        alertDialog.window?.setLayout(width, height)
    }

    fun setCancelable(isCancel: Boolean) {
        alertDialog.setCancelable(isCancel)
    }

    val buttonOk: MaterialButton
        get() {
            return btnOk
        }
}