package com.loskon.noteminimalism3.app.presentation.screens.fontlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.fragment.getFont
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.app.base.snaphelper.CenteredSnapHelper
import com.loskon.noteminimalism3.app.base.widget.recyclerview.BoundsOffsetDecoration
import com.loskon.noteminimalism3.databinding.FragmentFontTypeBinding
import com.loskon.noteminimalism3.managers.AppFont
import com.loskon.noteminimalism3.managers.setNavigationIconColor
import com.loskon.noteminimalism3.model.FontType
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.dialogs.WarningFontDialog
import com.loskon.noteminimalism3.ui.recyclerview.AppItemAnimator
import com.loskon.noteminimalism3.viewbinding.viewBinding

class FontListFragment : Fragment(R.layout.fragment_font_type) {

    private val binding by viewBinding(FragmentFontTypeBinding::bind)
    private val viewModel: FontListViewModel by viewModels()

    private val fontTypesAdapter = FontTypesListAdapter()
    private val snapHelper = CenteredSnapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.setFontList(createFontList())
    }

    private fun createFontList(): List<FontType> {
        return listOf(
            getFontType(0, R.string.roboto_title, R.font.roboto_light),
            getFontType(1, R.string.lato_light_title, R.font.lato_light),
            getFontType(2, R.string.open_sans_light_title, R.font.open_sans_light),
            getFontType(3, R.string.oswald_light_title, R.font.oswald_light),
            getFontType(4, R.string.source_sans_pro_light_title, R.font.source_sans_pro_light),
            getFontType(5, R.string.montserrat_light_title, R.font.montserrat_light),
            getFontType(6, R.string.roboto_condensed_light_title, R.font.roboto_condensed_light),
            getFontType(7, R.string.poppins_light_title, R.font.poppins_light),
            getFontType(8, R.string.ubuntu_light_title, R.font.ubuntu_light),
            getFontType(9, R.string.dosis_light_title, R.font.dosis_light),
            getFontType(10, R.string.titillium_web_light_title, R.font.titillium_web_light),
            getFontType(11, R.string.ibm_plex_serif_light_title, R.font.ibm_plex_serif_light),
            getFontType(12, R.string.karla_light_title, R.font.karla_light),
            getFontType(13, R.string.marmelad_regular_title, R.font.marmelad_regular),
            getFontType(14, R.string.nunito_light_title, R.font.nunito_light),
            getFontType(15, R.string.alice_regular_title, R.font.alice_regular)
        )
    }

    private fun getFontType(id: Int, stringId: Int, fontId: Int): FontType {
        return FontType(id, getString(stringId), getFont(fontId))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishViewsColor()
        configureRecyclerView()
        configureSnapHelper()
        setupViewsListener()
        installObserver()
        showWarningFontDialog()
    }

    private fun establishViewsColor() {
        val color = AppPreference.getColor(requireContext())
        binding.bottomBarFont.setNavigationIconColor(color)
    }

    private fun configureRecyclerView() {
        with(binding.rvFont) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            addItemDecoration(BoundsOffsetDecoration())
            itemAnimator = AppItemAnimator()
            adapter = fontTypesAdapter
            setHasFixedSize(true)
        }
    }

    private fun configureSnapHelper() {
        snapHelper.attachToRecyclerView(binding.rvFont)
    }

    private fun setupViewsListener() {
        fontTypesAdapter.setItemOnClickListener { fontType, position ->
            binding.rvFont.smoothScrollToPosition(position)
            binding.tvFontExample.typeface = fontType.typeFace
            AppPreference.setFontType(requireContext(), fontType.id)
            AppFont.set(requireContext())
        }
        binding.bottomBarFont.setDebounceNavigationClickListener {
            findNavController().popBackStack()
        }
    }

    private fun installObserver() {
        viewModel.getFontTypeListState.observe(viewLifecycleOwner) { fontTypes ->
            if (fontTypes.isNotEmpty()) {
                val savedPosition = AppPreference.getFontType(requireContext())
                binding.tvFontExample.typeface = fontTypes[savedPosition].typeFace
                snapHelper.setCenterPosition(savedPosition)
                fontTypesAdapter.updateFontList(fontTypes)
            }
        }
    }

    private fun showWarningFontDialog() {
        val showDialog = AppPreference.showWarningFontDialog(requireContext())
        if (showDialog) WarningFontDialog(requireContext()).show()
    }
}