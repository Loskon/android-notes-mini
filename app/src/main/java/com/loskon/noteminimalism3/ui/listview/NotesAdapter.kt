package com.loskon.noteminimalism3.ui.listview

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.utils.DateUtil
import com.loskon.noteminimalism3.utils.setBackgroundTintColor
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Кастомный адаптер для вывода списка файлов резервных копий
 */

class NotesAdapter :
    BaseAdapter() {

    private var list: List<Note2> = emptyList()

    private var color: Int = 0

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(parent?.context, R.layout.item_note_old, null)
        val note = list[position]

        val title: TextView = view.findViewById(R.id.txt_title_card_note)
        val date: TextView = view.findViewById(R.id.txt_date_card_note)
        val viewFavorite: View = view.findViewById(R.id.viewFavForCard)

        title.text = note.title
        date.text = DateUtil.getStringDate(note.dateCreation)
        viewFavorite.setVisibleView(note.isFavorite)
        viewFavorite.setBackgroundTintColor(color)

        return view
    }

    fun setViewColor(color: Int) {
        this.color = color
    }

    fun setFilesList(newList: List<Note2>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Note2 = list[position]

    override fun getItemId(position: Int): Long = position.toLong()
}


