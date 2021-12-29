package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.TextView
import com.loskon.noteminimalism3.BuildConfig
import com.loskon.noteminimalism3.R

/**
 * Состояние активности ссылок в заметках
 */

class AboutAppSheetDialog(private val context: Context) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val insertView = View.inflate(context, R.layout.sheet_pref_about_app, null)

    private val tvVersion: TextView = insertView.findViewById(R.id.tv_about_app_version)

    init {
        dialog.setInsertView(insertView)
        dialog.setBtnOkVisibility(false)
        dialog.setTextTitle(R.string.app_name)
        dialog.setTextBtnCancel(R.string.to_close)
    }

    fun show() {
        setVersionApp()
        dialog.show()
    }

    private fun setVersionApp() {
        val versionName: String = BuildConfig.VERSION_NAME
        tvVersion.text = context.getString(R.string.sheet_about_version, versionName)
    }
}