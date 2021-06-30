package com.loskon.noteminimalism3.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.NoteActivity
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarNoteMessage
import java.util.regex.Pattern

/**
 * Работа с ссылками в тексте заметок
 */

class DialogNoteLinks(private val context: Context) : View.OnClickListener {

    companion object {
        const val URL_WEB = "WEB"
        const val URL_MAIL = "MAIL"
        const val URL_PHONE = "PHONE"
        const val ERROR = "ERROR"
    }

    private val materialDialog: BaseMaterialDialog = BaseMaterialDialog(context)
    private val dialogView = View.inflate(context, R.layout.dialog_open_link, null)

    private val btnOpen: Button = dialogView.findViewById(R.id.btn_link_open)
    private val btnCopy: Button = dialogView.findViewById(R.id.btn_link_copy)

    private var link: String = ""
    private var typeLinks: String = ""

    init {
        materialDialog.setBtnOkVisibility(false)
        materialDialog.setBtnCancelVisibility(false)
    }

    fun show(linkUrl: String) {
        this.link = linkUrl

        setDialogTitle()
        installHandlers()

        materialDialog.show(dialogView)
    }

    private fun getTypeURL(titleLinks: String): String {
        var typeLinks = ERROR

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

            ERROR -> {
                setTextBtn(R.string.dg_open_link_invalid)
            }
        }

        materialDialog.setTextTitle(link)
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
        when (v?.id) {
            R.id.btn_link_open -> openLink()

            R.id.btn_link_copy -> copyLink()
        }

        materialDialog.dismiss()
    }

    private fun openLink() {
        when (typeLinks) {
            URL_WEB -> context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(link))
            )
            URL_MAIL -> context.startActivity(
                Intent(Intent.ACTION_SENDTO, Uri.parse(link))
            )
            URL_PHONE -> context.startActivity(
                Intent(Intent.ACTION_DIAL, Uri.parse(link))
            )

            else -> showSnackbar(false, MySnackbarNoteMessage.ERROR)
        }
    }

    private fun copyLink() {
        replaceText("http://")
        replaceText("https://")
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("copy_links", link)
            clipboard.setPrimaryClip(clip)
            showSnackbar(true, MySnackbarNoteMessage.MSG_NOTE_HYPERLINKS_COPIED)
        } catch (exception: Exception) {
            exception.printStackTrace()
            showSnackbar(false, MySnackbarNoteMessage.MSG_INVALID_LINK)
        }
    }

    private fun showSnackbar(isSuccess: Boolean, message: String) {
        (context as NoteActivity).mySnackbarNoteMessage.show(isSuccess, message)
    }
}