package com.loskon.noteminimalism3.ui.dialogfragmntes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.managers.setColorStateMenuItem
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_FAVORITES
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.utils.getShortDrawable

/**
 * Выбор категории заметок и открытие окна настроек
 */

class CategorySheetFragment : BottomSheetDialogFragment() {

    private var callback: CategorySheetCallback? = null

    private lateinit var navigationView: NavigationView

    private var category: String = CATEGORY_ALL_NOTES

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerCallback(context)
        getPassedArguments()
    }

    private fun registerCallback(context: Context) {
        callback = context as CategorySheetCallback?
    }

    private fun getPassedArguments() {
        category = arguments?.getString(ARG_CATEGORY).toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialogStatusBar)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_category, container, false)
        navigationView = view.findViewById(R.id.navigation_view)
        navigationView.background = requireContext().getShortDrawable(R.drawable.sheet_round_corner)
        navigationView.menu.getItem(getNumSelectedItemMenu()).isChecked = true
        navigationView.setColorStateMenuItem(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationView.setNavigationItemSelectedListener { onMenuItemsClick(it) }
    }

    private fun onMenuItemsClick(menuItem: MenuItem): Boolean {
        val menuId = menuItem.itemId

        when (menuId) {
            R.id.nav_item_note -> category = CATEGORY_ALL_NOTES
            R.id.nav_item_favorites -> category = CATEGORY_FAVORITES
            R.id.nav_item_trash -> category = CATEGORY_TRASH
            R.id.nav_item_settings -> IntentManager.openSettings(requireContext())
        }

        if (menuId != R.id.nav_item_settings) callback?.onChangeCategory(category)

        dismiss()
        return true
    }


    private fun getNumSelectedItemMenu(): Int =
        when (category) {
            CATEGORY_ALL_NOTES -> 0
            CATEGORY_FAVORITES -> 1
            CATEGORY_TRASH -> 2
            else -> 0
        }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    //--- interface --------------------------------------------------------------------------------
    interface CategorySheetCallback {
        fun onChangeCategory(category: String)
    }

    //--- object --------------------------------------------------------------------------------
    companion object {
        const val TAG = "BottomSheetDialogFragmentTag"
        private const val ARG_CATEGORY = "arg_category"

        fun newInstance(category: String) = CategorySheetFragment().apply {
            arguments = Bundle().apply { putString(ARG_CATEGORY, category) }
        }
    }
}