package com.loskon.noteminimalism3.base.presentation.sheetdialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.databinding.BaseDialogBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.setVisibilityKtx
import com.loskon.noteminimalism3.viewbinding.viewBinding

open class AppBaseSheetDialogFragment : BaseSheetDialogFragment() {

    private val binding by viewBinding(BaseDialogBinding::inflate)
    protected val color: Int get() = AppPreference.getColor(requireContext())

    private var onOkClick: (() -> Unit)? = null
    private var onCancelClick: (() -> Unit)? = null

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
        setupBaseViewsListeners()
    }

    private fun establishBaseViewsColor() {
        binding.btnBaseDialogOk.setBackgroundColor(color)
        binding.btnBaseDialogCancel.setTextColor(color)
    }

    private fun configureBaseViewsParameters() {
        binding.containerBaseDialog.isNestedScrollingEnabled = isNestedScrollingEnabled
    }

    private fun setupBaseViewsListeners() {
        binding.btnBaseDialogOk.setDebounceClickListener {
            onOkClick?.invoke()
            dismiss()
        }
        binding.btnBaseDialogCancel.setDebounceClickListener {
            dismiss()
        }
    }

    fun setContentView(view: View) {
        binding.containerBaseDialog.setVisibilityKtx(true)
        binding.containerBaseDialog.addView(view)
    }

    fun setDialogTitle(@StringRes stringId: Int) {
        binding.tvBaseDialogTitle.text = getString(stringId)
    }

    fun setDialogTitle(string: String) {
        binding.tvBaseDialogTitle.text = string
    }

    fun setBtnOkText(@StringRes stringId: Int) {
        binding.btnBaseDialogOk.text = getString(stringId)
    }

    fun setBtnOkText(string: String) {
        binding.btnBaseDialogOk.text = string
    }

    fun setTextBtnCancel(@StringRes stringId: Int) {
        binding.btnBaseDialogCancel.text = getString(stringId)
    }

    fun setTextBtnCancel(string: String) {
        binding.btnBaseDialogCancel.text = string
    }

    fun setBtnOkVisibility(visible: Boolean) {
        binding.btnBaseDialogOk.setVisibilityKtx(visible)
    }

    fun setOkClickListener(onOkClick: (() -> Unit)?) {
        this.onOkClick = onOkClick
    }

    fun setCancelClickListener(onCancelListener: (() -> Unit)? = null) {
        this.onCancelClick = onCancelListener
    }
}