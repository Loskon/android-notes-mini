package com.loskon.noteminimalism3.app.presentation.screens

import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment.BaseAppSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetCreateBackupBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding

class LocalBackupSheetDialogFragment : BaseAppSheetDialogFragment(R.layout.sheet_create_backup) {

    private val binding by viewBinding(SheetCreateBackupBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewsParameters()
        establishViewsColor()
        //setupViewListener()
    }

    private fun setupViewsParameters() {
        setTitleDialog(R.string.sheet_backup_title)
    }

    private fun establishViewsColor() {
        binding.inputLayoutBackup.boxStrokeColor = color
    }
}