package com.loskon.noteminimalism3.app.screens.note.presentation

import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.fragment.putArgs
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.presentation.dialogfragment.AppBaseDialogFragment
import com.loskon.noteminimalism3.databinding.DialogOpenLinkBinding
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.viewbinding.viewBinding
import java.util.regex.Pattern

class NoteLinkDialogFragment : AppBaseDialogFragment() {

    private val binding by viewBinding(DialogOpenLinkBinding::inflate)

    // TODO
    private val url by lazy { arguments?.getString(PUT_URL_KEY) ?: "" }

    private var onInvalidLinkOpen: (() -> Unit)? = null
    private var onLinkCopy: (() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        setupViewsParameters()
        configureParametersForLink()
        setupViewsListeners()
    }

    private fun setupViewsParameters() {
        setBtnCancelVisibility(false)
        setBtnOkVisibility(false)
    }

    private fun configureParametersForLink() {
        val urlType = getUrlType(url)
        val urlTitle = getUrlWithoutPrefix(urlType)
        val stringId = getOpenStringId(urlType)

        setTitleDialog(urlTitle)
        binding.btnLinkOpen.text = getString(stringId)
    }

    private fun getUrlType(url: String): String? {
        var urlType: String? = null

        if (Pattern.matches(WEB_S_REGEX, url) || Pattern.matches(WEB_REGEX, url)) {
            urlType = WEB_URL
        } else if (Pattern.matches(MAIL_REGEX, url)) {
            urlType = MAIL_URL
        } else if (Pattern.matches(PHONE_REGEX, url)) {
            urlType = PHONE_URL
        }

        return urlType
    }

    private fun getUrlWithoutPrefix(urlType: String?): String {
        return when (urlType) {
            MAIL_URL -> url.replace("mailto:", "")
            PHONE_URL -> url.replace("tel:", "")
            WEB_URL -> url
            else -> url
        }
    }

    private fun getOpenStringId(urlType: String?): Int {
        return when (urlType) {
            MAIL_URL -> R.string.dg_open_link_send
            PHONE_URL -> R.string.dg_open_link_call
            WEB_URL -> R.string.dg_open_link_open
            else -> R.string.dg_open_link_invalid
        }
    }

    private fun setupViewsListeners() {
        binding.btnLinkOpen.setDebounceClickListener {
            launchClient(getUrlType(url))
            dismiss()
        }
        binding.btnLinkCopy.setDebounceClickListener {
            val text = getUrlWithoutPrefix(getUrlType(url))
            com.loskon.noteminimalism3.base.clipboardmanager.ClipboardHelper.copyText(requireContext(), text)
            onLinkCopy?.invoke()
            dismiss()
        }
    }

    private fun launchClient(urlType: String?) {
        when (urlType) {
            WEB_URL -> IntentManager.launchWebClient(requireContext(), url)
            MAIL_URL -> IntentManager.launchEmailClient(requireContext(), url)
            PHONE_URL -> IntentManager.launchPhoneClient(requireContext(), url)
            else -> onInvalidLinkOpen?.invoke()
        }
    }

    fun setOnInvalidLinkOpenListener(onInvalidLinkOpen: (() -> Unit)?) {
        this.onInvalidLinkOpen = onInvalidLinkOpen
    }

    fun setOnLinkCopyListener(onLinkCopy: (() -> Unit)?) {
        this.onLinkCopy = onLinkCopy
    }

    companion object {
        const val TAG = "NoteLinkDialogFragment"
        private const val PUT_URL_KEY = "PUT_URL_KEY"

        private const val WEB_URL = "WEB_URL"
        private const val MAIL_URL = "MAIL_URL"
        private const val PHONE_URL = "PHONE_URL"

        private const val WEB_S_REGEX = ".*https://.*"
        private const val WEB_REGEX = ".*http://.*"
        private const val MAIL_REGEX = ".*mailto:.*"
        private const val PHONE_REGEX = ".*tel:.*"

        fun newInstance(
            url: String
        ): NoteLinkDialogFragment {
            return NoteLinkDialogFragment().putArgs {
                putString(PUT_URL_KEY, url)
            }
        }
    }
}