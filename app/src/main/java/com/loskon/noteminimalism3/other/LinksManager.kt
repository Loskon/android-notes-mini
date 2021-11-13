package com.loskon.noteminimalism3.other

import android.content.Context
import android.text.util.Linkify
import com.loskon.noteminimalism3.sharedpref.PrefManager

/**
 * Получение активного типа ссылок
 */

class LinksManager {

    companion object {

        fun getActiveLinks(context: Context): Int {
            return getWebLinks(context) or getMailLinks(context) or getPhoneLinks(context)
        }

        private fun getWebLinks(context: Context): Int {
            return if (PrefManager.isWeb(context)) {
                Linkify.WEB_URLS
            } else {
                0
            }
        }

        private fun getMailLinks(context: Context): Int {
            return if (PrefManager.isMail(context)) {
                Linkify.EMAIL_ADDRESSES
            } else {
                0
            }
        }

        private fun getPhoneLinks(context: Context): Int {
            return if (PrefManager.isPhone(context)) {
                Linkify.PHONE_NUMBERS
            } else {
                0
            }
        }
    }
}