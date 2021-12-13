package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.view.View
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setColorProgressIndicator
import com.loskon.noteminimalism3.sharedpref.PrefManager

/**
 * Прогресс соединения с Firebase
 */

class DialogProgress(private val context: Context) {

    private val dialog: BaseMaterialDialog = BaseMaterialDialog(context)
    private val insertView = View.inflate(context, R.layout.dialog_progress, null)

    private val indicator: CircularProgressIndicator =
        insertView.findViewById(R.id.circular_progress_indicator)

    init {
        dialog.setTextTitleVisibility(false)
        dialog.setBtnOkVisibility(false)
        dialog.setBtnCancelVisibility(false)
        dialog.setCancelable(false)
        dialog.setProgressLayoutParametrs()
    }

    fun show() {
        establishColorViews()
        dialog.show(insertView)
    }

    private fun establishColorViews() {
        val color: Int = PrefManager.getAppColor(context)
        indicator.setColorProgressIndicator(color)
    }

    fun close() {
        dialog.dismiss()
    }
}