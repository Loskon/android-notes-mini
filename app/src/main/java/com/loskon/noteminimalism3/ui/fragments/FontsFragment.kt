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
import com.loskon.noteminimalism3.sharedpref.PrefHelper
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

    private lateinit var layoutManager: LinearLayoutManager
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
        configureLayoutManager()
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
        activity.bottomBar.setNavigationOnClickListener {
            activity.onBackPressed()
        }
    }

    private fun configureLayoutManager() {
        val orientation: Int = RecyclerView.HORIZONTAL
        layoutManager = LinearLayoutManager(activity, orientation, false)
    }

    private fun configureRecyclerView() {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = CustomItemAnimator()
    }

    private fun configureRecyclerAdapter() {
        savedPosition = PrefHelper.getTypeFont(activity)
        adapter.setCheckedPosition(savedPosition)

        val color: Int = PrefHelper.getAppColor(activity)
        adapter.setColor(color)
    }

    private fun updateFontsList() {
        val fonts: List<Font> = getListFonts()
        adapter.setFontsList(fonts)
    }

    private fun getListFonts(): List<Font> {
        return listOf(
            createFont(0, R.string.roboto_name, R.font.roboto_light),
            createFont(1, R.string.lato_light_name, R.font.lato_light),
            createFont(2, R.string.open_sans_light_name, R.font.open_sans_light),
            createFont(3, R.string.oswald_light_name, R.font.oswald_light),
            createFont(4, R.string.source_sans_pro_light_name, R.font.source_sans_pro_light),
            createFont(5, R.string.montserrat_light_name, R.font.montserrat_light),
            createFont(6, R.string.roboto_condensed_light_name, R.font.roboto_condensed_light),
            createFont(7, R.string.poppins_light_name, R.font.poppins_light),
            createFont(8, R.string.ubuntu_light_name, R.font.ubuntu_light),
            createFont(9, R.string.dosis_light_name, R.font.dosis_light),
            createFont(10, R.string.titillium_web_light_name, R.font.titillium_web_light),
            createFont(11, R.string.ibm_plex_serif_light_name, R.font.ibm_plex_serif_light),
            createFont(12, R.string.karla_light_name, R.font.karla_light),
            createFont(13, R.string.marmelad_regular_name, R.font.marmelad_regular),
            createFont(14, R.string.nunito_light_name, R.font.nunito_light),
            createFont(15, R.string.alice_regular_name, R.font.alice_regular)
        )
    }

    private fun createFont(id: Int, stringId: Int, fontId: Int): Font {
        return Font(id, getString(stringId), activity.getShortFont(fontId))
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
        if (PrefHelper.isDialogShow(activity)) {
            DialogWarning(activity).show()
        }
    }

    override fun onClickingItem(item: Font) {
        tvExample.typeface = item.font_type_face
        PrefHelper.setTypeFont(activity, item.id)
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