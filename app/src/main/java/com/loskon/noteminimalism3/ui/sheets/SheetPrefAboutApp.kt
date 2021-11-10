package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.TextView
import com.loskon.noteminimalism3.BuildConfig
import com.loskon.noteminimalism3.R

/**
 * Состояние активности ссылок в заметках
 */

class SheetPrefAboutApp(private val context: Context) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val sheetView = View.inflate(context, R.layout.sheet_pref_about_app, null)

    private val tvVersion: TextView = sheetView.findViewById(R.id.tv_about_app)

    init {
        configViews()
        setVersionApp()
    }

    private fun configViews() {
        dialog.setInsertView(sheetView)
        dialog.setBtnOkVisibility(false)
        dialog.setTextTitle(R.string.app_name)
        dialog.setTextBtnCancel(R.string.to_close)
    }

    private fun setVersionApp() {
        val versionName: String = BuildConfig.VERSION_NAME
        tvVersion.text = context.getString(R.string.sheet_about_version, versionName)
    }

    fun show() {
        dialog.show()
    }
}