package com.loskon.noteminimalism3.managers

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager

/**
 * Смена шрифта в приложении
 */

class FontManager {
    companion object {

        fun setFont(context: Context) {
            context.apply {
                val fontId: Int = when (PrefManager.getTypeFont(this)) {

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

        fun getAppFont(context: Context): Typeface? {
            context.apply {
                val font: Int = when (PrefManager.getTypeFont(this)) {

                    0 -> R.font.roboto_light

                    1 -> R.font.lato_light

                    2 -> R.font.open_sans_light

                    3 -> R.font.oswald_light

                    4 -> R.font.source_sans_pro_light

                    5 -> R.font.montserrat_light

                    6 -> R.font.roboto_condensed_light

                    7 -> R.font.poppins_light

                    8 -> R.font.ubuntu_light

                    9 -> R.font.dosis_light

                    10 -> R.font.titillium_web_light

                    11 -> R.font.ibm_plex_serif_light

                    12 -> R.font.karla_light

                    13 -> R.font.marmelad_regular

                    14 -> R.font.nunito_light

                    15 -> R.font.alice_regular

                    else -> R.font.roboto_light
                }

                return ResourcesCompat.getFont(context, font)
            }
        }
    }
}