package com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updatePadding
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.fragment.getDimen
import com.loskon.noteminimalism3.app.base.extension.fragment.putArgs
import com.loskon.noteminimalism3.app.base.extension.view.setTextSizeKtx

open class ConfirmSheetDialogFragment : BaseAppSheetDialogFragment() {

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
            addView(
                TextView(requireContext()).apply {
                    val width = ViewGroup.LayoutParams.MATCH_PARENT
                    val height = ViewGroup.LayoutParams.WRAP_CONTENT
                    val dimen = getDimen(R.dimen.margin_medium)
                    val textDimen = getDimen(R.dimen.text_size_medium)

                    layoutParams = ViewGroup.LayoutParams(width, height)
                    updatePadding(dimen, dimen, dimen, dimen)
                    text = getString(messageStringId)
                    setTextSizeKtx(textDimen)
                    gravity = Gravity.CENTER
                }
            )
        }
    }

    companion object {
        const val TAG = "ConfirmSheetDialogFragment"
        private const val PUT_TITLE_STRING_ID_KEY = "PUT_TITLE_STRING_ID_KEY"
        private const val PUT_BTN_OK_STRING_ID_KEY = "PUT_BTN_OK_STRING_ID_KEY"
        private const val PUT_BTN_CANCEL_STRING_ID_KEY = "PUT_BTN_CANCEL_STRING_ID_KEY"
        private const val PUT_MESSAGE_STRING_ID_KEY = "PUT_MESSAGE_STRING_ID_KEY"

        fun newInstance(
            titleStringId: Int,
            btnOkStringId: Int,
            btnCancelStringId: Int
        ): ConfirmSheetDialogFragment {
            return ConfirmSheetDialogFragment().putArgs {
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
        ): ConfirmSheetDialogFragment {
            return ConfirmSheetDialogFragment().putArgs {
                putInt(PUT_TITLE_STRING_ID_KEY, titleStringId)
                putInt(PUT_BTN_OK_STRING_ID_KEY, btnOkStringId)
                putInt(PUT_BTN_CANCEL_STRING_ID_KEY, btnCancelStringId)
                putInt(PUT_MESSAGE_STRING_ID_KEY, messageStringId)
            }
        }
    }
}