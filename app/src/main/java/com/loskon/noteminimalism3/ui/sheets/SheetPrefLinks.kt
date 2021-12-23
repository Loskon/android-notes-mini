package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.CheckBox
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setStrokeCheckBoxColor
import com.loskon.noteminimalism3.sharedpref.PrefManager

/**
 * Состояние активности ссылок в заметках
 */

class SheetPrefLinks(private val context: Context) : View.OnClickListener {

    private val dialog: BaseSheetDialogs = BaseSheetDialogs(context)
    private val insertView = View.inflate(context, R.layout.sheet_pref_links, null)

    private val checkBoxWeb: CheckBox = insertView.findViewById(R.id.check_box_web)
    private val checkBoxMail: CheckBox = insertView.findViewById(R.id.check_box_mail)
    private val checkBoxPhone: CheckBox = insertView.findViewById(R.id.check_box_phone)

    init {
        dialog.setInsertView(insertView)
        dialog.setBtnOkVisibility(false)
        dialog.setTextTitle(R.string.sheet_pref_links_title)
        dialog.setTextBtnCancel(R.string.to_close)
    }

    fun show() {
        establishColorViews()
        configureStateChecked()
        installHandlers()
        dialog.show()
    }

    private fun establishColorViews() {
        val color = PrefManager.getAppColor(context)
        checkBoxWeb.setStrokeCheckBoxColor(color)
        checkBoxMail.setStrokeCheckBoxColor(color)
        checkBoxPhone.setStrokeCheckBoxColor(color)
    }

    private fun configureStateChecked() {
        checkBoxWeb.isChecked = PrefManager.isWeb(context)
        checkBoxMail.isChecked = PrefManager.isMail(context)
        checkBoxPhone.isChecked = PrefManager.isPhone(context)
    }

    private fun installHandlers() {
        checkBoxWeb.setOnClickListener(this)
        checkBoxMail.setOnClickListener(this)
        checkBoxPhone.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.check_box_web -> {
                PrefManager.setResultWeb(context, checkBoxWeb.isChecked)
            }

            R.id.check_box_mail -> {
                PrefManager.setResultMail(context, checkBoxMail.isChecked)
            }

            R.id.check_box_phone -> {
                PrefManager.setResultPhone(context, checkBoxPhone.isChecked)
            }
        }
    }
}