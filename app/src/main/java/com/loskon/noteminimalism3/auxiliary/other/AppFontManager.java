package com.loskon.noteminimalism3.auxiliary.other;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;

/**
 * Помощщник для установки шрифтов
 */

public class AppFontManager {

    public static void setFont(Context context) {
        int fontNumber = GetSharedPref.getTypeFont(context);

        if (fontNumber == R.id.rb_lato_light_font) {
            context.getTheme().applyStyle(R.style.LatoLightFont, true);
        } else if (fontNumber == R.id.rb_open_sans_light_font) {
            context.getTheme().applyStyle(R.style.OpenSansLightFont, true);
        } else if (fontNumber == R.id.rb_oswald_light_font) {
            context.getTheme().applyStyle(R.style.OswaldLightFont, true);
        } else if (fontNumber == R.id.rb_source_sans_pro_light_font) {
            context.getTheme().applyStyle(R.style.SourceSansProLightFont, true);
        } else if (fontNumber == R.id.rb_montserrat_light_font) {
            context.getTheme().applyStyle(R.style.MontserratLightFont, true);
        } else if (fontNumber == R.id.rb_roboto_condensed_light_font) {
            context.getTheme().applyStyle(R.style.RobotoCondensedLightFont, true);
        } else if (fontNumber == R.id.rb_poppins_light_font) {
            context.getTheme().applyStyle(R.style.PoppinsLightFont, true);
        } else if (fontNumber == R.id.rb_ubuntu_light_font) {
            context.getTheme().applyStyle(R.style.UbuntuLightFont, true);
        } else if (fontNumber == R.id.rb_dosis_light_font) {
            context.getTheme().applyStyle(R.style.DosisLightFont, true);
        } else if (fontNumber == R.id.rb_titillium_web_light_font) {
            context.getTheme().applyStyle(R.style.TitilliumWebLightFont, true);
        } else if (fontNumber == R.id.rb_ibm_plex_serif_light_font) {
            context.getTheme().applyStyle(R.style.IBMPlexSerifLightFont, true);
        } else if (fontNumber == R.id.rb_karla_light_font) {
            context.getTheme().applyStyle(R.style.KarlaLightFont, true);
        } else if (fontNumber == R.id.rb_marmelad_regular_font) {
            context.getTheme().applyStyle(R.style.MarmeladRegularFont, true);
        } else if (fontNumber == R.id.rb_nunito_light_font) {
            context.getTheme().applyStyle(R.style.NunitoLightFont, true);
        } else if (fontNumber == R.id.rb_alice_regular_font) {
            context.getTheme().applyStyle(R.style.AliceRegularFont, true);
        } else {
            context.getTheme().applyStyle(R.style.RobotoLightFont, true);
        }
    }

    public static Typeface setFontText(Context context, int fontNumber) {
        int font;

        if (fontNumber == R.id.rb_lato_light_font) {
            font = R.font.lato_light;
        } else if (fontNumber == R.id.rb_open_sans_light_font) {
            font = R.font.open_sans_light;
        } else if (fontNumber == R.id.rb_oswald_light_font) {
            font = R.font.oswald_light;
        } else if (fontNumber == R.id.rb_source_sans_pro_light_font) {
            font = R.font.source_sans_pro_light;
        } else if (fontNumber == R.id.rb_montserrat_light_font) {
            font = R.font.montserrat_light;
        } else if (fontNumber == R.id.rb_roboto_condensed_light_font) {
            font = R.font.roboto_condensed_light;
        } else if (fontNumber == R.id.rb_poppins_light_font) {
            font = R.font.poppins_light;
        } else if (fontNumber == R.id.rb_ubuntu_light_font) {
            font = R.font.ubuntu_light;
        } else if (fontNumber == R.id.rb_dosis_light_font) {
            font = R.font.dosis_light;
        } else if (fontNumber == R.id.rb_titillium_web_light_font) {
            font = R.font.titillium_web_light;
        } else if (fontNumber == R.id.rb_ibm_plex_serif_light_font) {
            font = R.font.ibm_plex_serif_light;
        } else if (fontNumber == R.id.rb_karla_light_font) {
            font = R.font.karla_light;
        } else if (fontNumber == R.id.rb_marmelad_regular_font) {
            font = R.font.marmelad_regular;
        } else if (fontNumber == R.id.rb_nunito_light_font) {
            font = R.font.nunito_light;
        } else if (fontNumber == R.id.rb_alice_regular_font) {
            font = R.font.alice_regular;
        } else {
            font = R.font.roboto_light;
        }

        return ResourcesCompat.getFont(context, font);
    }
}



