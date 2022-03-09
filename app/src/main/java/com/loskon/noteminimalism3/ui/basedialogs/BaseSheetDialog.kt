package com.loskon.noteminimalism3.ui.basedialogs

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.utils.setLayoutParamsForInsertedView
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Основа для нижних диалоговых окон
 */

open class BaseSheetDialog(
    sheetContext: Context,
    private val insertViewId: Int? = null
) :
    BottomSheetDialog(sheetContext, R.style.RoundedSheetDialog) {

    private val sheetView: View = View.inflate(context, R.layout.dialog_base, null)
    private val tvTitle: TextView = sheetView.findViewById(R.id.tv_base_dialog_title)
    private val linLayout: LinearLayout = sheetView.findViewById(R.id.container_base_dialog)
    private val buttonOk: MaterialButton = sheetView.findViewById(R.id.btn_base_dialog_ok)
    private val buttonCancel: MaterialButton = sheetView.findViewById(R.id.btn_base_dialog_cancel)

    private var appColor: Int = 0

    init {
        configureSheetBehavior()
        establishViewsColor()
        setupViewsListeners()
        addInsertedView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(sheetView)
    }

    private fun configureSheetBehavior() {
        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }
    }

    private fun establishViewsColor() {
        appColor = PrefHelper.getAppColor(context)
        buttonOk.setBackgroundColor(appColor)
        buttonCancel.setTextColor(appColor)
    }

    private fun setupViewsListeners() {
        buttonCancel.setOnSingleClickListener { dismiss() }
    }

    private fun addInsertedView() {
        if (insertViewId != null) {
            val insertView: View = View.inflate(context, insertViewId, null)
            insertView.setLayoutParamsForInsertedView()
            linLayout.addView(insertView)
        } else {
            linLayout.setVisibleView(false)
        }
    }

    //----------------------------------------------------------------------------------------------
    fun setTitleDialog(@StringRes stringId: Int) {
        tvTitle.text = context.getString(stringId)
    }

    fun setTextBtnOk(stringId: Int) {
        buttonOk.text = context.getString(stringId)
    }

    fun setTextBtnCancel(stringId: Int) {
        buttonCancel.text = context.getString(stringId)
    }

    fun setTitleVisibility(isVisible: Boolean) {
        tvTitle.setVisibleView(isVisible)
    }

    fun setContainerVisibility(isVisible: Boolean) {
        linLayout.setVisibleView(isVisible)
    }

    fun setBtnOkVisibility(isVisible: Boolean) {
        buttonOk.setVisibleView(isVisible)
    }

    fun setBtnCancelVisibility(isVisible: Boolean) {
        buttonCancel.setVisibleView(isVisible)
    }

    //----------------------------------------------------------------------------------------------
    val btnOk: MaterialButton
        get() {
            return buttonOk
        }

    val btnCancel: MaterialButton
        get() {
            return buttonCancel
        }

    val view: View
        get() {
            return sheetView
        }

    val color: Int
        get() {
            return appColor
        }
}