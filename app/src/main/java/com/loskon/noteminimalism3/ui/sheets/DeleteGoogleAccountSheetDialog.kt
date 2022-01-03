package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setStrokeBtnColor
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.ui.fragments.BackupFragment

/**
 * Подтверждение удаление гугл-аккаунта
 */

class DeleteGoogleAccountSheetDialog(
    private val context: Context,
    private val fragment: BackupFragment
) :
    View.OnClickListener {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val insertView = View.inflate(context, R.layout.sheet_delete_account_warning, null)

    private val btnYes: MaterialButton = insertView.findViewById(R.id.btn_data_yes)
    private val btnNo: MaterialButton = insertView.findViewById(R.id.btn_data_no)

    init {
        sheetDialog.addInsertedView(insertView)
        sheetDialog.setBtnOkVisibility(false)
        sheetDialog.setBtnCancelVisibility(false)
        sheetDialog.setTextTitle(R.string.sheet_delete_warnings)
    }

    fun show() {
        establishColorViews()
        installHandlersForViews()
        sheetDialog.show()
    }

    private fun establishColorViews() {
        val color = PrefHelper.getAppColor(context)
        btnNo.setBackgroundColor(color)
        btnYes.setStrokeBtnColor(color)
        btnYes.setTextColor(color)
    }

    private fun installHandlersForViews() {
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
}