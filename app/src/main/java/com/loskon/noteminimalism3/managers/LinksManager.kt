package com.loskon.noteminimalism3.managers

import android.content.Context
import android.text.util.Linkify
import com.loskon.noteminimalism3.sharedpref.AppPreference

/**
 * Получение активных гиперссылок
 */

object LinksManager {

    fun getActiveLinks(context: Context): Int {
        return getWebLinks(context) or getMailLinks(context) or getPhoneLinks(context)
    }

    private fun getWebLinks(context: Context): Int {
        return if (AppPreference.isWeb(context)) {
            Linkify.WEB_URLS
        } else {
            0
        }
    }

    private fun getMailLinks(context: Context): Int {
        return if (AppPreference.isMail(context)) {
            Linkify.EMAIL_ADDRESSES
        } else {
            0
        }
    }

    private fun getPhoneLinks(context: Context): Int {
        return if (AppPreference.isPhone(context)) {
            Linkify.PHONE_NUMBERS
        } else {
            0
        }
    }
}