package com.loskon.noteminimalism3.app.screens.appearancesettings

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.screens.appearancesettings.AppearanceSettingsFragment.Companion.SET_COLOR_REQUEST_KEY
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.extension.view.setEndSelection
import com.loskon.noteminimalism3.base.extension.view.setFilterKtx
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetColorHexBinding
import com.loskon.noteminimalism3.utils.showKeyboard
import com.loskon.noteminimalism3.viewbinding.viewBinding

class ColorHexSheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetColorHexBinding::inflate)

    private var selectedColor: Int = Color.TRANSPARENT

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        setSavedArguments(savedInstanceState)
        setupViewsParameters()
        establishViewsColor()
        configureInsertedViews()
        setupViewsListeners()
    }

    private fun setSavedArguments(savedInstanceState: Bundle?) {
        selectedColor = savedInstanceState?.getInt(PUT_KEY_SAVE_HEX_COLOR) ?: color
    }

    private fun setupViewsParameters() {
        setDialogTitle(R.string.sheet_pref_color_hex_title)
    }

    private fun establishViewsColor() {
        binding.inputLayoutHex.boxStrokeColor = selectedColor
    }

    private fun configureInsertedViews() {
        binding.inputEditTextHex.showKeyboard()
        binding.inputEditTextHex.setFilterKtx(pattern = "[^A-Fa-f0-9 ]", maxLength = 6)
        binding.inputEditTextHex.setText(convertIntInHex(selectedColor))
        binding.inputEditTextHex.setEndSelection()
    }

    private fun convertIntInHex(color: Int): String {
        return if (color == -16777216) {
            "000000"
        } else {
            Integer.toHexString((color - 4278190080L).toInt())
        }
    }

    private fun setupViewsListeners() {
        binding.inputEditTextHex.doOnTextChanged { text, _, _, _ ->
            run {
                changeSelectedColor(text.toString().trim())
                binding.inputLayoutHex.boxStrokeColor = selectedColor
            }
        }
        binding.inputLayoutHex.setEndIconOnClickListener {
            binding.inputEditTextHex.text?.clear()
            binding.inputLayoutHex.boxStrokeColor = convertHexInInt("000000")
        }
        binding.btnResetHexColor.setDebounceClickListener {
            binding.inputEditTextHex.setText(convertIntInHex(color))
            binding.inputEditTextHex.setEndSelection()
        }
        setOkClickListener {
            setFragmentResult(SET_COLOR_REQUEST_KEY, bundleOf(SET_COLOR_REQUEST_KEY to selectedColor))
            dismiss()
        }
    }

    private fun changeSelectedColor(hexText: String) {
        selectedColor = if (hexText.isEmpty()) {
            convertHexInInt("000000")
        } else {
            convertHexInInt(hexText)
        }
    }

    private fun convertHexInInt(hexString: String): Int {
        return (Integer.valueOf(hexString, 16) + 4278190080L).toInt()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(PUT_KEY_SAVE_HEX_COLOR, selectedColor)
    }

    companion object {
        private const val PUT_KEY_SAVE_HEX_COLOR = "PUT_KEY_SAVE_HEX_COLOR"
    }
}