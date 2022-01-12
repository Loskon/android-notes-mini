package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.showKeyboard

/**
 * Окно для указания hex-кода для цвета темы приложения
 */

class SelectColorHexSheetDialog(private val context: Context) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val insertView = View.inflate(context, R.layout.sheet_color_hex, null)

    private val inputLayout: TextInputLayout = insertView.findViewById(R.id.input_layout_hex)
    private val inputEditText: TextInputEditText = insertView.findViewById(R.id.input_edit_text_hex)
    private val btnReset: MaterialButton = insertView.findViewById(R.id.reset_hex_color)

    private var color: Int = 0

    init {
        dialog.addInsertedView(insertView)
        dialog.setTextTitle(R.string.sheet_pref_color_hex_title)
    }

    fun show() {
        establishViewsColor()
        configViews()
        installHandlersForViews()
        dialog.show()
    }

    private fun establishViewsColor() {
        color = PrefHelper.getAppColor(context)
        inputLayout.boxStrokeColor = color
    }

    private fun configViews() {
        inputEditText.showKeyboard(context)
        setHexString()
    }

    private fun setHexString() {
        inputEditText.setText(convertIntInHex(PrefHelper.getAppColor(context)))
        inputEditText.setSelection(inputEditText.editableText.length)
        inputEditText.setFilterAllowedCharacters()
    }

    private fun convertIntInHex(colorInt: Int): String {
        return Integer.toHexString((colorInt - 4278190080L).toInt())
    }

    private fun installHandlersForViews() {
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
            inputLayout.boxStrokeColor = PrefHelper.getAppColor(context)
        }

        dialog.buttonOk.setOnSingleClickListener {
            PrefHelper.setAppColor(context, color)
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

    // Callbacks
    interface ColorHexNavIconCallback {
        fun onChangeColor(color: Int)
    }

    interface ColorHexNotifyDataCallback {
        fun onChangeColor()
    }

    interface ColorHexListCallback {
        fun onChangeColor(color: Int)
    }

    companion object {
        private var callbackColorNavIcon: ColorHexNavIconCallback? = null
        private var callbackColorNotifyData: ColorHexNotifyDataCallback? = null
        private var callbackColorList: ColorHexListCallback? = null

        fun registerCallbackColorNavIcon(callbackColorNavIcon: ColorHexNavIconCallback) {
            this.callbackColorNavIcon = callbackColorNavIcon
        }

        fun registerCallbackNotifyData(callbackColorNotifyData: ColorHexNotifyDataCallback) {
            this.callbackColorNotifyData = callbackColorNotifyData
        }

        fun registerCallbackColorList(callbackColorList: ColorHexListCallback) {
            this.callbackColorList = callbackColorList
        }

    }
}

// Extension functions
private fun TextInputEditText.setFilterAllowedCharacters() {
    filters = arrayOf(object : InputFilter {
        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            return source?.subSequence(start, end)
                ?.replace(Regex("[^A-Fa-f0-9 ]"), "")
        }
    }, InputFilter.LengthFilter(6))
}