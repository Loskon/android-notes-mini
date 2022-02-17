package com.loskon.noteminimalism3.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.ui.basedialogs.BaseMaterialDialog
import com.loskon.noteminimalism3.ui.fragments.NoteFragment
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import java.util.regex.Pattern

/**
 * Окно для открытия/копирования гиперссылок
 */

private const val URL_WEB = "WEB"
private const val URL_MAIL = "MAIL"
private const val URL_PHONE = "PHONE"
private const val URL_ERROR = "ERROR"

class NoteLinkDialog(private val fragment: NoteFragment) :
    BaseMaterialDialog(fragment.requireContext(), R.layout.dialog_open_link) {

    private val btnOpen: Button = view.findViewById(R.id.btn_link_open)
    private val btnCopy: Button = view.findViewById(R.id.btn_link_copy)

    private var receivedLink: String = ""
    private var linkToOpen: String = ""
    private var typeLink: String = ""

    init {
        configureDialogParameters()
    }

    private fun configureDialogParameters() {
        setBtnOkVisibility(false)
        setBtnCancelVisibility(false)
    }

    fun show(receivedLink: String) {
        this.receivedLink = receivedLink
        this.linkToOpen = receivedLink

        configInsertedViews()
        setupViewsListeners()
        super.show()
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

        setTitleDialog(receivedLink)
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

    private fun setupViewsListeners() {
        btnOpen.setOnSingleClickListener { clickingOpenButton() }
        btnCopy.setOnSingleClickListener { clickingCopyButton() }
    }

    private fun clickingOpenButton() {
        dismiss()
        launchClient()
    }

    private fun launchClient() {
        when (typeLink) {
            URL_WEB -> IntentManager.launchWebClient(context, linkToOpen)
            URL_MAIL -> IntentManager.launchEmailClient(context, linkToOpen)
            URL_PHONE -> IntentManager.launchPhoneClient(context, linkToOpen)
            else -> showSnackbar(WarningSnackbar.MSG_UNKNOWN_ERROR)
        }
    }

    private fun showSnackbar(messageType: String) = fragment.showSnackbar(messageType)

    private fun clickingCopyButton() {
        dismiss()
        performCopyLink()
    }

    private fun performCopyLink() {
        try {
            val service: String = Context.CLIPBOARD_SERVICE
            val clipboard: ClipboardManager = context.getSystemService(service) as ClipboardManager
            val clipData: ClipData = ClipData.newPlainText("copy_links", receivedLink)
            clipboard.setPrimaryClip(clipData)
            showSnackbar(WarningSnackbar.MSG_NOTE_HYPERLINKS_COPIED)
        } catch (exception: Exception) {
            showSnackbar(WarningSnackbar.MSG_INVALID_LINK)
        }
    }
}