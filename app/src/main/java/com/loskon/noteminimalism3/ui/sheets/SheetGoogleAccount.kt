package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.ui.fragments.BackupFragment

/**
 * Управление гугл-аккаунтом
 */

class SheetGoogleAccount(
    private val context: Context,
    private val fragment: BackupFragment
) :
    View.OnClickListener {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val sheetView = View.inflate(context, R.layout.sheet_google_account, null)

    private val btnLogout: MaterialButton = sheetView.findViewById(R.id.btn_data_account_logout)
    private val btnDelete: MaterialButton = sheetView.findViewById(R.id.btn_data_account_delete)

    init {
        setupColorViews()
        configViews()
        installHandlers()
    }

    private fun setupColorViews() {
        val color = PrefManager.getAppColor(context)
        btnLogout.setBackgroundColor(color)
        btnDelete.setBackgroundColor(color)
    }

    private fun configViews() {
        dialog.setInsertView(sheetView)
        dialog.setBtnOkVisibility(false)
        dialog.setTextTitle(R.string.sheet_account_title)
        dialog.setTextBtnCancel(R.string.to_close)
    }

    private fun installHandlers() {
        btnLogout.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_data_account_logout -> {
                dialog.dismiss()
                fragment.signOutFromGoogle()
            }
            R.id.btn_data_account_delete -> {
                dialog.dismiss()
                SheetDeleteGoogleAccount(context, fragment).show()
            }
        }
    }

    fun show() {
        dialog.show()
    }
}