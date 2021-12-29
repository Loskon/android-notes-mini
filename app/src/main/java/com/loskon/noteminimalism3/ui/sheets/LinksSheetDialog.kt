package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.CheckBox
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setStrokeCheckBoxColor
import com.loskon.noteminimalism3.sharedpref.PrefHelper

/**
 * Состояние активности ссылок в заметках
 */

class LinksSheetDialog(private val context: Context) : View.OnClickListener {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
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
        val color = PrefHelper.getAppColor(context)
        checkBoxWeb.setStrokeCheckBoxColor(color)
        checkBoxMail.setStrokeCheckBoxColor(color)
        checkBoxPhone.setStrokeCheckBoxColor(color)
    }

    private fun configureStateChecked() {
        checkBoxWeb.isChecked = PrefHelper.isWeb(context)
        checkBoxMail.isChecked = PrefHelper.isMail(context)
        checkBoxPhone.isChecked = PrefHelper.isPhone(context)
    }

    private fun installHandlers() {
        checkBoxWeb.setOnClickListener(this)
        checkBoxMail.setOnClickListener(this)
        checkBoxPhone.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.check_box_web -> {
                PrefHelper.setResultWeb(context, checkBoxWeb.isChecked)
            }

            R.id.check_box_mail -> {
                PrefHelper.setResultMail(context, checkBoxMail.isChecked)
            }

            R.id.check_box_phone -> {
                PrefHelper.setResultPhone(context, checkBoxPhone.isChecked)
            }
        }
    }
}