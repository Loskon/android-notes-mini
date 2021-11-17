package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.showKeyboard

/**
 * Выбор основного цвета для приложения с помощью hex-кода
 */

class SheetPrefSelectColorHex(private val context: Context) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val sheetView = View.inflate(context, R.layout.sheet_pref_color_hex, null)

    private val inputLayout: TextInputLayout = sheetView.findViewById(R.id.input_layout_hex)
    private val inputEditText: TextInputEditText = sheetView.findViewById(R.id.input_edit_text_hex)
    private val btnReset: MaterialButton = sheetView.findViewById(R.id.reset_hex_color)

    private var color: Int = 0

    init {
        dialog.setInsertView(sheetView)
        dialog.setTextTitle(R.string.sheet_pref_color_hex_title)

        setupColorViews()
        configViews()
        installHandlers()
    }

    private fun setupColorViews() {
        color = PrefManager.getAppColor(context)
        inputLayout.boxStrokeColor = color
    }

    private fun configViews() {
        inputEditText.showKeyboard(context)
        setHexString()
    }

    private fun setHexString() {
        inputEditText.setText(convertIntInHex(PrefManager.getAppColor(context)))
        inputEditText.setSelection(inputEditText.editableText.length)
    }

    private fun convertIntInHex(colorInt: Int): String {
        return Integer.toHexString((colorInt - 4278190080L).toInt())
    }

    private fun installHandlers() {
        inputEditText.doOnTextChanged { text: CharSequence?, _, _, _ ->
            run {
                changeColorBoxInputLayout(text)
            }
        }

        inputLayout.setEndIconOnClickListener {
            inputEditText.text?.clear()
            inputLayout.boxStrokeColor = convertHexInInt("000000")
        }

        btnReset.setOnSingleClickListener {
            setHexString()
            inputLayout.boxStrokeColor = PrefManager.getAppColor(context)
        }

        dialog.buttonOk.setOnSingleClickListener {
            PrefManager.setAppColor(context, color)
            callingCallbacks()
            dialog.dismiss()
        }
    }

    private fun changeColorBoxInputLayout(text: CharSequence?) {
        val hexString: String = text.toString()

        color = if (hexString.isEmpty()) {
            convertHexInInt("000000")
        } else {
            convertHexInInt(hexString)
        }

        inputLayout.boxStrokeColor = color
    }

    private fun convertHexInInt(hexString: String): Int {
        return (Integer.valueOf(hexString, 16) + 4278190080L).toInt()
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
    interface CallbackColorHexNavIcon {
        fun onChangeColor(color: Int)
    }

    interface CallbackColorHexNotifyData {
        fun onChangeColor()
    }

    interface CallbackColorHexList {
        fun onChangeColor(color: Int)
    }

    companion object {
        private var callbackColorNavIcon: CallbackColorHexNavIcon? = null
        private var callbackColorNotifyData: CallbackColorHexNotifyData? = null
        private var callbackColorList: CallbackColorHexList? = null

        fun listenerCallBackColorNavIcon(callbackColorNavIcon: CallbackColorHexNavIcon) {
            this.callbackColorNavIcon = callbackColorNavIcon
        }

        fun listenerCallBackNotifyData(callbackColorNotifyData: CallbackColorHexNotifyData) {
            this.callbackColorNotifyData = callbackColorNotifyData
        }

        fun listenerCallBackColorList(callbackColorList: CallbackColorHexList) {
            this.callbackColorList = callbackColorList
        }

    }
}