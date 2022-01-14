package com.loskon.noteminimalism3.ui.sheetdialogs

import com.google.android.material.button.MaterialButton
import com.larswerkman.holocolorpicker.ColorPicker
import com.larswerkman.holocolorpicker.SVBar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.fragments.SettingsAppFragment
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Окно для выбора цвета темы приложения с помощью ColorPicker
 */

class SelectColorPickerSheetDialog(private val fragment: SettingsAppFragment) :
    BaseSheetDialog(fragment.requireContext(), R.layout.sheet_color_picker) {

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
        fragment.callingCallbacks(appColor)
        dismiss()
    }
}