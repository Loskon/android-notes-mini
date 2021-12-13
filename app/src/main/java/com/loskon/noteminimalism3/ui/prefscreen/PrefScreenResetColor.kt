package com.loskon.noteminimalism3.ui.prefscreen

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
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
        PrefManager.setAppColor(context, color)
        callbackColorNavIcon?.onChangeColor(color)
        callbackColorNotifyData?.onChangeColor()
        callbackColorList?.onChangeColor(color)
    }

    // Callbacks
    interface CallbackColorResetNavIcon {
        fun onChangeColor(color: Int)
    }

    interface CallbackColorResetNotifyData {
        fun onChangeColor()
    }

    interface CallbackColorResetList {
        fun onChangeColor(color: Int)
    }

    companion object {
        private var callbackColorNavIcon: CallbackColorResetNavIcon? = null
        private var callbackColorNotifyData: CallbackColorResetNotifyData? = null
        private var callbackColorList: CallbackColorResetList? = null

        fun listenerCallBackColorNavIcon(callbackColorNavIcon: CallbackColorResetNavIcon) {
            this.callbackColorNavIcon = callbackColorNavIcon
        }

        fun listenerCallBackNotifyData(callbackColorNotifyData: CallbackColorResetNotifyData) {
            this.callbackColorNotifyData = callbackColorNotifyData
        }

        fun listenerCallBackColorList(callbackColorList: CallbackColorResetList) {
            this.callbackColorList = callbackColorList
        }
    }
}