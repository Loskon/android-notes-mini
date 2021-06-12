package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref
import com.loskon.noteminimalism3.utils.setColorSlider
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setTextSizeInSp

class SheetPrefFontSize(private val context: Context) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.dialog_font_size, null)

    private val textView: TextView = view.findViewById(R.id.tv_font_size_text)
    private val slider: Slider = view.findViewById(R.id.slider_font_size_note)
    private val btnReset: MaterialButton = view.findViewById(R.id.btn_font_size_reset)
    private val btnOk: Button = sheetDialog.getButtonOk

    private var fontSizeNote: Int = 0

    init {
        setupColorViews()
        configViews()
        setStateChecked()
        installHandlers()
    }

    private fun setupColorViews() {
        val color = MyColor.getMyColor(context)
        slider.setColorSlider(color)
        btnReset.strokeColor = ColorStateList.valueOf(color)
        btnReset.setTextColor(color)
    }

    private fun configViews() {
        sheetDialog.setInsertView(view)
        sheetDialog.setTextTitle(context.getString(R.string.dg_font_size_title))
    }

    private fun setStateChecked() {
        fontSizeNote = GetSharedPref.getFontSizeNote(context)
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
            sheetDialog.dismiss()
            saveResult()
        }
    }

    private fun setTextSize() {
        textView.setTextSizeInSp(fontSizeNote)
    }

    private fun saveResult() {
        MySharedPref.setInt(context, MyPrefKey.KEY_TITLE_FONT_SIZE_NOTES, fontSizeNote)
    }

    fun show() {
        sheetDialog.show()
    }
}
