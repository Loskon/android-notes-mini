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
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.utils.setLayoutParams
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 *
 */

class BaseSheetDialog(context: Context) :
    BottomSheetDialog(context, R.style.BottomSheetBackground) {

    private val sheetContext: Context = context
    private val sheetBehavior: BottomSheetBehavior<FrameLayout> = behavior

    private val sheetView: View = View.inflate(sheetContext, R.layout.dialog_base, null)
    private val textTitle: TextView = sheetView.findViewById(R.id.tv_base_title)
    private val linearLayout: LinearLayout = sheetView.findViewById(R.id.sheet_container)
    private val btnOk: MaterialButton = sheetView.findViewById(R.id.btn_baset_ok)
    private val btnCancel: MaterialButton = sheetView.findViewById(R.id.btn_base_cancel)

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
        val color: Int = MyColor.getMyColor(sheetContext)
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
        insertView.setLayoutParams()
        if (insertView.parent != null) linearLayout.removeView(insertView)
        linearLayout.addView(insertView)
    }

    fun setTextTitle(title: String) {
        textTitle.text = title
    }

    fun setTextBtnCancel(titleCancel: String) {
        btnCancel.text = titleCancel
    }

    fun setTextTitleVisibility(isVisible: Boolean) {
        textTitle.setVisibleView(isVisible)
    }

    fun setBtnOkVisibility(isVisible: Boolean) {
        btnOk.setVisibleView(isVisible)
    }

    fun setBtnCancelVisibility(isVisible: Boolean) {
        btnCancel.setVisibleView(isVisible)
    }

    fun setContainerVisibility(isVisible: Boolean) {
        linearLayout.setVisibleView(isVisible)
    }

    val getButtonOk: MaterialButton
        get() {
            return btnOk
        }
}