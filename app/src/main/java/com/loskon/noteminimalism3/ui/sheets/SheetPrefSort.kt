package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setRadioButtonColor
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Выбор способа сортировки для списка заметок
 */

class SheetPrefSort(private val context: Context) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val insertView = View.inflate(context, R.layout.sheet_pref_sorting, null)

    private val radioGroup: RadioGroup = insertView.findViewById(R.id.rg_sort)
    private val radioButtonCreate: RadioButton = insertView.findViewById(R.id.rb_sort_creation)
    private val radioButtonMod: RadioButton = insertView.findViewById(R.id.rb_sort_modification)

    private var checkedNumber: Int = 0

    init {
        dialog.setInsertView(insertView)
        dialog.setTextTitle(R.string.sort_title)
    }

    fun show() {
        establishColorViews()
        configureStateChecked()
        installHandlers()
        dialog.show()
    }

    private fun establishColorViews() {
        val color: Int = PrefManager.getAppColor(context)
        radioButtonCreate.setRadioButtonColor(color)
        radioButtonMod.setRadioButtonColor(color)
    }

    private fun configureStateChecked() {
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

        dialog.buttonOk.setOnSingleClickListener {
            PrefManager.setSortingWay(context, checkedNumber)
            callbackSort?.onChangeSortingWay(checkedNumber)
            dialog.dismiss()
        }
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