package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.view.View
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setColorProgressIndicator

/**
 * Прогресс соединения с Firebase
 */

class DialogProgress(private val context: Context) {

    private val dialog: BaseMaterialDialog = BaseMaterialDialog(context)
    private val dialogView = View.inflate(context, R.layout.dialog_progress, null)

    private val indicator: CircularProgressIndicator =
        dialogView.findViewById(R.id.circularProgressIndicator)

    fun show() {
        configViews()
        setupColorViews()

        dialog.show(dialogView)
    }

    private fun configViews() {
        dialog.setCancelable(false)
        dialog.setTextTitleVisibility(false)
        dialog.setBtnOkVisibility(false)
        dialog.setBtnCancelVisibility(false)
        dialog.setWidthForProgress()
    }

    private fun setupColorViews() {
        val color: Int = PrefManager.getAppColor(context)
        indicator.setColorProgressIndicator(color)
    }

    fun close() {
        dialog.dismiss()
    }
}