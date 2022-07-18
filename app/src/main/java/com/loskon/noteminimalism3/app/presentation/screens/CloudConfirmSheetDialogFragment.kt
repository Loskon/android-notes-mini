package com.loskon.noteminimalism3.app.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.presentation.dialogfragment.BaseCustomizedSheetDialogFragment
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.BackupNewFragment.Companion.CLOUD_BACKUP_BUNDLE_KEY
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.BackupNewFragment.Companion.CLOUD_BACKUP_REQUEST_KEY

class CloudConfirmSheetDialogFragment : BaseCustomizedSheetDialogFragment() {

    private val args: CloudConfirmSheetDialogFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewsParameters()
        setupViewListener()
    }

    private fun setupViewsParameters() {
        setTitleDialog(R.string.sheet_confirm_action)
        setTextBtnOk(R.string.continue_action)
        setTextBtnCancel(R.string.no)
    }

    private fun setupViewListener() {
        setOnClickBtnOk {
            setFragmentResult(CLOUD_BACKUP_REQUEST_KEY, bundleOf(CLOUD_BACKUP_BUNDLE_KEY to args.isBackup))
        }
    }
}