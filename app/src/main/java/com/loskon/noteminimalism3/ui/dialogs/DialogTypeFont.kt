package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.AppFontManager
import com.loskon.noteminimalism3.auxiliary.other.MyIntent
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref

/**
 * Выбор шрифта для приложения
 */

class DialogTypeFont(private val context: Context) {

    private val materialDialog: BaseMaterialDialog = BaseMaterialDialog(context)
    private val dialogView = View.inflate(context, R.layout.dialog_type_font, null)

    private val textView: TextView = dialogView.findViewById(R.id.tv_font_example)
    private val btnOk: Button = materialDialog.buttonOk

    init {
        materialDialog.setTextTitle(context.getString(R.string.dg_type_font_title))
    }

    fun show(checkedId: Int) {
        setupViews(checkedId)
        installHandlers(checkedId)
        materialDialog.show(dialogView)
    }

    private fun installHandlers(checkedId: Int) {
        val typeface = AppFontManager.setFontText(context, checkedId)
        textView.typeface = typeface
    }

    private fun setupViews(checkedId: Int) {
        btnOk.setOnClickListener {
            MySharedPref.setInt(context, MyPrefKey.KEY_TYPE_FONT, checkedId)
            callbackTypeFont?.onChangeFont()
            MyIntent.goSettingsActivityClear(context)
            materialDialog.dismiss()
        }

    }


    // Callback
    interface CallbackTypeFont {
        fun onChangeFont()
    }

    companion object {
        var callbackTypeFont: CallbackTypeFont? = null

        @JvmStatic
        fun listenerCallBack(callbackTypeFont: CallbackTypeFont) {
            DialogTypeFont.callbackTypeFont = callbackTypeFont
        }
    }
}