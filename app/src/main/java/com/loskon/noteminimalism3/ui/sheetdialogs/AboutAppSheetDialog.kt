package com.loskon.noteminimalism3.ui.sheetdialogs

import android.content.Context
import android.widget.TextView
import com.loskon.noteminimalism3.BuildConfig
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog

/**
 * Окно с информацией о приложении
 */

class AboutAppSheetDialog(sheetContext: Context) :
    BaseSheetDialog(sheetContext, R.layout.sheet_about_app) {

    private val tvAppVersion: TextView = view.findViewById(R.id.tv_about_app_version)

    init {
        configureDialogParameters()
        configureInsertedViews()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.app_name)
        setTextBtnCancel(R.string.to_close)
        setBtnOkVisibility(false)
    }

    private fun configureInsertedViews() {
        val versionName: String = BuildConfig.VERSION_NAME
        tvAppVersion.text = context.getString(R.string.sheet_about_version, versionName)
    }
}