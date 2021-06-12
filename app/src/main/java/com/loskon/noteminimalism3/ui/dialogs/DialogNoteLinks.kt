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
 *
 */

const val URL_WEB = "WEB"
const val URL_MAIL = "MAIL"
const val URL_PHONE = "PHONE"
const val ERROR = "ERROR"

class DialogNoteLinks(private val activity: NoteActivity) : View.OnClickListener {

    private val materialDialog: BaseMaterialDialog = BaseMaterialDialog(activity)
    private val dialogView = View.inflate(activity, R.layout.dialog_open_link, null)

    private val btnOpen: Button = dialogView.findViewById(R.id.btn_link_open)
    private val btnCopy: Button = dialogView.findViewById(R.id.btn_link_copy)

    private var titleLinks: String? = null
    private var typeLinks: String? = null

    init {
        materialDialog.setBtnOkVisibility(false)
        materialDialog.setBtnCancelVisibility(false)
    }

    fun show(titleLinks: String) {
        this.titleLinks = titleLinks
        typeLinks = getTypeURL(titleLinks)
        setTitle()
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

    private fun setTitle() {
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
                setTextBtn(R.string.unknown_error)
            }
        }

        titleLinks?.let { it -> materialDialog.setTextTitle(it) }
    }

    private fun replaceText(title: String) {
        titleLinks = titleLinks?.replace(title, "")
    }

    private fun setTextBtn(textId: Int) {
        btnOpen.text = activity.getString(textId)
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
            URL_WEB -> activity.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(titleLinks))
            )
            URL_MAIL -> activity.startActivity(
                Intent(Intent.ACTION_SENDTO, Uri.parse(titleLinks))
            )
            URL_PHONE -> activity.startActivity(
                Intent(Intent.ACTION_DIAL, Uri.parse(titleLinks))
            )

            else -> showSnackbar(false, MySnackbarNoteMessage.ERROR)
        }
    }

    private fun copyLink() {
        replaceText("http://")
        replaceText("https://")
        try {
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("copy_links", titleLinks)
            clipboard.setPrimaryClip(clip)
            showSnackbar(true, MySnackbarNoteMessage.MSG_NOTE_HYPERLINKS_COPIED)
        } catch (exception: Exception) {
            exception.printStackTrace()
            showSnackbar(false, MySnackbarNoteMessage.MSG_INVALID_LINK)
        }
    }

    private fun showSnackbar(isSuccess: Boolean, message: String) {
        activity.mySnackbarNoteMessage.show(isSuccess, message)
    }
}