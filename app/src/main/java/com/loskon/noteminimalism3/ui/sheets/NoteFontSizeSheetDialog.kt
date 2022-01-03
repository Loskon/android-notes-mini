package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setSliderColor
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setTextSizeShort

/**
 * Изменение размер текста заметок
 */

class NoteFontSizeSheetDialog(private val context: Context) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val sheetView = View.inflate(context, R.layout.sheet_note_font_size, null)

    private val textView: TextView = sheetView.findViewById(R.id.tv_sheet_font_size)
    private val slider: Slider = sheetView.findViewById(R.id.slider_sheet_font_size)
    private val btnReset: MaterialButton = sheetView.findViewById(R.id.btn_sheet_font_size_reset)
    private val btnOk: Button = dialog.buttonOk

    private var fontSizeNote: Int = 0

    init {
        setupColorViews()
        configViews()
        setStateChecked()
        installHandlersForViews()
    }

    private fun setupColorViews() {
        val color = PrefHelper.getAppColor(context)
        slider.setSliderColor(color)
    }

    private fun configViews() {
        dialog.addInsertedView(sheetView)
        dialog.setTextTitle(R.string.sheet_font_size_title)
    }

    private fun setStateChecked() {
        fontSizeNote = PrefHelper.getNoteFontSize(context)
        slider.value = fontSizeNote.toFloat()
        setTextSize()
    }

    private fun installHandlersForViews() {
        slider.addOnChangeListener(Slider.OnChangeListener { _, value: Float, _ ->
            fontSizeNote = value.toInt()
            setTextSize()
        })

        btnReset.setOnSingleClickListener {
            fontSizeNote = 18
            setTextSize()
            slider.value = fontSizeNote.toFloat()
        }

        btnOk.setOnSingleClickListener {
            dialog.dismiss()
            saveResult()
        }
    }

    private fun setTextSize() {
        textView.setTextSizeShort(fontSizeNote)
    }

    private fun saveResult() {
        PrefHelper.setNoteFontSize(context, fontSizeNote)
    }

    fun show() {
        dialog.show()
    }
}
