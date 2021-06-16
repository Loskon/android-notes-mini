package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.CheckBox
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref

/**
 * Состояние активности ссылок в заметках
 */

class SheetPrefLinks(private val context: Context) : View.OnClickListener {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_pref_links, null)

    private val checkBoxWeb: CheckBox = view.findViewById(R.id.check_box_web)
    private val checkBoxMail: CheckBox = view.findViewById(R.id.check_box_mail)
    private val checkBoxPhone: CheckBox = view.findViewById(R.id.check_box_phone)

    init {
        setupColorViews()
        configViews()
        setStateChecked()
        installHandlers()
    }

    private fun setupColorViews() {
        val color = MyColor.getMyColor(context)
        checkBoxWeb.buttonTintList = ColorStateList.valueOf(color)
        checkBoxMail.buttonTintList = ColorStateList.valueOf(color)
        checkBoxPhone.buttonTintList = ColorStateList.valueOf(color)
    }

    private fun configViews() {
        sheetDialog.setInsertView(view)
        sheetDialog.setBtnOkVisibility(false)
        sheetDialog.setTextTitle(R.string.sheet_pref_links_title)
        sheetDialog.setTextBtnCancel(R.string.to_close)
    }

    private fun setStateChecked() {
        checkBoxWeb.isChecked = GetSharedPref.isWeb(context)
        checkBoxMail.isChecked = GetSharedPref.isMail(context)
        checkBoxPhone.isChecked = GetSharedPref.isPhone(context)
    }

    private fun installHandlers() {
        checkBoxWeb.setOnClickListener(this)
        checkBoxMail.setOnClickListener(this)
        checkBoxPhone.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.check_box_web -> {
                saveResult(MyPrefKey.KEY_WEB, checkBoxWeb)
            }
            R.id.check_box_mail -> {
                saveResult(MyPrefKey.KEY_MAIL, checkBoxMail)
            }
            R.id.check_box_phone -> {
                saveResult(MyPrefKey.KEY_PHONE, checkBoxPhone)
            }
        }
    }

    private fun saveResult(key: String, checkBox: CheckBox) {
        MySharedPref.setBoolean(context, key, checkBox.isChecked)
    }

    fun show() {
        sheetDialog.show()
    }
}