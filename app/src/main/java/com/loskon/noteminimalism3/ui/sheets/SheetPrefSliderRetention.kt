package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.Button
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setSliderColor
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Нижнее диалоговое меню со слайдером для выбора ограничения времени хранения заметок в корзине
 */

class SheetPrefSliderRetention(private val context: Context) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val insertView = View.inflate(context, R.layout.sheet_pref_slider, null)

    private val slider: Slider = insertView.findViewById(R.id.slider_range)
    private val btnOk: Button = sheetDialog.buttonOk

    init {
        sheetDialog.setInsertView(insertView)
        sheetDialog.setTextTitle(R.string.retention_trash_title)
    }

    fun show() {
        establishColorViews()
        configViews()
        sheetDialog.show()
    }

    private fun establishColorViews() {
        val color = PrefManager.getAppColor(context)
        slider.setSliderColor(color)
    }

    private fun configViews() {
        val preKey: String = context.getString(R.string.retention_trash_key)
        val value: Int = PrefManager.getRetentionRange(context)

        slider.value = value.toFloat()

        installHandlers(preKey)
    }

    private fun installHandlers(prefKey: String) {
        btnOk.setOnSingleClickListener {
            val sliderValue: Int = slider.value.toInt()
            PrefManager.save(context, prefKey, sliderValue)

            callback?.onChangeRetention(sliderValue)
            sheetDialog.dismiss()
        }
    }

    interface CallbackRetention {
        fun onChangeRetention(range: Int)
    }

    companion object {
        private var callback: CallbackRetention? = null

        fun listenerCallback(callback: CallbackRetention) {
            Companion.callback = callback
        }
    }
}