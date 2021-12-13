package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.Button
import com.google.android.material.button.MaterialButton
import com.larswerkman.holocolorpicker.ColorPicker
import com.larswerkman.holocolorpicker.SVBar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Выбор основного цвета для приложения
 */

class SheetPrefSelectColor(private val context: Context) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val sheetView = View.inflate(context, R.layout.sheet_pref_color_picker, null)

    private val colorPicker: ColorPicker = sheetView.findViewById(R.id.color_picker)
    private val svBar: SVBar = sheetView.findViewById(R.id.sv_color_bar)
    private val btnReset: MaterialButton = sheetView.findViewById(R.id.btn_color_picker_reset)
    private val btnOk: Button = dialog.buttonOk

    private var color: Int = 0

    init {
        configViews()
        setupColorViews()
        installHandlers()
    }

    private fun configViews() {
        dialog.setInsertView(sheetView)
        dialog.setTextTitle(R.string.select_color_app_title)
        colorPicker.addSVBar(svBar)
        colorPicker.showOldCenterColor = false
    }

    private fun setupColorViews() {
        val color: Int = PrefManager.getAppColor(context)
        colorPicker.color = color
    }

    private fun installHandlers() {
        colorPicker.onColorChangedListener =
            ColorPicker.OnColorChangedListener { color: Int ->
                this.color = color
            }

        btnOk.setOnSingleClickListener {
            PrefManager.setAppColor(context, color)
            callingCallbacks()
            dialog.dismiss()
        }

        btnReset.setOnSingleClickListener {
            colorPicker.color = PrefManager.getAppColor(context)
        }
    }

    private fun callingCallbacks() {
        callbackColorNavIcon?.onChangeColor(color)
        callbackColorNotifyData?.onChangeColor()
        callbackColorList?.onChangeColor(color)
    }

    fun show() {
        dialog.show()
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