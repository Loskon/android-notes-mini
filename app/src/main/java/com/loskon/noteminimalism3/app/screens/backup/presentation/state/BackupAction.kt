package com.loskon.noteminimalism3.app.screens.backup.presentation.state

import android.content.IntentSender

sealed class BackupAction {
    class LaunchAuthContract(val intentSender: IntentSender) : BackupAction()
    class ShowSnackbar(val messageType: BackupMessageType) : BackupAction()
    class ShowConfirmSheetDialog(val isBackup: Boolean) : BackupAction()
    object ShowAccountDialog : BackupAction()
}