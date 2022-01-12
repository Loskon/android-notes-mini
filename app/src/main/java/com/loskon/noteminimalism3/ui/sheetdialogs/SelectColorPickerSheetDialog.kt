package com.loskon.noteminimalism3.ui.sheetdialogs

import android.content.Context
import com.google.android.material.button.MaterialButton
import com.larswerkman.holocolorpicker.ColorPicker
import com.larswerkman.holocolorpicker.SVBar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Окно для выбора цвета темы приложения с помощью ColorPicker
 */

class SelectColorPickerSheetDialog(sheetContext: Context) :
    BaseSheetDialog(sheetContext, R.layout.sheet_color_picker) {

    private val colorPicker: ColorPicker = view.findViewById(R.id.color_picker)
    private val svBar: SVBar = view.findViewById(R.id.sv_color_bar)
    private val btnReset: MaterialButton = view.findViewById(R.id.btn_color_picker_reset)

    private var appColor: Int = 0

    init {
        configureDialogParameters()
        configureInsertedViews()
        installHandlersForViews()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.select_color_app_title)
    }

    private fun configureInsertedViews() {
        colorPicker.addSVBar(svBar)
        colorPicker.showOldCenterColor = false
        colorPicker.color = color
    }

    private fun installHandlersForViews() {
        colorPicker.setOnColorChangedListener { appColor = it }
        btnOk.setOnSingleClickListener { onOkBtnClick() }
        btnReset.setOnSingleClickListener { colorPicker.color = color }
    }

    private fun onOkBtnClick() {
        PrefHelper.setAppColor(context, appColor)
        callingCallbacks()
        dismiss()
    }

    private fun callingCallbacks() {
        callbackColorNavIcon?.onChangeColor(appColor)
        callbackColorNotifyData?.onChangeColor()
        callbackColorList?.onChangeColor(appColor)
    }

    //--- interface --------------------------------------------------------------------------------
    interface ColorNavIconCallback {
        fun onChangeColor(color: Int)
    }

    interface ColorNotifyDataCallback {
        fun onChangeColor()
    }

    interface ColorListCallback {
        fun onChangeColor(color: Int)
    }

    companion object {
        private var callbackColorNavIcon: ColorNavIconCallback? = null
        private var callbackColorNotifyData: ColorNotifyDataCallback? = null
        private var callbackColorList: ColorListCallback? = null

        fun registerCallbackColorNavIcon(callbackColorNavIcon: ColorNavIconCallback) {
            this.callbackColorNavIcon = callbackColorNavIcon
        }

        fun registerCallbackNotifyData(callbackColorNotifyData: ColorNotifyDataCallback) {
            this.callbackColorNotifyData = callbackColorNotifyData
        }

        fun registerCallbackColorList(callbackColorList: ColorListCallback) {
            this.callbackColorList = callbackColorList
        }
    }
}