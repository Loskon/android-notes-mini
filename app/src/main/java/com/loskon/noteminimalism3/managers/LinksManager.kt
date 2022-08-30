package com.loskon.noteminimalism3.managers

import android.content.Context
import android.text.util.Linkify
import com.loskon.noteminimalism3.sharedpref.AppPreference

object LinksManager {

    private const val NOT_SUPPORTED = 0

    fun getActiveLinks(context: Context): Int {
        return webLinks(context) or mailLinks(context) or phoneLinks(context)
    }

    private fun webLinks(context: Context): Int {
        return if (AppPreference.isWebLink(context)) {
            Linkify.WEB_URLS
        } else {
            NOT_SUPPORTED
        }
    }

    private fun mailLinks(context: Context): Int {
        return if (AppPreference.isMailLink(context)) {
            Linkify.EMAIL_ADDRESSES
        } else {
            NOT_SUPPORTED
        }
    }

    private fun phoneLinks(context: Context): Int {
        return if (AppPreference.isPhoneLink(context)) {
            Linkify.PHONE_NUMBERS
        } else {
            NOT_SUPPORTED
        }
    }
}