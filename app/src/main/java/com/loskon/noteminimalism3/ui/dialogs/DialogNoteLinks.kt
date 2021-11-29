package com.loskon.noteminimalism3.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.fragments.NoteFragment
import com.loskon.noteminimalism3.ui.snackbars.SnackbarManager
import com.loskon.noteminimalism3.utils.IntentUtil
import java.util.regex.Pattern

/**
 * Работа с гиперссылками в заметке
 */

class DialogNoteLinks(
    private val context: Context,
    private val fragment: NoteFragment
) :
    View.OnClickListener {

    companion object {
        const val URL_WEB = "WEB"
        const val URL_MAIL = "MAIL"
        const val URL_PHONE = "PHONE"
        const val URL_ERROR = "ERROR"
    }

    private val dialog: BaseMaterialDialog = BaseMaterialDialog(context)
    private val dialogView = View.inflate(context, R.layout.dialog_open_link, null)

    private val btnOpen: Button = dialogView.findViewById(R.id.btn_link_open)
    private val btnCopy: Button = dialogView.findViewById(R.id.btn_link_copy)

    private var link: String = ""
    private var typeLinks: String = ""

    init {
        dialog.setBtnOkVisibility(false)
        dialog.setBtnCancelVisibility(false)
    }

    fun show(linkUrl: String) {
        this.link = linkUrl

        setDialogTitle()
        installHandlers()

        dialog.show(dialogView)
    }

    private fun setDialogTitle() {
        typeLinks = getTypeURL(link)

        when (typeLinks) {
            URL_MAIL -> {
                replaceText("mailto:")
                setTextBtn(R.string.dg_open_link_send)
            }
            URL_PHONE -> {
                replaceText("tel:")
                setTextBtn(R.string.dg_open_link_call)
            }
            URL_WEB -> {
                setTextBtn(R.string.dg_open_link_open)
            }

            URL_ERROR -> {
                setTextBtn(R.string.dg_open_link_invalid)
            }
        }

        dialog.setTextTitle(link)
    }

    private fun getTypeURL(titleLinks: String): String {
        var typeLinks = URL_ERROR

        val regexWebS = ".*https://.*"
        val regexWeb = ".*http://.*"
        val regexMail = ".*mailto:.*"
        val regexPhone = ".*tel:.*"

        val matchesWebS = Pattern.matches(regexWebS, titleLinks)
        val matchesWeb = Pattern.matches(regexWeb, titleLinks)
        val matchesMail = Pattern.matches(regexMail, titleLinks)
        val matchesPhone = Pattern.matches(regexPhone, titleLinks)

        if (matchesWeb || matchesWebS) typeLinks = URL_WEB
        if (matchesMail) typeLinks = URL_MAIL
        if (matchesPhone) typeLinks = URL_PHONE

        return typeLinks
    }

    private fun replaceText(title: String) {
        link = link.replace(title, "")
    }

    private fun setTextBtn(textId: Int) {
        btnOpen.text = context.getString(textId)
    }

    private fun installHandlers() {
        btnOpen.setOnClickListener(this)
        btnCopy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        dialog.dismiss()

        when (v?.id) {
            R.id.btn_link_open -> openLink()

            R.id.btn_link_copy -> copyLink()
        }
    }

    private fun openLink() {
        when (typeLinks) {
            URL_WEB -> IntentUtil.launcherWebClient(context, link)
            URL_MAIL -> IntentUtil.launcherEmailClient(context, link)
            URL_PHONE -> IntentUtil.launcherPhoneClient(context, link)

            else -> showSnackbar(SnackbarManager.MSG_UNKNOWN_ERROR)
        }
    }

    private fun copyLink() {
        replaceText("http://")
        replaceText("https://")

        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("copy_links", link)
            clipboard.setPrimaryClip(clip)
            showSnackbar(SnackbarManager.MSG_NOTE_HYPERLINKS_COPIED)
        } catch (exception: Exception) {
            showSnackbar(SnackbarManager.MSG_INVALID_LINK)
        }
    }

    private fun showSnackbar(typeMessage: String) {
        fragment.showSnackbar(typeMessage)
    }
}