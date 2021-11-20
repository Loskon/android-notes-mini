package com.loskon.noteminimalism3.ui.fragments

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.model.Font
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.dialogs.DialogWarning
import com.loskon.noteminimalism3.ui.recyclerview.CustomItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.fonts.CustomSnapHelper
import com.loskon.noteminimalism3.ui.recyclerview.fonts.FontListAdapter
import com.loskon.noteminimalism3.utils.getShortFont

/**
 * Форма для выбора шрифта в приложении
 */

class FontsFragment : Fragment(), FontListAdapter.CallbackFontAdapter {

    private lateinit var activity: SettingsActivity

    private var adapter: FontListAdapter = FontListAdapter()

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvExample: TextView

    private var savedPosition: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SettingsActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fonts, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_fonts)
        tvExample = view.findViewById(R.id.tv_example_font)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        installCallbacks()
        configurationBottomBar()
        configureRecyclerView()
        configureRecyclerAdapter()
        updateFontsList()
        installSnapHelper()
        configureOtherViews()
        showWarningDialog()
    }

    private fun installCallbacks() {
        FontListAdapter.listenerCallback(this)
    }

    private fun configurationBottomBar() {
        activity.apply {
            bottomBar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun configureRecyclerView() {
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = CustomItemAnimator()
    }

    private fun configureRecyclerAdapter() {
        savedPosition = PrefManager.getTypeFont(activity)
        adapter.setCheckedPosition(savedPosition)

        val color: Int = PrefManager.getAppColor(activity)
        adapter.setColor(color)
    }

    private fun updateFontsList() {
        val tweets: List<Font> = getListFonts()
        adapter.setFontsList(tweets)
    }

    private fun getListFonts(): List<Font> {
        return listOf(
            Font(
                0,
                getString(R.string.default_font),
                activity.getShortFont(R.font.roboto_light)
            ),

            Font(
                1,
                getString(R.string.lato_light_font),
                activity.getShortFont(R.font.lato_light)
            ),

            Font(
                2,
                getString(R.string.open_sans_light_font),
                activity.getShortFont(R.font.open_sans_light)
            ),

            Font(
                3,
                getString(R.string.oswald_light_font),
                activity.getShortFont(R.font.oswald_light)
            ),

            Font(
                4,
                getString(R.string.source_sans_pro_light_font),
                activity.getShortFont(R.font.source_sans_pro_light)
            ),

            Font(
                5,
                getString(R.string.montserrat_light_font),
                activity.getShortFont(R.font.montserrat_light)
            ),

            Font(
                6,
                getString(R.string.roboto_condensed_light_font),
                activity.getShortFont(R.font.roboto_condensed_light)
            ),

            Font(
                7,
                getString(R.string.poppins_light_font),
                activity.getShortFont(R.font.poppins_light)
            ),

            Font(
                8,
                getString(R.string.ubuntu_light_font),
                activity.getShortFont(R.font.ubuntu_light)
            ),

            Font(
                9,
                getString(R.string.dosis_light_font),
                activity.getShortFont(R.font.dosis_light)
            ),

            Font(
                10,
                getString(R.string.titillium_web_light_font),
                activity.getShortFont(R.font.titillium_web_light)
            ),

            Font(
                11,
                getString(R.string.ibm_plex_serif_light_font),
                activity.getShortFont(R.font.ibm_plex_serif_light)
            ),

            Font(
                12,
                getString(R.string.karla_light_font),
                activity.getShortFont(R.font.karla_light)
            ),

            Font(
                13,
                getString(R.string.marmelad_regular_font),
                activity.getShortFont(R.font.marmelad_regular)
            ),

            Font(
                14,
                getString(R.string.nunito_light_font),
                activity.getShortFont(R.font.nunito_light)
            ),

            Font(
                15,
                getString(R.string.alice_regular_font),
                activity.getShortFont(R.font.alice_regular)
            )
        )
    }

    private fun installSnapHelper() {
        val snapHelper = CustomSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        snapHelper.setCenterPosition(savedPosition)
    }

    private fun configureOtherViews() {
        tvExample.typeface = getListFonts()[savedPosition].font_type_face
    }

    private fun showWarningDialog() {
        if (PrefManager.isDialogShow(activity)) {
            DialogWarning(activity).show()
        }
    }

    override fun onClickingItem(item: Font) {
        tvExample.typeface = item.font_type_face
        PrefManager.setTypeFont(activity, item.id)
        activity.setAppFonts()
        callback?.onChangeTypeFont(item.font_type_face)
    }

    interface CallbackTypeFont {
        fun onChangeTypeFont(typeFace: Typeface?)
    }

    companion object {
        private var callback: CallbackTypeFont? = null

        fun listenerCallback(callback: CallbackTypeFont) {
            this.callback = callback
        }
    }
}