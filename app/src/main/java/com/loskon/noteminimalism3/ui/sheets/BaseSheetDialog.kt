package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setLayoutParams
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Единая форма для нижнего диалогового окна
 */

class BaseSheetDialog(private val sheetContext: Context) :
    BottomSheetDialog(sheetContext, R.style.SheetDialogRounded) {

    private val sheetBehavior: BottomSheetBehavior<FrameLayout> = behavior
    private val contentView: View = View.inflate(sheetContext, R.layout.base_dialog, null)

    private val tvTitle: TextView = contentView.findViewById(R.id.tv_base_dialog_title)
    private val linLayout: LinearLayout = contentView.findViewById(R.id.container_base_dialog)
    private val btnOk: MaterialButton = contentView.findViewById(R.id.btn_base_dialog_ok)
    private val btnCancel: MaterialButton = contentView.findViewById(R.id.btn_base_dialog_cancel)

    init {
        settingsBehavior()
        establishColorViews()
        installHandlers()
    }

    private fun settingsBehavior() {
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        sheetBehavior.isDraggable = false
        sheetBehavior.isHideable = false
    }

    private fun establishColorViews() {
        val color: Int = PrefManager.getAppColor(sheetContext)
        btnOk.setBackgroundColor(color)
        btnCancel.setTextColor(color)
    }

    private fun installHandlers() {
        btnCancel.setOnSingleClickListener { dismiss() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
    }

    fun setInsertView(insertView: View) {
        insertView.setLayoutParams()
        if (insertView.parent != null) linLayout.removeView(insertView)
        linLayout.addView(insertView)
    }

    // Внешние методы
    fun setTextTitle(stringId: Int) {
        tvTitle.text = sheetContext.getString(stringId)
    }

    fun setTextTitle(string: String) {
        tvTitle.text = string
    }

    fun setTextBtnOk(stringId: Int) {
        btnOk.text = sheetContext.getString(stringId)
    }

    fun setTextBtnCancel(stringId: Int) {
        btnCancel.text = sheetContext.getString(stringId)
    }

    fun setTitleVisibility(isVisible: Boolean) {
        tvTitle.setVisibleView(isVisible)
    }

    fun setContainerVisibility(isVisible: Boolean) {
        linLayout.setVisibleView(isVisible)
    }

    fun setBtnOkVisibility(isVisible: Boolean) {
        btnOk.setVisibleView(isVisible)
    }

    fun setBtnCancelVisibility(isVisible: Boolean) {
        btnCancel.setVisibleView(isVisible)
    }

    val buttonOk: MaterialButton
        get() {
            return btnOk
        }

    val buttonCancel: MaterialButton
        get() {
            return btnCancel
        }
}