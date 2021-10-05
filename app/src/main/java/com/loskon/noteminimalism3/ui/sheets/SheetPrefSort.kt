package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setRadioButtonColor

/**
 * Выбор способа сортировки для списка заметок
 */

class SheetPrefSort(private val context: Context) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_pref_sorting, null)

    private val radioGroup: RadioGroup = view.findViewById(R.id.rg_sort)
    private val radioButtonCreate: RadioButton = view.findViewById(R.id.rb_sort_creation)
    private val radioButtonMod: RadioButton = view.findViewById(R.id.rb_sort_modification)
    private val btnOk: Button = sheetDialog.getButtonOk

    private var checkedNumber: Int = 0

    init {
        setupColorViews()
        configViews()
        setStateChecked()
        installHandlers()
    }

    private fun setupColorViews() {
        val color: Int = MyColor.getMyColor(context)
        radioButtonCreate.setRadioButtonColor(color)
        radioButtonMod.setRadioButtonColor(color)
    }

    private fun configViews() {
        sheetDialog.setInsertView(view)
        sheetDialog.setTextTitle(R.string.sort_title)
    }

    private fun setStateChecked() {
        checkedNumber = GetSharedPref.getSort(context)

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
            MySharedPref.setInt(context, MyPrefKey.KEY_SORT, checkedNumber)
            callbackSort?.onChangeSortingWay(checkedNumber)
            sheetDialog.dismiss()
        }
    }

    fun show() {
        sheetDialog.show()
    }


    // Callback
    interface CallbackSort {
        fun onChangeSortingWay(sortingWay: Int)
    }

    companion object {
        var callbackSort: CallbackSort? = null

        @JvmStatic
        fun listenerCallback(callbackSort: CallbackSort) {
            this.callbackSort = callbackSort
        }
    }
}