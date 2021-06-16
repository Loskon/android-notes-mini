package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.Button
import com.google.android.material.button.MaterialButton
import com.larswerkman.holocolorpicker.ColorPicker
import com.larswerkman.holocolorpicker.SVBar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref
import com.loskon.noteminimalism3.utils.getShortColor
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.showToast

/**
 * Выбор основного цвета для приложения
 */

class SheetPrefSelectColor(private val context: Context) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_pref_color_picker, null)

    private val colorPicker: ColorPicker = view.findViewById(R.id.color_picker)
    private val svBar: SVBar = view.findViewById(R.id.sv_bar)
    private val btnReset: MaterialButton = view.findViewById(R.id.btn_color_picker_reset)
    private val btnOk: Button = sheetDialog.getButtonOk

    private var color: Int = 0

    init {
        configViews()
        setupColorViews()
        installHandlers()
    }

    private fun configViews() {
        sheetDialog.setInsertView(view)
        sheetDialog.setTextTitle(R.string.sheet_color_picker_title)
        colorPicker.addSVBar(svBar)
        colorPicker.showOldCenterColor = false
    }

    private fun setupColorViews() {
        val color: Int = MyColor.getMyColor(context)
        btnReset.iconTint = ColorStateList.valueOf(color)
        colorPicker.color = color
    }

    private fun installHandlers() {
        colorPicker.onColorChangedListener =
            ColorPicker.OnColorChangedListener { color: Int ->
                this.color = color
            }

        btnOk.setOnSingleClickListener {
            callingWarning()
            callingCallbacks()
            sheetDialog.dismiss()
        }

        btnReset.setOnSingleClickListener {
            colorPicker.color = context.getShortColor(R.color.material_blue)
        }
    }

    private fun callingWarning() {
        if (color == -16777216 || color == -1) {
            context.showToast(R.string.bad_idea)
            color = MyColor.getMyColor(context)
        } else {
            MySharedPref.setInt(context, MyPrefKey.KEY_COLOR, color)
        }
    }

    private fun callingCallbacks() {
        callbackColorNavIcon?.onCallback(color)
        callbackColorSettingsApp?.onCallback(color)
        callbackColorNotifyData?.onCallback()
        callbackColorMain?.onCallback(color)
    }

    fun show() {
        sheetDialog.show()
    }


    // Callbacks
    interface CallbackColorNavIcon {
        fun onCallback(color: Int)
    }

    interface CallbackColorSettingsApp {
        fun onCallback(color: Int)
    }

    interface CallbackColorNotifyData {
        fun onCallback()
    }

    interface CallbackColorMain {
        fun onCallback(color: Int)
    }

    companion object {
        private var callbackColorNavIcon: CallbackColorNavIcon? = null
        private var callbackColorSettingsApp: CallbackColorSettingsApp? = null
        private var callbackColorNotifyData: CallbackColorNotifyData? = null
        private var callbackColorMain: CallbackColorMain? = null

        @JvmStatic
        fun regCallBackColorNavIcon(callbackColorNavIcon: CallbackColorNavIcon) {
            SheetPrefSelectColor.callbackColorNavIcon = callbackColorNavIcon
        }

        @JvmStatic
        fun regCallBackColorSettingsApp(callbackColorSettingsApp: CallbackColorSettingsApp) {
            SheetPrefSelectColor.callbackColorSettingsApp = callbackColorSettingsApp
        }

        @JvmStatic
        fun regCallBackColorNotifyData(callbackColorNotifyData: CallbackColorNotifyData) {
            SheetPrefSelectColor.callbackColorNotifyData = callbackColorNotifyData
        }

        @JvmStatic
        fun regCallBackColorMain(callbackColorMain: CallbackColorMain) {
            SheetPrefSelectColor.callbackColorMain = callbackColorMain
        }
    }
}