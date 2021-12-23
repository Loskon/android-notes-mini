package com.loskon.noteminimalism3.managers

import android.content.Context
import android.text.util.Linkify
import com.loskon.noteminimalism3.sharedpref.PrefHelper

/**
 * Получение активных гиперссылок
 */

class LinksManager {
    companion object {

        fun getActiveLinks(context: Context): Int {
            return getWebLinks(context) or getMailLinks(context) or getPhoneLinks(context)
        }

        private fun getWebLinks(context: Context): Int {
            return if (PrefHelper.isWeb(context)) {
                Linkify.WEB_URLS
            } else {
                0
            }
        }

        private fun getMailLinks(context: Context): Int {
            return if (PrefHelper.isMail(context)) {
                Linkify.EMAIL_ADDRESSES
            } else {
                0
            }
        }

        private fun getPhoneLinks(context: Context): Int {
            return if (PrefHelper.isPhone(context)) {
                Linkify.PHONE_NUMBERS
            } else {
                0
            }
        }
    }
}