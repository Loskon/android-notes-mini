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
 *
 */

class SheetPrefSelectColor(private val context: Context) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.dialog_pref_color_picker, null)

    private val colorPickerView: ColorPicker = view.findViewById(R.id.holo_picker)
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
        sheetDialog.setTextTitle(context.getString(R.string.select_color_title))
        colorPickerView.addSVBar(svBar)
        colorPickerView.showOldCenterColor = false
    }

    private fun setupColorViews() {
        val color: Int = MyColor.getMyColor(context)
        btnReset.iconTint = ColorStateList.valueOf(color)
        colorPickerView.color = color
    }

    private fun installHandlers() {
        colorPickerView.onColorChangedListener =
            ColorPicker.OnColorChangedListener { color: Int ->
                this.color = color
            }

        btnOk.setOnSingleClickListener {
            callingWarning()
            callingCallbacks()
            sheetDialog.dismiss()
        }

        btnReset.setOnSingleClickListener {
            colorPickerView.color = context.getShortColor(R.color.material_blue)
        }
    }

    private fun callingWarning() {
        if (color == -16777216 || color == -1) {
            val message: String = context.getString(R.string.bad_idea)
            context.showToast(message)
            color = MyColor.getMyColor(context)
        } else {
            MySharedPref.setInt(context, MyPrefKey.KEY_COLOR, color)
        }
    }

    private fun callingCallbacks() {
        callbackColorNavIcon?.onCallBackNavIcon(color)
        callbackColorSettingsApp?.onCallBackSettingsApp(color)
        callbackColorNotifyData?.onCallBackNotifyData()
        callbackColorMain?.onCallBackMain(color)
    }

    fun show() {
        sheetDialog.show()
    }


    // Callbacks
    interface CallbackColorNavIcon {
        fun onCallBackNavIcon(color: Int)
    }

    interface CallbackColorSettingsApp {
        fun onCallBackSettingsApp(color: Int)
    }

    interface CallbackColorNotifyData {
        fun onCallBackNotifyData()
    }

    interface CallbackColorMain {
        fun onCallBackMain(color: Int)
    }

    companion object {
        private var callbackColorNavIcon: CallbackColorNavIcon? = null
        private var callbackColorSettingsApp: CallbackColorSettingsApp? = null
        private var callbackColorNotifyData: CallbackColorNotifyData? = null
        private var callbackColorMain: CallbackColorMain? = null

        @JvmStatic
        fun regCallBackNavIcon2(callbackColorNavIcon: CallbackColorNavIcon) {
            SheetPrefSelectColor.callbackColorNavIcon = callbackColorNavIcon
        }

        @JvmStatic
        fun regCallBackSettingsApp2(callbackColorSettingsApp: CallbackColorSettingsApp) {
            SheetPrefSelectColor.callbackColorSettingsApp = callbackColorSettingsApp
        }

        @JvmStatic
        fun regCallBackNotifyData2(callbackColorNotifyData: CallbackColorNotifyData) {
            SheetPrefSelectColor.callbackColorNotifyData = callbackColorNotifyData
        }

        @JvmStatic
        fun regCallbackMain2(callbackColorMain: CallbackColorMain) {
            SheetPrefSelectColor.callbackColorMain = callbackColorMain
        }
    }
}