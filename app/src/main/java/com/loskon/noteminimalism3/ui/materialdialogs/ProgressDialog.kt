package com.loskon.noteminimalism3.ui.materialdialogs

import android.content.Context
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setColorProgressIndicator
import com.loskon.noteminimalism3.ui.basedialogs.BaseMaterialDialog

/**
 * Окно для показа прогресса соединения с Firebase
 */

class ProgressDialog(dialogContext: Context) :
    BaseMaterialDialog(dialogContext, R.layout.dialog_progress) {

    private val indicator: CircularProgressIndicator = view.findViewById(R.id.progress_indicator)

    init {
        configureDialogParameters()
        establishViewsColor()
    }

    private fun configureDialogParameters() {
        setTextTitleVisibility(false)
        setBtnOkVisibility(false)
        setBtnCancelVisibility(false)
        setCancelable(false)
        setProgressLayoutParameters()
    }

    private fun establishViewsColor() {
        indicator.setColorProgressIndicator(color)
    }
}