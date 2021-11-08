package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.Button
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setSliderColor

/**
 * Нижнее диалоговое меню со слайдером для выбора ограничения времени хранения заметок в корзине
 */

class SheetPrefSliderRetention(private val context: Context) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_pref_slider, null)

    private val slider: Slider = view.findViewById(R.id.slider_range)
    private val btnOk: Button = sheetDialog.buttonOk

    init {
        setupColorViews()
        configViews()
    }

    private fun setupColorViews() {
        val color = PrefManager.getAppColor(context)
        slider.setSliderColor(color)
    }

    private fun configViews() {
        sheetDialog.setInsertView(view)
    }

    fun show() {
        val preferenceKey: String = context.getString(R.string.retention_trash_title)
        val value: Int = PrefManager.getRetentionRange(context)

        sheetDialog.setTextTitle(preferenceKey)
        slider.value = value.toFloat()

        installHandlers(preferenceKey)

        sheetDialog.show()
    }

    private fun installHandlers(preferenceKey: String) {
        btnOk.setOnSingleClickListener {
            val sliderValue: Int = slider.value.toInt()
            PrefManager.save(context, preferenceKey, sliderValue)

            callback?.onChangeRetention(sliderValue)
            sheetDialog.dismiss()
        }
    }

    interface CallbackRetention{
        fun onChangeRetention(range: Int)
    }

    companion object {
        private var callback: CallbackRetention? = null

        fun listenerCallback(callback: CallbackRetention) {
            Companion.callback = callback
        }
    }
}