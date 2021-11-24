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
    BottomSheetDialog(sheetContext, R.style.BottomSheetDialogRounded) {

    private val sheetBehavior: BottomSheetBehavior<FrameLayout> = behavior

    private val sheetView: View = View.inflate(sheetContext, R.layout.base_dialog, null)
    private val tvTitle: TextView = sheetView.findViewById(R.id.tv_base_dialog_title)
    private val linearLayout: LinearLayout = sheetView.findViewById(R.id.container_base_dialog)
    private val btnOk: MaterialButton = sheetView.findViewById(R.id.btn_base_dialog_ok)
    private val btnCancel: MaterialButton = sheetView.findViewById(R.id.btn_base_dialog_cancel)

    init {
        settingsBehavior()
        setupColorViews()
        installHandlers()
    }

    private fun settingsBehavior() {
        sheetBehavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isDraggable = false
            isHideable = false
        }
    }

    private fun setupColorViews() {
        val color: Int = PrefManager.getAppColor(sheetContext)
        btnOk.setBackgroundColor(color)
        btnCancel.setTextColor(color)
    }

    private fun installHandlers() {
        btnCancel.setOnSingleClickListener { dismiss() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(sheetView)
    }

    fun setInsertView(insertView: View) {
        insertView.apply {
            setLayoutParams()
            if (parent != null) linearLayout.removeView(this)
            linearLayout.addView(this)
        }
    }


    //
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
        linearLayout.setVisibleView(isVisible)
    }

    fun setBtnOkVisibility(isVisible: Boolean) {
        btnOk.setVisibleView(isVisible)
    }

    fun setBtnCancelVisibility(isVisible: Boolean) {
        btnCancel.setVisibleView(isVisible)
    }


    //
    val buttonOk: MaterialButton
        get() {
            return btnOk
        }

    val buttonCancel: MaterialButton
        get() {
            return btnCancel
        }
}