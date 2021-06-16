package com.loskon.noteminimalism3.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.utils.getShortDrawable
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Category.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Category.CATEGORY_FAVORITES
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Category.CATEGORY_TRASH

class BottomSheetCategory(
    private val listFragment: NoteListFragment,
    private var categoryInSheet: String
) :
    BottomSheetDialogFragment() {

    companion object {
        const val TAG = "BottomSheetDialogFragment"

        fun newInstance(
            listFragment: NoteListFragment,
            categoryInSheet: String
        ): BottomSheetCategory {
            return BottomSheetCategory(listFragment, categoryInSheet)
        }
    }

    private lateinit var navigationView: NavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottomsheet, container, false)
        navigationView = view.findViewById(R.id.navigation_view)
        navigationView.background = requireContext().getShortDrawable(R.drawable.sheet_round_corner)
        return view
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetStatusBar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationView.menu.getItem(getNumSelectedItemMenu()).isChecked = true

        MyColor.setNavMenuItemThemeColors(requireActivity(), navigationView)

        navigationView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            val menuId = menuItem.itemId

            when (menuId) {
                R.id.nav_item_note -> {
                    categoryInSheet = CATEGORY_ALL_NOTES
                }
                R.id.nav_item_favorites -> {
                    categoryInSheet = CATEGORY_FAVORITES
                }
                R.id.nav_item_trash -> {
                    categoryInSheet = CATEGORY_TRASH
                }
                R.id.nav_item_settings -> {
                    val intent = Intent(activity, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }

            if (menuId != R.id.nav_item_settings) {
                listFragment.onClickMenuItem(categoryInSheet)
            }

            dismiss()
            true
        }
    }

    private fun getNumSelectedItemMenu(): Int =
        when (categoryInSheet) {
            CATEGORY_ALL_NOTES -> 0
            CATEGORY_FAVORITES -> 1
            CATEGORY_TRASH -> 2
            else -> 0
        }
}