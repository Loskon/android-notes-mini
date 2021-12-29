package com.loskon.noteminimalism3.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.ui.fragments.NoteFragment
import com.loskon.noteminimalism3.ui.snackbars.SnackbarControl
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import java.util.regex.Pattern

/**
 * Работа с гиперссылками в заметке
 */

private const val URL_WEB = "WEB"
private const val URL_MAIL = "MAIL"
private const val URL_PHONE = "PHONE"
private const val URL_ERROR = "ERROR"

class NoteLinksDialog(private val context: Context, private val fragment: NoteFragment) {

    private val dialog: BaseDialog = BaseDialog(context)
    private val insertView = View.inflate(context, R.layout.dialog_open_link, null)

    private val btnOpen: Button = insertView.findViewById(R.id.btn_link_open)
    private val btnCopy: Button = insertView.findViewById(R.id.btn_link_copy)

    private var receivedLink: String = ""
    private var linkToOpen: String = ""
    private var typeLink: String = ""

    init {
        dialog.setBtnOkVisibility(false)
        dialog.setBtnCancelVisibility(false)
    }

    fun show(receivedLink: String) {
        this.receivedLink = receivedLink
        this.linkToOpen = receivedLink

        configInsertedViews()
        installHandlers()
        dialog.show(insertView)
    }

    private fun configInsertedViews() {
        typeLink = getTypeLink()

        when (typeLink) {
            URL_MAIL -> {
                replaceTitleText("mailto:")
                setTextButton(R.string.dg_open_link_send)
            }
            URL_PHONE -> {
                replaceTitleText("tel:")
                setTextButton(R.string.dg_open_link_call)
            }
            URL_WEB -> {
                setTextButton(R.string.dg_open_link_open)
            }

            URL_ERROR -> {
                setTextButton(R.string.dg_open_link_invalid)
            }
        }

        dialog.setTextTitle(receivedLink)
    }

    private fun getTypeLink(): String {
        var foundLinkType: String = URL_ERROR

        val regexWebS = ".*https://.*"
        val regexWeb = ".*http://.*"
        val regexMail = ".*mailto:.*"
        val regexPhone = ".*tel:.*"

        val matchesWebS: Boolean = Pattern.matches(regexWebS, receivedLink)
        val matchesWeb: Boolean = Pattern.matches(regexWeb, receivedLink)
        val matchesMail: Boolean = Pattern.matches(regexMail, receivedLink)
        val matchesPhone: Boolean = Pattern.matches(regexPhone, receivedLink)

        if (matchesWeb || matchesWebS) {
            foundLinkType = URL_WEB
        } else if (matchesMail) {
            foundLinkType = URL_MAIL
        } else if (matchesPhone) {
            foundLinkType = URL_PHONE
        }

        return foundLinkType
    }

    private fun replaceTitleText(replacedText: String) {
        receivedLink = receivedLink.replace(replacedText, "")
    }

    private fun setTextButton(stringId: Int) {
        btnOpen.text = context.getString(stringId)
    }

    private fun installHandlers() {
        btnOpen.setOnSingleClickListener { clickingOpenButton() }
        btnCopy.setOnSingleClickListener { clickingCopyButton() }
    }

    private fun clickingOpenButton() {
        dialog.dismiss()
        launchClient()
    }

    private fun launchClient() {
        when (typeLink) {
            URL_WEB -> IntentManager.launchWebClient(context, linkToOpen)
            URL_MAIL -> IntentManager.launchEmailClient(context, linkToOpen)
            URL_PHONE -> IntentManager.launchPhoneClient(context, linkToOpen)
            else -> showSnackbar(SnackbarControl.MSG_UNKNOWN_ERROR)
        }
    }

    private fun showSnackbar(typeMessage: String) = fragment.showSnackbar(typeMessage)

    private fun clickingCopyButton() {
        dialog.dismiss()
        performCopyLink()
    }

    private fun performCopyLink() {
        try {
            val service: String = Context.CLIPBOARD_SERVICE
            val clipboard: ClipboardManager = context.getSystemService(service) as ClipboardManager
            val clipData: ClipData = ClipData.newPlainText("copy_links", receivedLink)
            clipboard.setPrimaryClip(clipData)
            showSnackbar(SnackbarControl.MSG_NOTE_HYPERLINKS_COPIED)
        } catch (exception: Exception) {
            showSnackbar(SnackbarControl.MSG_INVALID_LINK)
        }
    }
}