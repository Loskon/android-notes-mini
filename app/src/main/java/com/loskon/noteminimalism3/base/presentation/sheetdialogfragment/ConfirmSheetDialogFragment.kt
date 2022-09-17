package com.loskon.noteminimalism3.base.presentation.sheetdialogfragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updatePadding
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.fragment.getDimen
import com.loskon.noteminimalism3.base.extension.fragment.putArgs
import com.loskon.noteminimalism3.base.extension.fragment.setFragmentClick
import com.loskon.noteminimalism3.base.extension.view.setTextSizeKtx

open class ConfirmSheetDialogFragment : AppBaseSheetDialogFragment() {

    private val requestKey: String? by lazy { arguments?.getString(PUT_REQUEST_KEY) }
    private val title: String? by lazy { arguments?.getString(PUT_TITLE_STRING_KEY) }
    private val btnOkText: String? by lazy { arguments?.getString(PUT_BTN_OK_STRING_KEY) }
    private val btnCancelText: String? by lazy { arguments?.getString(PUT_BTN_CANCEL_STRING_KEY) }
    private val message: String? by lazy { arguments?.getString(PUT_MESSAGE_STRING_KEY) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureBaseViewsParameters()
        configureTextViewMessage()
        setupConfirmListener()
    }

    private fun configureBaseViewsParameters() {
        if (title != null) setDialogTitle(requireNotNull(title))
        if (btnOkText != null) setBtnOkText(requireNotNull(btnOkText))
        if (btnCancelText != null) setTextBtnCancel(requireNotNull(btnCancelText))
    }

    private fun configureTextViewMessage() {
        if (message != null) {
            setContentView(
                TextView(requireContext()).apply {
                    val width = ViewGroup.LayoutParams.MATCH_PARENT
                    val height = ViewGroup.LayoutParams.WRAP_CONTENT
                    val dimen = getDimen(R.dimen.margin_medium)
                    val textDimen = getDimen(R.dimen.text_size_medium)

                    layoutParams = ViewGroup.LayoutParams(width, height)
                    updatePadding(dimen, dimen, dimen, dimen)
                    text = requireNotNull(message)
                    setTextSizeKtx(textDimen)
                    gravity = Gravity.CENTER
                }
            )
        }
    }

    private fun setupConfirmListener() {
        setOkClickListener {
            if (requestKey != null) setFragmentClick(requireNotNull(requestKey))
        }
    }

    companion object {
        private const val PUT_REQUEST_KEY = "PUT_REQUEST_KEY"
        private const val PUT_TITLE_STRING_KEY = "PUT_TITLE_STRING_KEY"
        private const val PUT_BTN_OK_STRING_KEY = "PUT_BTN_OK_STRING_KEY"
        private const val PUT_BTN_CANCEL_STRING_KEY = "PUT_BTN_CANCEL_STRING_KEY"
        private const val PUT_MESSAGE_STRING_KEY = "PUT_MESSAGE_STRING_KEY"

        fun newInstance(
            requestKey: String,
            title: String,
            btnOkText: String,
            btnCancelText: String
        ): ConfirmSheetDialogFragment {
            return ConfirmSheetDialogFragment().putArgs {
                putString(PUT_REQUEST_KEY, requestKey)
                putString(PUT_TITLE_STRING_KEY, title)
                putString(PUT_BTN_OK_STRING_KEY, btnOkText)
                putString(PUT_BTN_CANCEL_STRING_KEY, btnCancelText)
            }
        }

        fun newInstance(
            requestKey: String,
            title: String,
            btnOkText: String,
            btnCancelText: String,
            message: String
        ): ConfirmSheetDialogFragment {
            return ConfirmSheetDialogFragment().putArgs {
                putString(PUT_REQUEST_KEY, requestKey)
                putString(PUT_TITLE_STRING_KEY, title)
                putString(PUT_BTN_OK_STRING_KEY, btnOkText)
                putString(PUT_BTN_CANCEL_STRING_KEY, btnCancelText)
                putString(PUT_MESSAGE_STRING_KEY, message)
            }
        }
    }
}