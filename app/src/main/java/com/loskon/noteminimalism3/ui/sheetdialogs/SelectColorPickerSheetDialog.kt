package com.loskon.noteminimalism3.ui.sheetdialogs

import com.google.android.material.button.MaterialButton
import com.larswerkman.holocolorpicker.ColorPicker
import com.larswerkman.holocolorpicker.SVBar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.fragments.AppearanceSettingsFragment
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener

/**
 * Окно для выбора цвета темы приложения с помощью ColorPicker
 */

class SelectColorPickerSheetDialog(private val fragment: AppearanceSettingsFragment) :
    BaseSheetDialog(fragment.requireContext(), R.layout.sheet_color_picker) {

    private val colorPicker: ColorPicker = view.findViewById(R.id.color_picker)
    private val svBar: SVBar = view.findViewById(R.id.sv_color_bar)
    private val btnReset: MaterialButton = view.findViewById(R.id.btn_color_picker_reset)

    private var appColor: Int = color

    init {
        configureDialogParameters()
        configureInsertedViews()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.select_color_app_title)
    }

    private fun configureInsertedViews() {
        colorPicker.addSVBar(svBar)
        colorPicker.showOldCenterColor = false
        colorPicker.color = appColor
    }

    private fun setupViewsListeners() {
        colorPicker.setOnColorChangedListener { appColor = it }
        btnOk.setDebounceClickListener { onOkBtnClick() }
        btnReset.setDebounceClickListener { colorPicker.color = color }
    }

    private fun onOkBtnClick() {
        fragment.callingCallbacks(appColor)
        dismiss()
    }
}