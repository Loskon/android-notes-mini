package com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.updatePadding
import com.loskon.noteminimalism3.app.base.extension.fragment.putArgs
import com.loskon.noteminimalism3.app.base.extension.view.dp
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setTextSizeKtx
import com.loskon.noteminimalism3.databinding.BaseDialogBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.setVisibilityKtx
import com.loskon.noteminimalism3.viewbinding.viewBinding

open class BaseAppSheetDialogFragmentNew : BaseSheetDialogFragmentNew() {

    private val binding by viewBinding(BaseDialogBinding::inflate)
    protected val color: Int get() = AppPreference.getColor(requireContext())

    private val titleId: Int by lazy { arguments?.getInt(PUT_TITLE_KEY) ?: 0 }
    private val btnOkTextId: Int by lazy { arguments?.getInt(PUT_BTN_OK_TEXT_KEY) ?: 0 }
    private val btnCancelTextId: Int by lazy { arguments?.getInt(PUT_BTN_CANCEL_TEXT_KEY) ?: 0 }
    private val textId: Int by lazy { arguments?.getInt(PUT_TEXT_KEY) ?: 0 }

    private var btnOkOnClick: (() -> Unit)? = null

    open val isNestedScrollingEnabled: Boolean = false

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
        if (titleId != 0) setTitleDialog(titleId)
        if (btnOkTextId != 0) setTextBtnOk(btnOkTextId)
        if (btnCancelTextId != 0) setTextBtnCancel(btnCancelTextId)
        binding.containerBaseDialog.setVisibilityKtx(false)
        binding.containerBaseDialog.isNestedScrollingEnabled = isNestedScrollingEnabled
    }

    private fun configureTextViewMessage() {
        if (textId != 0) {
            val textView = TextView(requireContext()).apply {
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.WRAP_CONTENT

                gravity = Gravity.CENTER
                setTextSizeKtx(16)
                updatePadding(16.dp, 16.dp, 16.dp, 16.dp)
                layoutParams = ViewGroup.LayoutParams(width, height)
                text = getString(textId ?: 0)
            }

            binding.containerBaseDialog.setVisibilityKtx(true)
            binding.containerBaseDialog.addView(textView)
        }
    }

    private fun setupBaseViewsListeners() {
        binding.btnBaseDialogOk.setDebounceClickListener {
            btnOkOnClick?.invoke()
        }
        binding.btnBaseDialogCancel.setDebounceClickListener {
            dismiss()
        }
    }

    fun insertView(view: View) {
        binding.containerBaseDialog.setVisibilityKtx(true)
        binding.containerBaseDialog.addView(view)
    }

    fun setTitleDialog(@StringRes stringId: Int) {
        binding.tvBaseDialogTitle.text = getString(stringId)
    }

    fun setTextBtnOk(stringId: Int) {
        binding.btnBaseDialogOk.text = getString(stringId)
    }

    fun setTextBtnCancel(stringId: Int) {
        binding.btnBaseDialogCancel.text = getString(stringId)
    }

    fun setBtnOkVisibility(isVisible: Boolean) {
        binding.btnBaseDialogOk.setVisibilityKtx(isVisible)
    }

    fun setOnClickBtnOk(btnOkOnClick: (() -> Unit)?) {
        this.btnOkOnClick = btnOkOnClick
    }

    companion object {
        const val TAG = "BaseAppSheetDialogFragmentNew"
        private const val PUT_TITLE_KEY = "PUT_TITLE_KEY"
        private const val PUT_BTN_OK_TEXT_KEY = "PUT_BTN_OK_TEXT_KEY"
        private const val PUT_BTN_CANCEL_TEXT_KEY = "PUT_BTN_CANCEL_TEXT_KEY"
        private const val PUT_TEXT_KEY = "PUT_TEXT_KEY"

        fun newInstance() = BaseAppSheetDialogFragmentNew()

        fun newInstance(
            titleId: Int,
            btnOkTextId: Int,
            btnCancelTextId: Int
        ): BaseAppSheetDialogFragmentNew {
            return BaseAppSheetDialogFragmentNew().putArgs {
                putInt(PUT_TITLE_KEY, titleId)
                putInt(PUT_BTN_OK_TEXT_KEY, btnOkTextId)
                putInt(PUT_BTN_CANCEL_TEXT_KEY, btnCancelTextId)
            }
        }

        fun newInstance(
            titleId: Int,
            btnOkTextId: Int,
            btnCancelTextId: Int,
            textId: Int
        ): BaseAppSheetDialogFragmentNew {
            return BaseAppSheetDialogFragmentNew().putArgs {
                putInt(PUT_TITLE_KEY, titleId)
                putInt(PUT_BTN_OK_TEXT_KEY, btnOkTextId)
                putInt(PUT_BTN_CANCEL_TEXT_KEY, btnCancelTextId)
                putInt(PUT_TEXT_KEY, textId)
            }
        }
    }
}