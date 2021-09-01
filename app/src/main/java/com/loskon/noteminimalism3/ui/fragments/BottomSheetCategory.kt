package com.loskon.noteminimalism3.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.utils.getShortDrawable
import com.loskon.noteminimalism3.utils.setColorStateMenuItem
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Companion.CATEGORY_FAVORITES
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Companion.CATEGORY_TRASH

/**
 *
 */

class BottomSheetCategory : BottomSheetDialogFragment() {

    private lateinit var navigationView: NavigationView
    private var category: String = CATEGORY_ALL_NOTES

    override fun onAttach(context: Context) {
        super.onAttach(context)
        category = arguments?.getString(ARG_CATEGORY).toString()

    }

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
        navigationView.apply {
            menu.getItem(getNumSelectedItemMenu()).isChecked = true

            setColorStateMenuItem(requireContext())

            setNavigationItemSelectedListener { menuItem: MenuItem ->
                val menuId = menuItem.itemId

                when (menuId) {
                    R.id.nav_item_note -> {
                        category = CATEGORY_ALL_NOTES
                    }
                    R.id.nav_item_favorites -> {
                        category = CATEGORY_FAVORITES
                    }
                    R.id.nav_item_trash -> {
                        category = CATEGORY_TRASH
                    }
                    R.id.nav_item_settings -> {
                        val intent = Intent(activity, SettingsActivity::class.java)
                        startActivity(intent)
                    }
                }

                if (menuId != R.id.nav_item_settings) {
                    navViewListener?.onCallback(category)
                }

                dismiss()
                true
            }
        }
    }

    private fun getNumSelectedItemMenu(): Int =
        when (category) {
            CATEGORY_ALL_NOTES -> 0
            CATEGORY_FAVORITES -> 1
            CATEGORY_TRASH -> 2
            else -> 0
        }

    interface OnNavViewListener {
        fun onCallback(category: String)
    }

    companion object {
        const val TAG = "BottomSheetDialogFragment"
        private const val ARG_CATEGORY = "arg_category"

        private var navViewListener: OnNavViewListener? = null

        fun setNavViewListener(navViewListener: OnNavViewListener) {
            this.navViewListener = navViewListener
        }

        @JvmStatic
        fun newInstance(categoryInSheet: String) = BottomSheetCategory().apply {
            arguments = Bundle().apply {
                putString(ARG_CATEGORY, categoryInSheet)
            }
        }
    }
}