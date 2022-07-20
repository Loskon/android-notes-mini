package com.loskon.noteminimalism3.app.base.presentation.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.databinding.BaseDialogBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.setVisibilityKtx
import com.loskon.noteminimalism3.viewbinding.viewBinding

open class BaseAppDialogFragment(
    @LayoutRes private val insertLayoutId: Int? = null
) : BaseDialogFragment() {

    private val binding by viewBinding(BaseDialogBinding::inflate)

    val color: Int get() = AppPreference.getColor(requireContext())

    private val layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

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
        setupBaseViewListener()
        addInsertedView()
    }

    private fun establishBaseViewsColor() {
        binding.btnBaseDialogOk.setBackgroundColor(color)
        binding.btnBaseDialogCancel.setTextColor(color)
    }

    private fun setupBaseViewListener() {
        binding.btnBaseDialogCancel.setDebounceClickListener { dismiss() }
    }

    private fun addInsertedView() {
        if (insertLayoutId != null) {
            val insertView: View = View.inflate(context, insertLayoutId, null)
            insertView.layoutParams = layoutParams
            binding.containerBaseDialog.addView(insertView)
        } else {
            binding.containerBaseDialog.setVisibilityKtx(false)
        }
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

    fun setOnClickBtnOk(onClick: () -> Unit?) {
        binding.btnBaseDialogOk.setDebounceClickListener { onClick() }
    }

    fun setOnClickBtnCancel(onClick: () -> Unit?) {
        binding.btnBaseDialogCancel.setDebounceClickListener { onClick() }
    }
}