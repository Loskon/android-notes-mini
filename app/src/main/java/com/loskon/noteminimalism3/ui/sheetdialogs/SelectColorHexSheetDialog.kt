package com.loskon.noteminimalism3.ui.sheetdialogs

import android.text.InputFilter
import android.text.Spanned
import androidx.core.widget.doOnTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.fragments.SettingsAppFragment
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.showKeyboard

/**
 * Окно для указания hex-кода для цвета темы приложения
 */

class SelectColorHexSheetDialog(private val fragment: SettingsAppFragment) :
    BaseSheetDialog(fragment.requireContext(), R.layout.sheet_color_hex) {

    private val inputLayout: TextInputLayout = view.findViewById(R.id.input_layout_hex)
    private val inputEditText: TextInputEditText = view.findViewById(R.id.input_edit_text_hex)
    private val btnReset: MaterialButton = view.findViewById(R.id.reset_hex_color)

    private var appColor: Int = color

    init {
        configureDialogParameters()
        configureInsertedViews()
        installHandlersForViews()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sheet_pref_color_hex_title)
    }

    private fun configureInsertedViews() {
        inputLayout.boxStrokeColor = appColor
        inputEditText.showKeyboard(context)
        inputEditText.setFilterAllowedCharacters()
        changeHexString()
    }

    private fun changeHexString() {
        inputEditText.setText(convertIntInHex(PrefHelper.getAppColor(context)))
        inputEditText.setSelection(inputEditText.editableText.length)
    }

    private fun convertIntInHex(color: Int): String {
        return if (color == -16777216) {
            "000000"
        } else {
            Integer.toHexString((color - 4278190080L).toInt())
        }
    }

    private fun installHandlersForViews() {
        inputEditText.doOnTextChanged { text, _, _, _ -> run { changeColorBox(text) } }
        inputLayout.setEndIconOnClickListener { onEndIconClick() }
        btnReset.setOnSingleClickListener { onResetBtnClick() }
        btnOk.setOnSingleClickListener { onOkBtnClick() }
    }

    private fun changeColorBox(text: CharSequence?) {
        val hexString: String = text.toString()

        appColor = if (hexString.isEmpty()) {
            convertHexInInt("000000")
        } else {
            convertHexInInt(hexString)
        }

        inputLayout.boxStrokeColor = appColor
    }

    private fun convertHexInInt(hexString: String): Int {
        return (Integer.valueOf(hexString, 16) + 4278190080L).toInt()
    }

    private fun onEndIconClick() {
        inputEditText.text?.clear()
        inputLayout.boxStrokeColor = convertHexInInt("000000")
    }

    private fun onResetBtnClick() {
        changeHexString()
        inputLayout.boxStrokeColor = color
    }

    private fun onOkBtnClick() {
        fragment.callingCallbacks(appColor)
        dismiss()
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