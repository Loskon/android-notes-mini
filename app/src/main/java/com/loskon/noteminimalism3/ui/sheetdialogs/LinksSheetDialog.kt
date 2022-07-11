package com.loskon.noteminimalism3.ui.sheetdialogs

import android.content.Context
import android.view.View
import android.widget.CheckBox
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setStrokeCheckBoxColor
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog

/**
 * Окно для переключения состояние активных ссылок
 */

class LinksSheetDialog(sheetContext: Context) :
    BaseSheetDialog(sheetContext, R.layout.sheet_links),
    View.OnClickListener {

    private val checkBoxWeb: CheckBox = view.findViewById(R.id.check_box_web)
    private val checkBoxMail: CheckBox = view.findViewById(R.id.check_box_mail)
    private val checkBoxPhone: CheckBox = view.findViewById(R.id.check_box_phone)

    init {
        configureDialogParameters()
        establishViewsColor()
        configureStateChecked()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sheet_pref_links_title)
        setTextBtnCancel(R.string.to_close)
        setBtnOkVisibility(false)
    }

    private fun establishViewsColor() {
        checkBoxWeb.setStrokeCheckBoxColor(color)
        checkBoxMail.setStrokeCheckBoxColor(color)
        checkBoxPhone.setStrokeCheckBoxColor(color)
    }

    private fun configureStateChecked() {
        checkBoxWeb.isChecked = AppPreference.isWeb(context)
        checkBoxMail.isChecked = AppPreference.isMail(context)
        checkBoxPhone.isChecked = AppPreference.isPhone(context)
    }

    private fun setupViewsListeners() {
        checkBoxWeb.setOnClickListener(this)
        checkBoxMail.setOnClickListener(this)
        checkBoxPhone.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.check_box_web -> {
                AppPreference.setResultWeb(context, checkBoxWeb.isChecked)
            }

            R.id.check_box_mail -> {
                AppPreference.setResultMail(context, checkBoxMail.isChecked)
            }

            R.id.check_box_phone -> {
                AppPreference.setResultPhone(context, checkBoxPhone.isChecked)
            }
        }
    }
}