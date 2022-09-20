package com.loskon.noteminimalism3.ui.sheetdialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.files.BackupFileHelper
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.basedialogs.BaseSliderSheetDialog

/**
 * Окно для указания количества сохраняемых файлов бэкапа
 */

class NumberBackupsSheetDialog(sheetContext: Context) :
    BaseSliderSheetDialog(
        sheetContext,
        R.string.number_of_backup_key,
        AppPreference.getBackupsCount(sheetContext)
    ) {

    override fun onOkBtnClick() {
        val sliderValue: Int = currentSliderValue
        AppPreference.set(context, prefKey, sliderValue)
        BackupFileHelper.deleteExtraFiles(context)
        callback?.onChangeNumberBackups(sliderValue)
        dismiss()
    }

    //--- interface --------------------------------------------------------------------------------
    interface NumberBackupsCallback {
        fun onChangeNumberBackups(number: Int)
    }

    companion object {
        private var callback: NumberBackupsCallback? = null

        fun registerNumberBackupsCallback(callback: NumberBackupsCallback?) {
            Companion.callback = callback
        }
    }
}