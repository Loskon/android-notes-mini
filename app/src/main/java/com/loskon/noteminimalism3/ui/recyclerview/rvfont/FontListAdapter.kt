package com.loskon.noteminimalism3.ui.recyclerview.rvfont

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setRadioButtonColor

/**
 * Адаптер для работы со списком заметок
 */

class FontListAdapter : RecyclerView.Adapter<FontListAdapter.FontViewHolder>() {

    private var list: List<FontModel> = emptyList()

    private var color: Int = 0
    private var lastCheckedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_list_item, parent, false)
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

    fun setFontsList(newList: List<FontModel>) {
        list = newList
        itemsChanged()
    }

    fun setCheckedPosition(newCheckedPosition: Int) {
        lastCheckedPosition = newCheckedPosition
    }

    fun setColor(color: Int) {
        this.color = color
    }

    fun getNote(position: Int): FontModel {
        return list[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun itemsChanged() {
        notifyDataSetChanged()
    }

    class FontViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title_font)
        var exampleText: TextView = itemView.findViewById(R.id.example_text_font)
        var radioButton: RadioButton = itemView.findViewById(R.id.radioButton)
    }

    interface CallbackFontAdapter {
        fun onClickingItem(item: FontModel)
    }

    companion object {
        private var callback: CallbackFontAdapter? = null

        fun listenerCallback(callback: CallbackFontAdapter) {
            this.callback = callback
        }
    }
}