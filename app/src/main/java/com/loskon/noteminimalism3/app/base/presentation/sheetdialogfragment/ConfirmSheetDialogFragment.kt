package com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updatePadding
import com.loskon.noteminimalism3.app.base.extension.fragment.putArgs
import com.loskon.noteminimalism3.app.base.extension.view.dp
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setTextSizeKtx
import com.loskon.noteminimalism3.databinding.BaseDialogBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.setVisibilityKtx
import com.loskon.noteminimalism3.viewbinding.viewBinding

open class ConfirmSheetDialogFragment : BaseSheetDialogFragment() {

    private val binding by viewBinding(BaseDialogBinding::inflate)
    protected val color: Int get() = AppPreference.getColor(requireContext())

    private val titleStringId: Int by lazy { arguments?.getInt(PUT_TITLE_STRING_ID_KEY) ?: 0 }
    private val btnOkStringId: Int by lazy { arguments?.getInt(PUT_BTN_OK_STRING_ID_KEY) ?: 0 }
    private val btnCancelStringId: Int by lazy { arguments?.getInt(PUT_BTN_CANCEL_STRING_ID_KEY) ?: 0 }
    private val messageStringId: Int by lazy { arguments?.getInt(PUT_MESSAGE_STRING_ID_KEY) ?: 0 }

    private var btnOkClick: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishBaseViewsColor()
        configureBaseViewsParameters()
        configureTextViewMessage()
        setupBaseViewsListeners()
    }

    private fun establishBaseViewsColor() {
        binding.btnBaseDialogOk.setBackgroundColor(color)
        binding.btnBaseDialogCancel.setTextColor(color)
    }

    private fun configureBaseViewsParameters() {
        if (titleStringId != 0) binding.tvBaseDialogTitle.text = getString(titleStringId)
        if (btnOkStringId != 0) binding.btnBaseDialogOk.text = getString(btnOkStringId)
        if (btnCancelStringId != 0) binding.btnBaseDialogCancel.text = getString(btnCancelStringId)
    }

    private fun configureTextViewMessage() {
        if (messageStringId != 0) {
            val textView = TextView(requireContext()).apply {
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.WRAP_CONTENT

                gravity = Gravity.CENTER
                setTextSizeKtx(16)
                updatePadding(16.dp, 16.dp, 16.dp, 16.dp)
                layoutParams = ViewGroup.LayoutParams(width, height)
                text = getString(messageStringId)
            }

            binding.containerBaseDialog.addView(textView)
            binding.containerBaseDialog.setVisibilityKtx(true)
        }
    }

    private fun setupBaseViewsListeners() {
        binding.btnBaseDialogOk.setDebounceClickListener {
            btnOkClick?.invoke()
            dismiss()
        }
        binding.btnBaseDialogCancel.setDebounceClickListener {
            dismiss()
        }
    }

    fun setBtnOkClickListener(btnOkClick: (() -> Unit)?) {
        this.btnOkClick = btnOkClick
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