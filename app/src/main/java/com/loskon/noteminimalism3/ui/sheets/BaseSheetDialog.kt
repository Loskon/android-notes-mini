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
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.utils.setLayoutParams
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Единая форма для нижнего меню
 */

class BaseSheetDialog(private val sheetContext: Context) :
    BottomSheetDialog(sheetContext, R.style.BottomSheetBackground) {

    private val sheetBehavior: BottomSheetBehavior<FrameLayout> = behavior

    private val view: View = View.inflate(sheetContext, R.layout.dialog_base, null)
    private val tvTitle: TextView = view.findViewById(R.id.tv_base_title)
    private val linearLayout: LinearLayout = view.findViewById(R.id.sheet_container)
    private val btnOk: MaterialButton = view.findViewById(R.id.btn_baset_ok)
    private val btnCancel: MaterialButton = view.findViewById(R.id.btn_base_cancel)

    init {
        settingsBehavior()
        setupColorViews()
        installHandlers()
    }

    private fun settingsBehavior() {
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        sheetBehavior.isDraggable = false
        sheetBehavior.isHideable = false
    }

    private fun setupColorViews() {
        val color: Int = AppPref.getAppColor(sheetContext)
        btnOk.setBackgroundColor(color)
        btnCancel.setTextColor(color)
    }

    private fun installHandlers() {
        btnCancel.setOnSingleClickListener { dismiss() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view)
    }

    fun setInsertView(insertView: View) {
        insertView.setLayoutParams()
        if (insertView.parent != null) linearLayout.removeView(insertView)
        linearLayout.addView(insertView)
    }


    //
    fun setTextTitle(stringTitleId: Int) {
        tvTitle.text = sheetContext.getString(stringTitleId)
    }

    fun setTextTitle(title: String) {
        tvTitle.text = title
    }

    fun setTextBtnOk(stringOkId: Int) {
        btnOk.text = sheetContext.getString(stringOkId)
    }

    fun setTextBtnCancel(stringCancelId: Int) {
        btnCancel.text = sheetContext.getString(stringCancelId)
    }

    fun setTextTitleVisibility(isVisible: Boolean) {
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