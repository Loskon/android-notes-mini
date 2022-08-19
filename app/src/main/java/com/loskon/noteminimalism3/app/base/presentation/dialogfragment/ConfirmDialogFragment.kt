package com.loskon.noteminimalism3.app.base.presentation.dialogfragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updatePadding
import com.loskon.noteminimalism3.app.base.extension.fragment.putArgs
import com.loskon.noteminimalism3.app.base.extension.view.dp
import com.loskon.noteminimalism3.app.base.extension.view.setTextSizeKtx

open class ConfirmDialogFragment : BaseAppDialogFragment() {

    private val titleStringId: Int by lazy { arguments?.getInt(PUT_TITLE_STRING_ID_KEY) ?: 0 }
    private val btnOkStringId: Int by lazy { arguments?.getInt(PUT_BTN_OK_STRING_ID_KEY) ?: 0 }
    private val btnCancelStringId: Int by lazy { arguments?.getInt(PUT_BTN_CANCEL_STRING_ID_KEY) ?: 0 }
    private val messageStringId: Int by lazy { arguments?.getInt(PUT_MESSAGE_STRING_ID_KEY) ?: 0 }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureBaseViewsParameters()
        configureTextViewMessage()
    }

    private fun configureBaseViewsParameters() {
        if (titleStringId != 0) setTitleDialog(titleStringId)
        if (btnOkStringId != 0) setTextBtnOk(btnOkStringId)
        if (btnCancelStringId != 0) setTextBtnCancel(btnCancelStringId)
    }

    private fun configureTextViewMessage() {
        if (messageStringId != 0) {
            val textView = TextView(requireContext()).apply {
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.WRAP_CONTENT

                setTextSizeKtx(16)
                updatePadding(16.dp, 16.dp, 16.dp, 16.dp)
                layoutParams = ViewGroup.LayoutParams(width, height)
                text = getString(messageStringId)
                gravity = Gravity.CENTER
            }

            addView(textView)
        }
    }

    companion object {
        const val TAG = "ConfirmDialogFragment"
        private const val PUT_TITLE_STRING_ID_KEY = "PUT_TITLE_STRING_ID_KEY"
        private const val PUT_BTN_OK_STRING_ID_KEY = "PUT_BTN_OK_STRING_ID_KEY"
        private const val PUT_BTN_CANCEL_STRING_ID_KEY = "PUT_BTN_CANCEL_STRING_ID_KEY"
        private const val PUT_MESSAGE_STRING_ID_KEY = "PUT_MESSAGE_STRING_ID_KEY"

        fun newInstance(
            titleStringId: Int,
            btnOkStringId: Int,
            btnCancelStringId: Int
        ): ConfirmDialogFragment {
            return ConfirmDialogFragment().putArgs {
                putInt(PUT_TITLE_STRING_ID_KEY, titleStringId)
                putInt(PUT_BTN_OK_STRING_ID_KEY, btnOkStringId)
                putInt(PUT_BTN_CANCEL_STRING_ID_KEY, btnCancelStringId)
            }
        }

        fun newInstance(
            titleStringId: Int,
            btnOkStringId: Int,
            btnCancelStringId: Int,
            messageStringId: Int
        ): ConfirmDialogFragment {
            return ConfirmDialogFragment().putArgs {
                putInt(PUT_TITLE_STRING_ID_KEY, titleStringId)
                putInt(PUT_BTN_OK_STRING_ID_KEY, btnOkStringId)
                putInt(PUT_BTN_CANCEL_STRING_ID_KEY, btnCancelStringId)
                putInt(PUT_MESSAGE_STRING_ID_KEY, messageStringId)
            }
        }
    }
}