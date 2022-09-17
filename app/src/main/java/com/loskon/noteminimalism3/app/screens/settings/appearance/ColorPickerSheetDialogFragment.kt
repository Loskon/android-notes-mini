package com.loskon.noteminimalism3.app.screens.settings.appearance

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetColorPickerBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding

class ColorPickerSheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetColorPickerBinding::inflate)

    private var selectedColor: Int = Color.TRANSPARENT

    private var handleSelectedColor: ((Int) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        getSavedArguments(savedInstanceState)
        setupViewsParameters()
        establishViewsColor()
        configureInsertedViews()
        setupViewsListeners()
    }

    private fun getSavedArguments(savedInstanceState: Bundle?) {
        selectedColor = savedInstanceState?.getInt(PUT_KEY_SAVE_COLOR) ?: color
    }

    private fun setupViewsParameters() {
        setDialogTitle(R.string.select_color_app_title)
    }

    private fun establishViewsColor() {
        binding.colorPicker.color = selectedColor
    }

    private fun configureInsertedViews() {
        binding.colorPicker.addSVBar(binding.svColorBar)
        binding.colorPicker.showOldCenterColor = false
    }

    private fun setupViewsListeners() {
        binding.colorPicker.setOnColorChangedListener { color ->
            selectedColor = color
        }
        binding.btnColorPickerReset.setDebounceClickListener {
            binding.colorPicker.color = color
        }
        setOkClickListener {
            handleSelectedColor?.invoke(selectedColor)
            dismiss()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(PUT_KEY_SAVE_COLOR, selectedColor)
    }

    fun setHandleSelectedColorListener(handleSelectedColor: ((Int) -> Unit)?) {
        this.handleSelectedColor = handleSelectedColor
    }

    companion object {
        const val TAG = "ColorPickerSheetDialogFragment"
        private const val PUT_KEY_SAVE_COLOR = "PUT_KEY_SAVE_COLOR"
        fun newInstance() = ColorPickerSheetDialogFragment()
    }
}