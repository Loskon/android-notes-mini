package com.loskon.noteminimalism3.ui.sheets.update

import android.content.Context
import android.view.View
import android.widget.Button
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.files.BackupFilesLimiter
import com.loskon.noteminimalism3.permissions.PermissionsStorageUpdate
import com.loskon.noteminimalism3.ui.sheets.BaseSheetDialog
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setSliderColor

/**
 * Нижнее меню с слайдером
 */

class SheetPrefSliderUpdate(private val context: Context) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_pref_slider, null)

    private val slider: Slider = view.findViewById(R.id.slider_range)
    private val btnOk: Button = sheetDialog.buttonOk

    init {
        setupColorViews()
        configViews()
    }

    private fun setupColorViews() {
        val color = MyColor.getMyColor(context)
        slider.setSliderColor(color)
    }

    private fun configViews() {
        sheetDialog.setInsertView(view)
    }

    fun show(preferenceKey: String, value: Int) {
        sheetDialog.setTextTitle(preferenceKey)
        slider.value = value.toFloat()

        installHandlers(preferenceKey)

        sheetDialog.show()
    }

    private fun installHandlers(preferenceKey: String) {
        btnOk.setOnSingleClickListener {
            val sliderValue = slider.value.toInt()
            AppPref.save(context, preferenceKey, sliderValue)

            if (preferenceKey == context.getString(R.string.num_of_backup_title)) {
                if (PermissionsStorageUpdate.hasAccessStorage(context)) {
                    BackupFilesLimiter.deleteExtraFiles(context)
                }
            }

            sheetDialog.dismiss()
        }
    }
}