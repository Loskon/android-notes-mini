package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.Button
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref
import com.loskon.noteminimalism3.backup.second.BackupLimiter
import com.loskon.noteminimalism3.ui.fragments.SettingsFragment
import com.loskon.noteminimalism3.utils.setColorSlider
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Нижнее меню с слайдером
 */

class SheetPrefSlider(
    private val context: Context,
    private val fragment: SettingsFragment
) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_pref_slider, null)

    private val slider: Slider = view.findViewById(R.id.slider_range)
    private val btnOk: Button = sheetDialog.getButtonOk

    init {
        setupColorViews()
        configViews()
    }

    private fun setupColorViews() {
        val color = MyColor.getMyColor(context)
        slider.setColorSlider(color)
    }

    private fun configViews() {
        sheetDialog.setInsertView(view)
    }

    fun show(keyTitle: String, value: Int) {
        sheetDialog.setTextTitle(keyTitle)
        slider.value = value.toFloat()

        installHandlers(keyTitle)

        sheetDialog.show()
    }

    private fun installHandlers(keyTitle: String) {
        btnOk.setOnSingleClickListener {
            val sliderValue = slider.value.toInt()
            MySharedPref.setInt(context, keyTitle, sliderValue)

            if (keyTitle == context.getString(R.string.num_of_backup_title)) {
                BackupLimiter.delExtraFiles(context)
            }

            fragment.setSummaryPreferences()
            sheetDialog.dismiss()
        }
    }
}