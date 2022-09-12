package com.loskon.noteminimalism3.app.base.widget.preference

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.setBackgroundTintColorKtx
import com.loskon.noteminimalism3.app.base.extension.view.setTextSizeKtx
import com.loskon.noteminimalism3.sharedpref.AppPreference

class NoteCardViewPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    private var view: View? = null
    private var textViewTitle: TextView? = null
    private var textViewDate: TextView? = null

    init {
        layoutResource = R.layout.preference_note_card_view
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.itemView.isClickable = false

        val color = AppPreference.getColor(context)
        val titleFontSize = AppPreference.getTitleFontSize(context)
        val dateFontSize = AppPreference.getDateFontSize(context)

        view = holder.findViewById(R.id.view_favorite)
        textViewTitle = holder.findViewById(R.id.tv_card_note_title) as TextView
        textViewDate = holder.findViewById(R.id.tv_card_note_date) as TextView

        view?.setBackgroundTintColorKtx(color)
        textViewTitle?.setTextSizeKtx(titleFontSize)
        textViewDate?.setTextSizeKtx(dateFontSize)
        textViewTitle?.text = context.getString(R.string.title_card_view)
        textViewDate?.text = context.getString(R.string.date_card_view)
    }

    fun setTextSizes(titleFontSize: Int, dateFontSize: Int) {
        textViewTitle?.setTextSizeKtx(titleFontSize)
        textViewDate?.setTextSizeKtx(dateFontSize)
    }
}