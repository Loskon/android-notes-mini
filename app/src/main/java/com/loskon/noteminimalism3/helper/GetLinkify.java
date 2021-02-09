package com.loskon.noteminimalism3.helper;

import android.app.Activity;
import android.text.util.Linkify;

import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;

public class GetLinkify {

    public static int Web(Activity activity) {
        if (GetSharedPref.isWeb(activity)) {
            return Linkify.WEB_URLS;
        } else {
            return 0;
        }
    }

    public static int Mail(Activity activity) {
        if (GetSharedPref.isMail(activity)) {
            return Linkify.EMAIL_ADDRESSES;
        } else {
            return 0;
        }
    }

    public static int Phone(Activity activity) {
        if (GetSharedPref.isPhone(activity)) {
            return Linkify.PHONE_NUMBERS;
        } else {
            return 0;
        }
    }
}