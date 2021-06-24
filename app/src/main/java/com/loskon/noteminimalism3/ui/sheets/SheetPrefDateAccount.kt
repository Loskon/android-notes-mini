package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.ui.activities.BackupActivity
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Управление гугл-аккаунтом
 */

class SheetPrefDateAccount(private val context: Context) : View.OnClickListener {

    private val activity: BackupActivity = context as BackupActivity
    private val bpCloud = activity.bpCloud

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_data_account, null)

    private val btnLogout: MaterialButton = view.findViewById(R.id.btn_data_logout)
    private val btnYes: MaterialButton = view.findViewById(R.id.btn_data_yes)
    private val btnNo: MaterialButton = view.findViewById(R.id.btn_data_no)
    private val btnDelete: MaterialButton = view.findViewById(R.id.btn_data_delete)
    private val textView: TextView = view.findViewById(R.id.tv_data_warning)

    init {
        setupColorViews()
        configViews()
        setItemVisibility(false)
        installHandlers()

    }

    private fun setupColorViews() {
        val color = MyColor.getMyColor(context)
        btnLogout.setBackgroundColor(color)
        btnDelete.setBackgroundColor(color)
        btnNo.setBackgroundColor(color)
        btnYes.strokeColor = ColorStateList.valueOf(color)
        btnYes.setTextColor(color)
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
        btnYes.setOnClickListener(this)
        btnNo.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (activity.internetCheck.isConnected) {
            when (v?.id) {
                R.id.btn_data_logout -> {
                    bpCloud.signOut()
                    sheetDialog.dismiss()
                }
                R.id.btn_data_delete -> {
                    setItemVisibility(true)
                }
                R.id.btn_data_yes -> {
                    bpCloud.deleteData()
                    sheetDialog.dismiss()
                }
                R.id.btn_data_no -> {
                    setItemVisibility(false)
                }
            }
        } else {
            sheetDialog.dismiss()
            bpCloud.noInternet()
        }
    }

    private fun setItemVisibility(isVisible: Boolean) {
        textView.setVisibleView(isVisible)
        btnYes.setVisibleView(isVisible)
        btnNo.setVisibleView(isVisible)
        btnDelete.setVisibleView(!isVisible)
    }

    fun show() {
        sheetDialog.show()
    }
}