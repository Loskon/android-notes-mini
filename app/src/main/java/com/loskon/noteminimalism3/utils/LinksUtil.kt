package com.loskon.noteminimalism3.utils

import android.content.Context
import android.text.util.Linkify
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref

/**
 *
 */

class LinksUtil {
    companion object {

        fun getActiveLinks(context: Context): Int {
            return getWeb(context) or getMail(context) or getPhone(context)
        }

        private fun getWeb(context: Context): Int {
            return if (GetSharedPref.isWeb(context)) {
                Linkify.WEB_URLS
            } else {
                0
            }
        }

        private fun getMail(context: Context): Int {
            return if (GetSharedPref.isMail(context)) {
                Linkify.EMAIL_ADDRESSES
            } else {
                0
            }
        }

        private fun getPhone(context: Context): Int {
            return if (GetSharedPref.isPhone(context)) {
                Linkify.PHONE_NUMBERS
            } else {
                0
            }
        }
    }
}