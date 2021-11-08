package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.Button
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.BackupFilesLimiter
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setSliderColor

/**
 * Нижнее диалоговое меню со слайдером для выбора ограничения файлов бэкапов
 */

class SheetPrefSliderNumberBackups(private val context: Context) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val sheetView = View.inflate(context, R.layout.sheet_pref_slider, null)

    private val slider: Slider = sheetView.findViewById(R.id.slider_range)
    private val btnOk: Button = dialog.buttonOk

    init {
        setupColorViews()
        configViews()
    }

    private fun setupColorViews() {
        val color = PrefManager.getAppColor(context)
        slider.setSliderColor(color)
    }

    private fun configViews() {
        dialog.setInsertView(sheetView)
    }

    fun show() {
        val preferenceKey: String = context.getString(R.string.num_of_backup_title)
        val value: Int = PrefManager.getNumberBackups(context)

        dialog.setTextTitle(preferenceKey)
        slider.value = value.toFloat()

        installHandlers(preferenceKey)
        dialog.show()
    }

    private fun installHandlers(preferenceKey: String) {
        btnOk.setOnSingleClickListener {
            val sliderValue: Int = slider.value.toInt()
            PrefManager.save(context, preferenceKey, sliderValue)

            BackupFilesLimiter.deleteExtraFiles(context)

            callback?.onChangeNumberBackups(sliderValue)
            dialog.dismiss()
        }
    }

    interface CallbackNumberBackups {
        fun onChangeNumberBackups(number: Int)
    }

    companion object {
        private var callback: CallbackNumberBackups? = null

        fun listenerCallback(callback: CallbackNumberBackups) {
            Companion.callback = callback
        }
    }
}


