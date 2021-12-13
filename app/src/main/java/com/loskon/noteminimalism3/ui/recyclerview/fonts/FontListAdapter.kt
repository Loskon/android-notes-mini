package com.loskon.noteminimalism3.ui.recyclerview.fonts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setRadioButtonColor
import com.loskon.noteminimalism3.model.Font
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Адаптер для работы со списком шрифтов
 */

class FontListAdapter : RecyclerView.Adapter<FontListAdapter.FontViewHolder>() {

    private var list: List<Font> = emptyList()

    private var color: Int = 0
    private var lastCheckedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_fonts, parent, false)
        return FontViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        val item = list[position]

        holder.apply {

            title.apply {
                text = item.title
                typeface = item.font_type_face
            }

            exampleText.typeface = item.font_type_face

            radioButton.apply {
                isChecked = (position == lastCheckedPosition)
                setRadioButtonColor(color)
            }

            itemView.apply {
                setOnSingleClickListener {
                    lastCheckedPosition = absoluteAdapterPosition
                    notifyItemRangeChanged(0, itemCount)
                    callback?.onClickingItem(item)
                }
            }

        }
    }

    fun setFontsList(newList: List<Font>) {
        list = newList
        updateChangedList()
    }

    fun setCheckedPosition(newCheckedPosition: Int) {
        lastCheckedPosition = newCheckedPosition
    }

    fun setColor(color: Int) {
        this.color = color
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateChangedList() {
        notifyDataSetChanged()
    }

    class FontViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title_font)
        var exampleText: TextView = itemView.findViewById(R.id.example_text_font)
        var radioButton: RadioButton = itemView.findViewById(R.id.radio_button_font)
    }

    interface CallbackFontAdapter {
        fun onClickingItem(item: Font)
    }

    companion object {
        private var callback: CallbackFontAdapter? = null

        fun listenerCallback(callback: CallbackFontAdapter) {
            this.callback = callback
        }
    }
}