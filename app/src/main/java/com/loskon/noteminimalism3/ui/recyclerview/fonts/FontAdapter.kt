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
import com.loskon.noteminimalism3.base.extension.view.setBackgroundColorKtx
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.model.FontType

/**
 * Адаптер для работы со списком шрифтов
 */

class FontAdapter : RecyclerView.Adapter<FontViewHolder>() {

    private lateinit var clickListener: FontClickListener

    private var list: List<FontType> = emptyList()

    private var color: Int = 0
    private var lastCheckedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_font, parent, false)
        return FontViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        val fontType: FontType = list[position]

        with(holder) {
            title.configureTitleText(fontType)
            exampleText.typeface = fontType.typeFace
            radioButton.configureRadioButton(position)
            card.setDebounceClickListener { onItemClick(fontType, position) }
        }
    }

    private fun TextView.configureTitleText(fontType: FontType) {
        text = fontType.title
        typeface = fontType.typeFace
    }

    private fun RadioButton.configureRadioButton(position: Int) {
        isChecked = (position == lastCheckedPosition)
        setBackgroundColorKtx(color)
    }

    private fun onItemClick(fontType: FontType, absoluteAdapterPosition: Int) {
        lastCheckedPosition = absoluteAdapterPosition
        notifyItemRangeChanged(0, itemCount)
        clickListener.onFontClick(fontType)
    }

    //----------------------------------------------------------------------------------------------
    fun setFontList(newList: List<FontType>) {
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
        fun onFontClick(fontType: FontType)
    }

    fun registerFontClickListener(clickListener: FontClickListener) {
        this.clickListener = clickListener
    }
}

class FontViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val card: CardView = itemView.findViewById(R.id.card_view_row_font)
    val title: TextView = itemView.findViewById(R.id.tv_title_font_card)
    val exampleText: TextView = itemView.findViewById(R.id.tv_font_example_card)
    val radioButton: RadioButton = itemView.findViewById(R.id.rb_font_card)
}