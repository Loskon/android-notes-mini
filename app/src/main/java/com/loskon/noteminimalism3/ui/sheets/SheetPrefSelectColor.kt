package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.Button
import com.google.android.material.button.MaterialButton
import com.larswerkman.holocolorpicker.ColorPicker
import com.larswerkman.holocolorpicker.SVBar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Выбор основного цвета для приложения
 */

class SheetPrefSelectColor(private val context: Context) {

    private val dialog: BaseSheetDialogs = BaseSheetDialogs(context)
    private val insertView = View.inflate(context, R.layout.sheet_pref_color_picker, null)

    private val colorPicker: ColorPicker = insertView.findViewById(R.id.color_picker)
    private val svBar: SVBar = insertView.findViewById(R.id.sv_color_bar)
    private val btnReset: MaterialButton = insertView.findViewById(R.id.btn_color_picker_reset)
    private val btnOk: Button = dialog.buttonOk

    private var color: Int = 0

    init {
        dialog.setInsertView(insertView)
        dialog.setTextTitle(R.string.select_color_app_title)
    }

    fun show() {
        establishColorViews()
        configViews()
        installHandlers()
        dialog.show()
    }

    private fun establishColorViews() {
        val color: Int = PrefHelper.getAppColor(context)
        colorPicker.color = color
    }

    private fun configViews() {
        colorPicker.addSVBar(svBar)
        colorPicker.showOldCenterColor = false
    }

    private fun installHandlers() {
        colorPicker.onColorChangedListener =
            ColorPicker.OnColorChangedListener { color: Int ->
                this.color = color
            }

        btnOk.setOnSingleClickListener {
            PrefHelper.setAppColor(context, color)
            callingCallbacks()
            dialog.dismiss()
        }

        btnReset.setOnSingleClickListener {
            colorPicker.color = PrefHelper.getAppColor(context)
        }
    }

    private fun callingCallbacks() {
        callbackColorNavIcon?.onChangeColor(color)
        callbackColorNotifyData?.onChangeColor()
        callbackColorList?.onChangeColor(color)
    }



    // Callbacks
    interface CallbackColorNavIcon {
        fun onChangeColor(color: Int)
    }

    interface CallbackColorNotifyData {
        fun onChangeColor()
    }

    interface CallbackColorList {
        fun onChangeColor(color: Int)
    }

    companion object {
        private var callbackColorNavIcon: CallbackColorNavIcon? = null
        private var callbackColorNotifyData: CallbackColorNotifyData? = null
        private var callbackColorList: CallbackColorList? = null

        fun listenerCallBackColorNavIcon(callbackColorNavIcon: CallbackColorNavIcon) {
            this.callbackColorNavIcon = callbackColorNavIcon
        }

        fun listenerCallBackNotifyData(callbackColorNotifyData: CallbackColorNotifyData) {
            this.callbackColorNotifyData = callbackColorNotifyData
        }

        fun listenerCallBackColorList(callbackColorList: CallbackColorList) {
            this.callbackColorList = callbackColorList
        }
    }
}