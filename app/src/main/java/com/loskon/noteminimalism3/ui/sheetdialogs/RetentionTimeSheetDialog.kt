package com.loskon.noteminimalism3.ui.sheetdialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.basedialogs.BaseSliderSheetDialog

/**
 * Окно для указания количества дней для хранения заметок в корзине
 */

class RetentionTimeSheetDialog(sheetContext: Context) :
    BaseSliderSheetDialog(
        sheetContext,
        R.string.retention_trash_key,
        AppPreference.getRetentionRange(sheetContext)
    ) {

    override fun onOkBtnClick() {
        val sliderValue: Int = currentSliderValue
        AppPreference.set(context, prefKey, sliderValue)
        callback?.onChangeRetention(sliderValue)
        dismiss()
    }

    //--- interface --------------------------------------------------------------------------------
    interface RetentionTimeCallback {
        fun onChangeRetention(range: Int)
    }

    companion object {
        private var callback: RetentionTimeCallback? = null

        fun registerRetentionTimeCallback(callback: RetentionTimeCallback?) {
            Companion.callback = callback
        }
    }
}