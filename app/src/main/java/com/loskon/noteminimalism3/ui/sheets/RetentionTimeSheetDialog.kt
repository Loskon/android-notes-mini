package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.Button
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setSliderColor
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Нижнее диалоговое меню со слайдером для выбора ограничения времени хранения заметок в корзине
 */

class RetentionTimeSheetDialog(private val context: Context) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val insertView = View.inflate(context, R.layout.sheet_slider, null)

    private val slider: Slider = insertView.findViewById(R.id.slider_range)
    private val btnOk: Button = dialog.buttonOk

    init {
        dialog.addInsertedView(insertView)
        dialog.setTextTitle(R.string.retention_trash_title)
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
        val preKey: String = context.getString(R.string.retention_trash_key)
        val value: Int = PrefHelper.getRetentionRange(context)

        slider.value = value.toFloat()

        installHandlersForViews(preKey)
    }

    private fun installHandlersForViews(prefKey: String) {
        btnOk.setOnSingleClickListener {
            val sliderValue: Int = slider.value.toInt()
            PrefHelper.save(context, prefKey, sliderValue)

            callback?.onChangeRetention(sliderValue)
            dialog.dismiss()
        }
    }

    interface RetentionTimeCallback {
        fun onChangeRetention(range: Int)
    }

    companion object {
        private var callback: RetentionTimeCallback? = null

        fun registerCallbackRetentionTime(callback: RetentionTimeCallback) {
            Companion.callback = callback
        }
    }
}