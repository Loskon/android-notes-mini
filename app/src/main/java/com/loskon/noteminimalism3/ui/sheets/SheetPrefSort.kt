package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setRadioButtonColor

/**
 * Выбор способа сортировки для списка заметок
 */

class SheetPrefSort(private val context: Context) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val sheetView = View.inflate(context, R.layout.sheet_pref_sorting, null)

    private val radioGroup: RadioGroup = sheetView.findViewById(R.id.rg_sort)
    private val radioButtonCreate: RadioButton = sheetView.findViewById(R.id.rb_sort_creation)
    private val radioButtonMod: RadioButton = sheetView.findViewById(R.id.rb_sort_modification)
    private val btnOk: Button = dialog.buttonOk

    private var checkedNumber: Int = 0

    init {
        setupColorViews()
        configViews()
        setStateChecked()
        installHandlers()
    }

    private fun setupColorViews() {
        val color: Int = PrefManager.getAppColor(context)
        radioButtonCreate.setRadioButtonColor(color)
        radioButtonMod.setRadioButtonColor(color)
    }

    private fun configViews() {
        dialog.setInsertView(sheetView)
        dialog.setTextTitle(R.string.sort_title)
    }

    private fun setStateChecked() {
        checkedNumber = PrefManager.getSortingWay(context)

        if (checkedNumber == 1) {
            radioGroup.check(R.id.rb_sort_modification)
        } else {
            radioGroup.check(R.id.rb_sort_creation)
        }
    }

    private fun installHandlers() {
        radioGroup.setOnCheckedChangeListener { _, radioButtonId: Int ->
            checkedNumber = if (radioButtonId == R.id.rb_sort_modification) {
                1
            } else {
                0
            }
        }

        btnOk.setOnSingleClickListener {
            PrefManager.setSortingWay(context, checkedNumber)
            callbackSort?.onChangeSortingWay(checkedNumber)
            dialog.dismiss()
        }
    }

    fun show() {
        dialog.show()
    }

    interface CallbackSort {
        fun onChangeSortingWay(sortingWay: Int)
    }

    companion object {
        private var callbackSort: CallbackSort? = null

        fun listenerCallback(callbackSort: CallbackSort) {
            this.callbackSort = callbackSort
        }
    }
}