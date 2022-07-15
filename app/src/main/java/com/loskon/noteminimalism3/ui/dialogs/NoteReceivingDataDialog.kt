package com.loskon.noteminimalism3.ui.dialogs

import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.ui.activities.ReceivingDataActivity
import com.loskon.noteminimalism3.ui.basedialogs.BaseMaterialDialog

/**
 * Окно для выбора пути сохранения полученного текста
 */

class NoteReceivingDataDialog(private val activity: ReceivingDataActivity) :
    BaseMaterialDialog(activity, R.layout.dialog_receiving_data) {

    private val btnAdd: Button = view.findViewById(R.id.btn_add_in_new_note)
    private val btnUpdate: Button = view.findViewById(R.id.btn_update_old_note)

    init {
        configureDialogParameters()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTextTitleVisibility(false)
        setBtnOkVisibility(false)
        setBtnCancelVisibility(false)
    }

    private fun setupViewsListeners() {
        btnAdd.setDebounceClickListener { onAddBtnClick() }
        btnUpdate.setDebounceClickListener { dismiss() }
    }

    private fun onAddBtnClick() {
        dismiss()
        activity.addNewNote()
    }
}