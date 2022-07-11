package com.loskon.noteminimalism3.ui.recyclerview.fonts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setRadioButtonColor
import com.loskon.noteminimalism3.model.Font
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Адаптер для работы со списком шрифтов
 */

class FontListAdapter : RecyclerView.Adapter<FontViewHolder>() {

    private lateinit var clickListener: FontClickListener

    private var list: List<Font> = emptyList()

    private var color: Int = 0
    private var lastCheckedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_fonts, parent, false)
        return FontViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        val font: Font = list[position]

        with(holder) {
            title.configureTitleText(font)
            exampleText.typeface = font.typeFace
            radioButton.configureRadioButton(position)
            card.setOnSingleClickListener { onItemClick(font, position) }
        }
    }

    private fun TextView.configureTitleText(font: Font) {
        text = font.title
        typeface = font.typeFace
    }

    private fun RadioButton.configureRadioButton(position: Int) {
        isChecked = (position == lastCheckedPosition)
        setRadioButtonColor(color)
    }

    private fun onItemClick(font: Font, absoluteAdapterPosition: Int) {
        lastCheckedPosition = absoluteAdapterPosition
        notifyItemRangeChanged(0, itemCount)
        clickListener.onFontClick(font)
    }

    //----------------------------------------------------------------------------------------------
    fun setFontList(newList: List<Font>) {
        list = newList
        updateChangedList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateChangedList() = notifyDataSetChanged()

    //----------------------------------------------------------------------------------------------
    fun setCheckedPosition(newCheckedPosition: Int) {
        lastCheckedPosition = newCheckedPosition
    }

    fun setViewColor(color: Int) {
        this.color = color
    }

    //--- interface --------------------------------------------------------------------------------
    interface FontClickListener {
        fun onFontClick(font: Font)
    }

    fun registerFontClickListener(clickListener: FontClickListener) {
        this.clickListener = clickListener
    }
}

class FontViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val card: CardView = itemView.findViewById(R.id.card_view_row_font)
    val title: TextView = itemView.findViewById(R.id.title_row_font)
    val exampleText: TextView = itemView.findViewById(R.id.example_text_row_font)
    val radioButton: RadioButton = itemView.findViewById(R.id.radio_button_row_font)
}