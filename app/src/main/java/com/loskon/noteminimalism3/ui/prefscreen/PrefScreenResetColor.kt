package com.loskon.noteminimalism3.ui.prefscreen

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.utils.getShortColor

/**
 * Preference с callbacks
 */

class PrefScreenResetColor @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    override fun onClick() {
        super.onClick()
        val color: Int = context.getShortColor(R.color.material_blue)
        PrefHelper.setAppColor(context, color)
        callbackColorNavIcon?.onChangeColor(color)
        callbackColorNotifyData?.onChangeColor()
        callbackColorList?.onChangeColor(color)
    }

    // Callbacks
    interface ColorResetNavIconCallback {
        fun onChangeColor(color: Int)
    }

    interface ColorResetNotifyDataCallback {
        fun onChangeColor()
    }

    interface ColorResetListCallback {
        fun onChangeColor(color: Int)
    }

    companion object {
        private var callbackColorNavIcon: ColorResetNavIconCallback? = null
        private var callbackColorNotifyData: ColorResetNotifyDataCallback? = null
        private var callbackColorList: ColorResetListCallback? = null

        fun registerCallbackColorNavIcon(callbackColorNavIcon: ColorResetNavIconCallback) {
            this.callbackColorNavIcon = callbackColorNavIcon
        }

        fun registerCallbackNotifyData(callbackColorNotifyData: ColorResetNotifyDataCallback) {
            this.callbackColorNotifyData = callbackColorNotifyData
        }

        fun registerCallbackColorList(callbackColorList: ColorResetListCallback) {
            this.callbackColorList = callbackColorList
        }
    }
}