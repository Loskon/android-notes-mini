package com.loskon.noteminimalism3.ui.sheetdialogs

import android.content.Context
import android.widget.RadioButton
import android.widget.RadioGroup
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.setColorKtx
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener

/**
 * Окно для выбора способа сортировки заметок
 */

class SortWaySheetDialog(sheetContext: Context) :
    BaseSheetDialog(sheetContext, R.layout.sheet_sort_way) {

    private val radioGroup: RadioGroup = view.findViewById(R.id.rg_sort)
    private val radioButtonCreate: RadioButton = view.findViewById(R.id.rb_sort_creation)
    private val radioButtonMod: RadioButton = view.findViewById(R.id.rb_sort_modification)

    private var checkedNumber: Int = 0

    init {
        configureDialogParameters()
        establishViewsColor()
        configureInsertedViews()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sort_title)
    }

    private fun establishViewsColor() {
        radioButtonCreate.setColorKtx(color)
        radioButtonMod.setColorKtx(color)
    }

    private fun configureInsertedViews() {
        checkedNumber = AppPreference.getSortingWay(context)

        if (checkedNumber == 1) {
            radioGroup.check(R.id.rb_sort_modification)
        } else {
            radioGroup.check(R.id.rb_sort_creation)
        }
    }

    private fun setupViewsListeners() {
        radioGroup.setShortOnCheckedChangeListener()
        btnOk.setDebounceClickListener { onOkBtnClick() }
    }

    private fun RadioGroup.setShortOnCheckedChangeListener() {
        setOnCheckedChangeListener { _, radioButtonId: Int ->
            checkedNumber = if (radioButtonId == R.id.rb_sort_modification) {
                1
            } else {
                0
            }
        }
    }

    private fun onOkBtnClick() {
        AppPreference.setSortingWay(context, checkedNumber)
        callbackSort?.onChangeSortingWay(checkedNumber)
        dismiss()
    }

    //--- interface --------------------------------------------------------------------------------
    interface SortWayCallback {
        fun onChangeSortingWay(sortingWay: Int)
    }

    companion object {
        private var callbackSort: SortWayCallback? = null

        fun registerSortWayCallback(callbackSort: SortWayCallback) {
            this.callbackSort = callbackSort
        }
    }
}