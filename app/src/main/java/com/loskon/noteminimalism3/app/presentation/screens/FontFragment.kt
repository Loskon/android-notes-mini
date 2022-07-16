package com.loskon.noteminimalism3.app.presentation.screens

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.context.getFontKtx
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.app.base.snaphelper.CenteredSnapHelper
import com.loskon.noteminimalism3.app.base.widget.recyclerview.BoundsOffsetDecoration
import com.loskon.noteminimalism3.databinding.FragmentFontBinding
import com.loskon.noteminimalism3.managers.setNavigationIconColor
import com.loskon.noteminimalism3.model.Font
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.dialogs.WarningFontDialog
import com.loskon.noteminimalism3.ui.recyclerview.AppItemAnimator
import com.loskon.noteminimalism3.viewbinding.viewBinding

class FontFragment : Fragment(R.layout.fragment_font) {

    private val binding by viewBinding(FragmentFontBinding::bind)
    private val viewModel: FontViewModel by viewModels()

    private var fontAdapter: FontNewAdapter = FontNewAdapter()

    private var color: Int = 0
    private var savedPosition: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        color = AppPreference.getColor(context)
        savedPosition = AppPreference.getFontType(context)
        viewModel.setFontList(createFontList())
    }

    private fun createFontList(): List<Font> {
        return listOf(
            getFont(0, R.string.roboto_title, R.font.roboto_light),
            getFont(1, R.string.lato_light_title, R.font.lato_light),
            getFont(2, R.string.open_sans_light_title, R.font.open_sans_light),
            getFont(3, R.string.oswald_light_title, R.font.oswald_light),
            getFont(4, R.string.source_sans_pro_light_title, R.font.source_sans_pro_light),
            getFont(5, R.string.montserrat_light_title, R.font.montserrat_light),
            getFont(6, R.string.roboto_condensed_light_title, R.font.roboto_condensed_light),
            getFont(7, R.string.poppins_light_title, R.font.poppins_light),
            getFont(8, R.string.ubuntu_light_title, R.font.ubuntu_light),
            getFont(9, R.string.dosis_light_title, R.font.dosis_light),
            getFont(10, R.string.titillium_web_light_title, R.font.titillium_web_light),
            getFont(11, R.string.ibm_plex_serif_light_title, R.font.ibm_plex_serif_light),
            getFont(12, R.string.karla_light_title, R.font.karla_light),
            getFont(13, R.string.marmelad_regular_title, R.font.marmelad_regular),
            getFont(14, R.string.nunito_light_title, R.font.nunito_light),
            getFont(15, R.string.alice_regular_title, R.font.alice_regular)
        )
    }

    private fun getFont(id: Int, stringId: Int, fontId: Int): Font {
        return Font(id, getString(stringId), requireContext().getFontKtx(fontId))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerView()
        configureSnapHelper()
        configureBottomBar()
        configureTvExampleFont()
        setupAdapterListener()
        installObserver()
        showWarningFontDialog()
    }

    private fun configureRecyclerView() {
        with(binding.rvFont) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            addItemDecoration(BoundsOffsetDecoration())
            itemAnimator = AppItemAnimator()
            adapter = fontAdapter
            setHasFixedSize(true)
        }
    }

    private fun configureSnapHelper() {
        val snapHelper = CenteredSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvFont)
        snapHelper.setCenterPosition(savedPosition)
    }

    private fun configureBottomBar() {
        with(binding.bottomBarFont) {
            setNavigationIconColor(color)
            setDebounceNavigationClickListener { requireActivity().onBackPressed() }
        }
    }

    private fun configureTvExampleFont() {
        binding.tvFontExample.typeface = viewModel.getFontListState.value[savedPosition].typeFace
    }

    private fun setupAdapterListener() {
        fontAdapter.setItemOnClickListener { font, position ->
            binding.rvFont.smoothScrollToPosition(position)
            binding.tvFontExample.typeface = font.typeFace
            AppPreference.setTypeFont(requireContext(), font.id)
        }
    }

    private fun installObserver() {
        viewModel.getFontListState.observe(viewLifecycleOwner) { fonts ->
            if (fonts.isNotEmpty()) {
                binding.tvFontExample.typeface = fonts[savedPosition].typeFace
                fontAdapter.updateFontList(fonts)
            }
        }
    }

    private fun showWarningFontDialog() {
        val showDialog = AppPreference.showWarningFontDialog(requireContext())
        if (showDialog) WarningFontDialog(requireContext()).show()
    }
}