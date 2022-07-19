package com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state

import android.content.IntentSender

sealed class BackupAction {
    class LaunchAuthContract(val intentSender: IntentSender) : BackupAction()
    class ShowSnackbar(val message: BackupMessageType) : BackupAction()
    class ShowConfirmDialog(val isBackup: Boolean) : BackupAction()
    object ShowAccountDialog : BackupAction()
}