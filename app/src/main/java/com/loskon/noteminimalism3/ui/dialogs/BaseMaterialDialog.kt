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
import com.loskon.noteminimalism3.R.style.MaterialAlertDialogBackground
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setLayoutParams
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Единая форма для диалога
 */

class BaseMaterialDialog(private val context: Context) {

    private val dialogView: View = View.inflate(context, R.layout.base_dialog, null)
    private val alertDialog: AlertDialog = getAlertDialog(dialogView)

    private val textTitle: TextView = dialogView.findViewById(R.id.tv_base_dialog_title)
    private val linearLayout: LinearLayout = dialogView.findViewById(R.id.container_base_dialog)
    private val btnOk: MaterialButton = dialogView.findViewById(R.id.btn_base_dialog_ok)
    private val btnCancel: MaterialButton = dialogView.findViewById(R.id.btn_base_dialog_cancel)

    private fun getAlertDialog(view: View): AlertDialog {
        val alertDialog = MaterialAlertDialogBuilder(
            context,
            MaterialAlertDialogBackground
        )
            .setView(view)
            .create()

        val width = ViewGroup.LayoutParams.WRAP_CONTENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        alertDialog.window?.setLayout(width, height)
        alertDialog.window?.setGravity(Gravity.CENTER)

        return alertDialog
    }

    fun show() {
        setupColorViews()
        installHandlers()

        alertDialog.show()
    }

    fun show(insertView: View) {
        setupColorViews()
        installHandlers()
        setInsertView(insertView)

        alertDialog.show()
    }

    private fun setupColorViews() {
        val color: Int = PrefManager.getAppColor(context)
        btnOk.setBackgroundColor(color)
        btnCancel.setTextColor(color)
    }

    private fun installHandlers() {
        btnCancel.setOnSingleClickListener { alertDialog.dismiss() }
    }

    private fun setInsertView(insertView: View) {
        insertView.setLayoutParams()
        if (insertView.parent != null) linearLayout.removeView(insertView)
        linearLayout.addView(insertView)
    }


    //
    fun dismiss() {
        alertDialog.dismiss()
    }

    //
    fun setTextTitle(stringId: Int) {
        textTitle.text = context.getString(stringId)
    }

    fun setTextTitle(title: String) {
        textTitle.text = title
    }

    fun setTextTitleVisibility(isVisible: Boolean) {
        textTitle.setVisibleView(isVisible)
    }

    fun setContainerVisibility(isVisible: Boolean) {
        linearLayout.setVisibleView(isVisible)
    }

    fun setBtnOkVisibility(isVisible: Boolean) {
        btnOk.setVisibleView(isVisible)
    }

    fun setBtnCancelVisibility(isVisible: Boolean) {
        btnCancel.setVisibleView(isVisible)
    }


    //
    fun setWidthForProgress() {
        val width = (context.resources.displayMetrics.widthPixels * 0.38).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        alertDialog.window?.setLayout(width, height)
    }

    fun setCancelable(isCancel: Boolean) {
        alertDialog.setCancelable(isCancel)
    }

    //
    val buttonOk: MaterialButton
        get() {
            return btnOk
        }

}