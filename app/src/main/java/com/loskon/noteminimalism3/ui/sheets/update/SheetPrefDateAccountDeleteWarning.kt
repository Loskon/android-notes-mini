package com.loskon.noteminimalism3.ui.sheets.update

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.ui.fragments.update.BackupFragment
import com.loskon.noteminimalism3.ui.sheets.BaseSheetDialog

/**
 * Управление гугл-аккаунтом
 */

class SheetPrefDateAccountDeleteWarning(
    private val context: Context,
    private val fragment: BackupFragment
) :
    View.OnClickListener {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_data_account_delete_warning, null)

    private val btnYes: MaterialButton = view.findViewById(R.id.btn_data_yes)
    private val btnNo: MaterialButton = view.findViewById(R.id.btn_data_no)

    init {
        setupColorViews()
        configViews()
        installHandlers()
    }

    private fun setupColorViews() {
        val color = AppPref.getAppColor(context)
        btnNo.setBackgroundColor(color)
        btnYes.strokeColor = ColorStateList.valueOf(color)
        btnYes.setTextColor(color)
    }

    private fun configViews() {
        sheetDialog.setInsertView(view)
        sheetDialog.setBtnOkVisibility(false)
        sheetDialog.setBtnCancelVisibility(false)
        sheetDialog.setTextTitle(R.string.dg_delete_warnings)
    }

    private fun installHandlers() {
        btnYes.setOnClickListener(this)
        btnNo.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_data_yes -> {
                fragment.deleteUserAccount()
                sheetDialog.dismiss()
            }
            R.id.btn_data_no -> {
                sheetDialog.dismiss()
            }
        }
    }

    fun show() {
        sheetDialog.show()
    }
}