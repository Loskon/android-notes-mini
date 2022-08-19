package com.loskon.noteminimalism3.app.base.presentation.dialogfragment

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

open class BaseAppDialogFragment : BaseDialogFragmentNew() {

    private val binding by viewBinding(BaseDialogBinding::inflate)
    protected val color: Int get() = AppPreference.getColor(requireContext())

    private var onItemClickListener: (() -> Unit)? = null
    private var onItemLongClickListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishBaseViewsColor()
        configureBaseViewsParameters()
        setupBaseViewListener()
    }

    private fun establishBaseViewsColor() {
        binding.btnBaseDialogOk.setBackgroundColor(color)
        binding.btnBaseDialogCancel.setTextColor(color)
    }

    private fun configureBaseViewsParameters() {
        binding.containerBaseDialog.setVisibilityKtx(false)
    }

    private fun setupBaseViewListener() {
        binding.btnBaseDialogOk.setDebounceClickListener {
            onItemClickListener?.invoke()
            dismiss()
        }
        binding.btnBaseDialogCancel.setDebounceClickListener {
            onItemLongClickListener?.invoke()
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

    fun setTitleVisibility(isVisible: Boolean) {
        binding.tvBaseDialogTitle.setVisibilityKtx(isVisible)
    }

    fun setBtnOkVisibility(isVisible: Boolean) {
        binding.btnBaseDialogOk.setVisibilityKtx(isVisible)
    }

    fun setBtnCancelVisibility(isVisible: Boolean) {
        binding.btnBaseDialogCancel.setVisibilityKtx(isVisible)
    }

    fun setBtnOkClickListener(onItemClickListener: (() -> Unit)? = null) {
        this.onItemClickListener = onItemClickListener
    }

    fun setBtnCancelClickListener(onItemLongClickListener: (() -> Unit)? = null) {
        this.onItemLongClickListener = onItemLongClickListener
    }
}