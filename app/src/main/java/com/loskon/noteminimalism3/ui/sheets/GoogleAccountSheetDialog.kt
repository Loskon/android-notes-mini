package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.ui.fragments.BackupFragment

/**
 * Управление гугл-аккаунтом
 */

class GoogleAccountSheetDialog(
    private val context: Context,
    private val fragment: BackupFragment
) :
    View.OnClickListener {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val insertView = View.inflate(context, R.layout.sheet_google_account, null)

    private val btnLogout: MaterialButton = insertView.findViewById(R.id.btn_data_account_logout)
    private val btnDelete: MaterialButton = insertView.findViewById(R.id.btn_data_account_delete)

    init {
        dialog.addInsertedView(insertView)
        dialog.setBtnOkVisibility(false)
        dialog.setTextTitle(R.string.sheet_account_title)
        dialog.setTextBtnCancel(R.string.to_close)
    }

    fun show() {
        establishViewsColor()
        installHandlersForViews()
        dialog.show()
    }

    private fun establishViewsColor() {
        val color = PrefHelper.getAppColor(context)
        btnLogout.setBackgroundColor(color)
        btnDelete.setBackgroundColor(color)
    }

    private fun installHandlersForViews() {
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
                DeleteGoogleAccountSheetDialog(context, fragment).show()
            }
        }
    }
}