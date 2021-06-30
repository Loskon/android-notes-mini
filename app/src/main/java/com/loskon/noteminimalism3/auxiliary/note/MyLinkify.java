package com.loskon.noteminimalism3.auxiliary.note;

import android.content.Context;
import android.text.util.Linkify;

import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;

/**
 * Определяет типы гиперссылок, которые пользователь выбрал в настройках
 */

public class MyLinkify {

    public static int Web(Context context) {
        if (GetSharedPref.isWeb(context)) {
            return Linkify.WEB_URLS;
        } else {
            return 0;
        }
    }

    public static int Mail(Context context) {
        if (GetSharedPref.isMail(context)) {
            return Linkify.EMAIL_ADDRESSES;
        } else {
            return 0;
        }
    }

    public static int Phone(Context context) {
        if (GetSharedPref.isPhone(context)) {
            return Linkify.PHONE_NUMBERS;
        } else {
            return 0;
        }
    }

    public static int getTypeLinks(Context context) {
        return Web(context) | Mail(context) | Phone(context);
    }
}
