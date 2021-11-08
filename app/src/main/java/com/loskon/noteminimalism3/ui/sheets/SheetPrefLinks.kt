package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.CheckBox
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager

/**
 * Состояние активности ссылок в заметках
 */

class SheetPrefLinks(private val context: Context) : View.OnClickListener {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val sheetView = View.inflate(context, R.layout.sheet_pref_links, null)

    private val checkBoxWeb: CheckBox = sheetView.findViewById(R.id.check_box_web)
    private val checkBoxMail: CheckBox = sheetView.findViewById(R.id.check_box_mail)
    private val checkBoxPhone: CheckBox = sheetView.findViewById(R.id.check_box_phone)

    init {
        setupColorViews()
        configViews()
        setStateChecked()
        installHandlers()
    }

    private fun setupColorViews() {
        val color = PrefManager.getAppColor(context)
        checkBoxWeb.buttonTintList = ColorStateList.valueOf(color)
        checkBoxMail.buttonTintList = ColorStateList.valueOf(color)
        checkBoxPhone.buttonTintList = ColorStateList.valueOf(color)
    }

    private fun configViews() {
        dialog.setInsertView(sheetView)
        dialog.setBtnOkVisibility(false)
        dialog.setTextTitle(R.string.sheet_pref_links_title)
        dialog.setTextBtnCancel(R.string.to_close)
    }

    private fun setStateChecked() {
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

    fun show() {
        dialog.show()
    }
}