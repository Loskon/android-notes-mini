package com.loskon.noteminimalism3.ui.fragments

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
 * Нижний лист для выбора категории заметок и открытия настроек
 */

class BottomSheetCategory : BottomSheetDialogFragment() {

    private lateinit var callback: CallbackCategory

    private lateinit var mContext: Context

    private lateinit var navigationView: NavigationView

    private var category: String = CATEGORY_ALL_NOTES

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listenerCallback(context)
        getPassedArguments()
        mContext = context
    }

    private fun listenerCallback(context: Context) {
        callback = context as CallbackCategory
    }

    private fun getPassedArguments() {
        category = arguments?.getString(ARG_CATEGORY).toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetStatusBar)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottomsheet, container, false)
        navigationView = view.findViewById(R.id.navigation_view)
        navigationView.background = mContext.getShortDrawable(R.drawable.sheet_round_corner)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationView.apply {

            menu.getItem(getNumSelectedItemMenu()).isChecked = true

            setColorStateMenuItem(mContext)

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
                        IntentManager.openSettings(mContext)
                    }
                }

                if (menuId != R.id.nav_item_settings) {
                    callback.onCallbackCategory(category)
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

    interface CallbackCategory {
        fun onCallbackCategory(notesCategory: String)
    }

    companion object {
        const val TAG = "BottomSheetDialogFragment"
        private const val ARG_CATEGORY = "arg_category"

        @JvmStatic
        fun newInstance(category: String) = BottomSheetCategory().apply {
            arguments = Bundle().apply {
                putString(ARG_CATEGORY, category)
            }
        }
    }
}