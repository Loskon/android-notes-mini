package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.files.BackupFilesLimiter
import com.loskon.noteminimalism3.managers.setSliderColor
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Нижнее диалоговое меню со слайдером для выбора ограничения файлов бэкапов
 */

class NumberBackupsSheetDialog(private val context: Context) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val insertView = View.inflate(context, R.layout.sheet_slider, null)

    private val slider: Slider = insertView.findViewById(R.id.slider_range)

    init {
        dialog.setInsertView(insertView)
        dialog.setTextTitle(R.string.number_of_backup_title)
    }

    fun show() {
        establishColorViews()
        configViews()
        dialog.show()
    }

    private fun establishColorViews() {
        val color = PrefHelper.getAppColor(context)
        slider.setSliderColor(color)
    }

    private fun configViews() {
        val prefKey: String = context.getString(R.string.number_of_backup_key)
        val value: Int = PrefHelper.getNumberBackups(context)

        slider.value = value.toFloat()

        installHandlers(prefKey)
    }

    private fun installHandlers(prefKey: String) {
        dialog.buttonOk.setOnSingleClickListener {
            val sliderValue: Int = slider.value.toInt()
            PrefHelper.save(context, prefKey, sliderValue)

            BackupFilesLimiter.deleteExtraFiles(context)

            callback?.onChangeNumberBackups(sliderValue)
            dialog.dismiss()
        }
    }

    interface NumberBackupsCallback {
        fun onChangeNumberBackups(number: Int)
    }

    companion object {
        private var callback: NumberBackupsCallback? = null

        fun registerCallbackNumberBackups(callback: NumberBackupsCallback) {
            Companion.callback = callback
        }
    }
}


