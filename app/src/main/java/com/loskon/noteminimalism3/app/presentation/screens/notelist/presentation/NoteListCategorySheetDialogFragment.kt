package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.fragment.putArgs
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceMenuItemClickListener
import com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment.BaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.FragmentDialogCategoryBinding
import com.loskon.noteminimalism3.managers.setColorStateMenuItem
import com.loskon.noteminimalism3.viewbinding.viewBinding

class NoteListCategorySheetDialogFragment : BaseSheetDialogFragment() {

    private val binding by viewBinding(FragmentDialogCategoryBinding::inflate)
    private val category by lazy { arguments?.getString(PUT_CATEGORY_KEY) ?: "" }

    private var onCategoryClick: ((category: String) -> Unit)? = null
    private var onSettingsClick: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewsParameters()
        establishViewsColor()
        setupViewsListeners()
    }

    private fun setupViewsParameters() {
        val menuItemId = getCheckedMenuItemId()
        binding.navigationView.menu.findItem(menuItemId).isChecked = true
    }

    private fun getCheckedMenuItemId(): Int {
        return when (category) {
            NoteListViewModel.CATEGORY_ALL_NOTES1 -> R.id.nav_item_note
            NoteListViewModel.CATEGORY_FAVORITES1 -> R.id.nav_item_favorites
            NoteListViewModel.CATEGORY_TRASH1 -> R.id.nav_item_trash
            else -> R.id.nav_item_note
        }
    }

    private fun establishViewsColor() {
        binding.navigationView.setColorStateMenuItem(requireContext())
    }

    private fun setupViewsListeners() {
        with(binding.navigationView) {
            setDebounceMenuItemClickListener(R.id.nav_item_note) {
                onCategoryClick?.invoke(NoteListViewModel.CATEGORY_ALL_NOTES1)
                dismiss()
            }
            setDebounceMenuItemClickListener(R.id.nav_item_favorites) {
                onCategoryClick?.invoke(NoteListViewModel.CATEGORY_FAVORITES1)
                dismiss()
            }
            setDebounceMenuItemClickListener(R.id.nav_item_trash) {
                onCategoryClick?.invoke(NoteListViewModel.CATEGORY_TRASH1)
                dismiss()
            }
            setDebounceMenuItemClickListener(R.id.nav_item_settings) {
                onSettingsClick?.invoke()
                dismiss()
            }
        }
    }

    fun setOnCategorySelectListener(onCategoryClick: ((category: String) -> Unit)?) {
        this.onCategoryClick = onCategoryClick
    }

    fun setOnSettingsClickListener(onSettingsClick: (() -> Unit)?) {
        this.onSettingsClick = onSettingsClick
    }

    companion object {
        const val TAG = "CategorySheetDialogFragment"
        private const val PUT_CATEGORY_KEY = "PUT_CATEGORY_KEY"

        fun newInstance(category: String): NoteListCategorySheetDialogFragment {
            return NoteListCategorySheetDialogFragment().putArgs {
                putString(PUT_CATEGORY_KEY, category)
            }
        }
    }
}