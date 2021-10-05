package com.loskon.noteminimalism3.other

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref

/**
 * Смена шрифта в приложении
 */

class AppFont {
    companion object {
        fun setFont(context: Context) {
            context.apply {
                val fontId: Int = when (AppPref.getTypeFont(this)) {
                    0 -> R.style.RobotoLightFont

                    1 -> R.style.LatoLightFont

                    2 -> R.style.OpenSansLightFont

                    3 -> R.style.OswaldLightFont

                    4 -> R.style.SourceSansProLightFont

                    5 -> R.style.MontserratLightFont

                    6 -> R.style.RobotoCondensedLightFont

                    7 -> R.style.PoppinsLightFont

                    8 -> R.style.UbuntuLightFont

                    9 -> R.style.DosisLightFont

                    10 -> R.style.TitilliumWebLightFont

                    11 -> R.style.IBMPlexSerifLightFont

                    12 -> R.style.KarlaLightFont

                    13 -> R.style.MarmeladRegularFont

                    14 -> R.style.NunitoLightFont

                    15 -> R.style.AliceRegularFont

                    else -> R.style.RobotoLightFont
                }

                theme.applyStyle(fontId, true)
            }
        }
    }
}