package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.view.View
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.utils.setColorProgressIndicator

/**
 * Прогресс соединения с Firebase
 */

class DialogProgress(private val context: Context) {

    private val materialDialog: BaseMaterialDialog = BaseMaterialDialog(context)
    private val dialogView = View.inflate(context, R.layout.dialog_progress, null)

    private val circularProgressIndicator: CircularProgressIndicator =
        dialogView.findViewById(R.id.circularProgressIndicator)

    fun show() {
        configViews()
        setupColorViews()

        materialDialog.show(dialogView)
    }

    private fun configViews() {
        materialDialog.setCancelable(false)
        materialDialog.setTextTitleVisibility(false)
        materialDialog.setBtnOkVisibility(false)
        materialDialog.setBtnCancelVisibility(false)
        materialDialog.setWidthForProgress()
    }

    private fun setupColorViews() {
        val color = MyColor.getMyColor(context)
        circularProgressIndicator.setColorProgressIndicator(color)
    }

    fun close() {
        materialDialog.dismiss()
    }
}