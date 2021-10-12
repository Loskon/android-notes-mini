package com.loskon.noteminimalism3.ui.sheets.update

import android.content.Context
import android.view.View
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.ui.fragments.update.BackupFragment
import com.loskon.noteminimalism3.ui.sheets.BaseSheetDialog

/**
 * Управление гугл-аккаунтом
 */

class SheetPrefDateAccountUpdate(
    private val context: Context,
    private val fragment: BackupFragment
) :
    View.OnClickListener {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_data_account_update, null)

    private val btnLogout: MaterialButton = view.findViewById(R.id.btn_data_logout)
    private val btnDelete: MaterialButton = view.findViewById(R.id.btn_data_delete)

    init {
        setupColorViews()
        configViews()
        installHandlers()
    }

    private fun setupColorViews() {
        val color = AppPref.getAppColor(context)
        btnLogout.setBackgroundColor(color)
        btnDelete.setBackgroundColor(color)
    }

    private fun configViews() {
        sheetDialog.setInsertView(view)
        sheetDialog.setBtnOkVisibility(false)
        sheetDialog.setTextTitle(R.string.dg_data_title)
        sheetDialog.setTextBtnCancel(R.string.to_close)
    }

    private fun installHandlers() {
        btnLogout.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_data_logout -> {
                sheetDialog.dismiss()
                fragment.signOutFromGoogle()
            }
            R.id.btn_data_delete -> {
                sheetDialog.dismiss()
                SheetPrefDateAccountDeleteWarning(context, fragment).show()
            }
        }
    }

    fun show() {
        sheetDialog.show()
    }
}