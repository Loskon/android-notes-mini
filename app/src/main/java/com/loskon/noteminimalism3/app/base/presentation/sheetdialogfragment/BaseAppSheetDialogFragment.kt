package com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.databinding.BaseDialogBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.setVisibilityKtx
import com.loskon.noteminimalism3.viewbinding.viewBinding

open class BaseAppSheetDialogFragment : BaseSheetDialogFragment() {

    private val binding by viewBinding(BaseDialogBinding::inflate)
    protected val color: Int get() = AppPreference.getColor(requireContext())

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
            btnOkOnClick?.invoke()
        }
        binding.btnBaseDialogCancel.setDebounceClickListener {
            dismiss()
        }
    }

    fun addView(view: View) {
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

        fun newInstance() = BaseAppSheetDialogFragment()
    }
}