package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setSliderColor
import com.loskon.noteminimalism3.utils.setTextSizeShort

/**
 * Изменение размер текста заметок
 */

class SheetPrefNoteFontSize(private val context: Context) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val sheetView = View.inflate(context, R.layout.sheet_pref_note_font_size, null)

    private val textView: TextView = sheetView.findViewById(R.id.sheet_font_size_tv)
    private val slider: Slider = sheetView.findViewById(R.id.sheet_font_size_slider)
    private val btnReset: MaterialButton = sheetView.findViewById(R.id.sheet_font_size_reset_btn)
    private val btnOk: Button = dialog.buttonOk

    private var fontSizeNote: Int = 0

    init {
        setupColorViews()
        configViews()
        setStateChecked()
        installHandlers()
    }

    private fun setupColorViews() {
        val color = PrefManager.getAppColor(context)
        slider.setSliderColor(color)
    }

    private fun configViews() {
        dialog.setInsertView(sheetView)
        dialog.setTextTitle(R.string.sheet_font_size_title)
    }

    private fun setStateChecked() {
        fontSizeNote = PrefManager.getFontSizeNote(context)
        slider.value = fontSizeNote.toFloat()
        setTextSize()
    }

    private fun installHandlers() {
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
        PrefManager.setFontSizeNote(context, fontSizeNote)
    }

    fun show() {
        dialog.show()
    }
}
