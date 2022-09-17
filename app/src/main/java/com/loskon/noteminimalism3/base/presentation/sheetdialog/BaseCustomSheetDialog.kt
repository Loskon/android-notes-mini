package com.loskon.noteminimalism3.base.presentation.sheetdialog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.databinding.BaseDialogBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.setVisibilityKtx
import com.loskon.noteminimalism3.viewbinding.viewBinding

open class BaseCustomSheetDialog(
    context: Context
) : BottomSheetDialog(context, R.style.RoundedSheetDialogStyle) {

    private val binding by viewBinding(BaseDialogBinding::inflate)
    val color: Int get() = AppPreference.getColor(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    init {
        configureSheetBehavior()
        configureBaseViewsParameters()
        establishBaseViewsColor()
        setupBaseViewListener()
    }

    private fun configureSheetBehavior() {
        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }
    }

    private fun configureBaseViewsParameters() {
        binding.containerBaseDialog.setVisibilityKtx(false)
    }

    private fun establishBaseViewsColor() {
        binding.btnBaseDialogOk.setBackgroundColor(color)
        binding.btnBaseDialogCancel.setTextColor(color)
    }

    private fun setupBaseViewListener() {
        binding.btnBaseDialogCancel.setDebounceClickListener { dismiss() }
    }

    fun insertView(view: View) {
        binding.containerBaseDialog.setVisibilityKtx(true)
        binding.containerBaseDialog.addView(view)
    }

    fun setTitleDialog(@StringRes stringId: Int) {
        binding.tvBaseDialogTitle.text = context.getString(stringId)
    }

    fun setTextBtnOk(stringId: Int) {
        binding.btnBaseDialogOk.text = context.getString(stringId)
    }

    fun setTextBtnCancel(stringId: Int) {
        binding.btnBaseDialogCancel.text = context.getString(stringId)
    }

    fun setTitleVisibility(isVisible: Boolean) {
        binding.tvBaseDialogTitle.setVisibilityKtx(isVisible)
    }

    fun setBtnOkVisibility(isVisible: Boolean) {
        binding.btnBaseDialogOk.setVisibilityKtx(isVisible)
    }

    fun setBtnCancelVisibility(isVisible: Boolean) {
        binding.btnBaseDialogCancel.setVisibilityKtx(isVisible)
    }

    fun setOnClickBtnOk(onClick: () -> Unit?): BaseCustomSheetDialog {
        binding.btnBaseDialogOk.setDebounceClickListener { onClick() }
        return this
    }

    fun setOnClickBtnCancel(onClick: () -> Unit?): BaseCustomSheetDialog {
        binding.btnBaseDialogCancel.setDebounceClickListener { onClick() }
        return this
    }

    inline fun create(functions: BaseCustomSheetDialog.() -> Unit): BaseCustomSheetDialog {
        this.functions()
        return this
    }
}