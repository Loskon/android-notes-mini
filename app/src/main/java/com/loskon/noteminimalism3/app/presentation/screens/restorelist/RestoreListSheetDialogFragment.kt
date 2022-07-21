package com.loskon.noteminimalism3.app.presentation.screens.restorelist

import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment.BaseAppSheetDialogFragmentNew
import com.loskon.noteminimalism3.databinding.SheetFileListBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding

class RestoreListSheetDialogFragment : BaseAppSheetDialogFragmentNew() {

    private val binding by viewBinding(SheetFileListBinding::inflate)

    private val filesAdapter = FileListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        insertView(binding.root)
        setupDialogViewsParameters()
    }

    private fun setupDialogViewsParameters() {
        setTitleDialog(R.string.sheet_restore_db_title)
        setTextBtnCancel(R.string.to_close)
        setBtnOkVisibility(false)
    }
}