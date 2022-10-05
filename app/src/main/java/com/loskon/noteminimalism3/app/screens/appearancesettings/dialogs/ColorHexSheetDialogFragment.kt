package com.loskon.noteminimalism3.app.screens.appearancesettings.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.screens.appearancesettings.AppearanceSettingsFragment
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.extension.view.setEndSelection
import com.loskon.noteminimalism3.base.extension.view.setFilterKtx
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetColorHexBinding
import com.loskon.noteminimalism3.utils.showKeyboard
import com.loskon.noteminimalism3.viewbinding.viewBinding

class ColorHexSheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetColorHexBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        val color = savedInstanceState?.getInt(PUT_SAVE_HEX_COLOR_KEY) ?: getAppColor()

        setDialogViewsParameters()
        establishViewsColor(color)
        configureViewsParameters(color)
        setupViewsListeners()
    }

    private fun setDialogViewsParameters() {
        setDialogTitle(R.string.sheet_pref_color_hex_title)
    }

    private fun establishViewsColor(color: Int) {
        binding.inputLayoutHex.boxStrokeColor = color
    }

    private fun configureViewsParameters(color: Int) {
        binding.inputEditTextHex.showKeyboard()
        binding.inputEditTextHex.setFilterKtx(pattern = "[^A-Fa-f0-9 ]", maxLength = 6)
        binding.inputEditTextHex.setText(convertIntInHex(color))
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
                val color = getSelectedColor(text.toString().trim())
                binding.inputLayoutHex.boxStrokeColor = color
            }
        }
        binding.inputLayoutHex.setEndIconOnClickListener {
            binding.inputEditTextHex.text?.clear()
            binding.inputLayoutHex.boxStrokeColor = convertHexInInt("000000")
        }
        binding.btnResetHexColor.setDebounceClickListener {
            binding.inputEditTextHex.setText(convertIntInHex(getAppColor()))
            binding.inputEditTextHex.setEndSelection()
        }
        setOkClickListener {
            val color = convertHexInInt(binding.inputEditTextHex.editableText.toString())
            val bundle = bundleOf(AppearanceSettingsFragment.SET_COLOR_REQUEST_KEY to color)

            setFragmentResult(AppearanceSettingsFragment.SET_COLOR_REQUEST_KEY, bundle)
        }
    }

    private fun getSelectedColor(hexText: String): Int {
        return if (hexText.isEmpty()) {
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
        val color = convertHexInInt(binding.inputEditTextHex.editableText.toString())
        savedInstanceState.putInt(PUT_SAVE_HEX_COLOR_KEY, color)
    }

    companion object {
        private const val PUT_SAVE_HEX_COLOR_KEY = "PUT_SAVE_HEX_COLOR_KEY"
    }
}