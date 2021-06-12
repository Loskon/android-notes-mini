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
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.utils.setLayoutParams
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView


/**
 *
 */

class BaseMaterialDialog(private val context: Context) {

    private val dialogView: View = View.inflate(context, R.layout.dialog_base, null)
    private val alertDialog: AlertDialog = getAlertDialog(dialogView)

    private val textTitle: TextView = dialogView.findViewById(R.id.tv_base_title)
    private val linearLayout: LinearLayout = dialogView.findViewById(R.id.sheet_container)
    private val btnOk: MaterialButton = dialogView.findViewById(R.id.btn_baset_ok)
    private val btnCancel: MaterialButton = dialogView.findViewById(R.id.btn_base_cancel)

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
        val color: Int = MyColor.getMyColor(context)
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

    fun setCancelable(isCancel: Boolean) {
        alertDialog.setCancelable(isCancel)
    }

    fun setWidthForProgress() {
        val width = (context.resources.displayMetrics.widthPixels * 0.38).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        alertDialog.window?.setLayout(width, height)
    }


    //
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
    val getButtonOk: MaterialButton
        get() {
            return btnOk
        }

}