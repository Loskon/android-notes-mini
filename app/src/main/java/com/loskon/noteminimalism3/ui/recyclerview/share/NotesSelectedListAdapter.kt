package com.loskon.noteminimalism3.ui.recyclerview.share

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.databinding.RowNoteBinding
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.ui.recyclerview.notes.NotesListViewHolder
import com.loskon.noteminimalism3.utils.setBackgroundTintColor
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setTextSizeShort

/**
 * Адаптер для работы со списком заметок (для Share)
 */

class NotesSelectedListAdapter : RecyclerView.Adapter<NotesListViewHolder>() {

    private var list: List<Note> = emptyList()

    private var color: Int = 0
    private var titleFontSize: Int = 0
    private var dateFontSize: Int = 0
    private var numberLines: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesListViewHolder {
        val view: RowNoteBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_note,
            parent,
            false
        )

        return NotesListViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NotesListViewHolder, position: Int) {
        val note = list[position]

        holder.apply {
            bind(note)

            title.apply {
                setTextSizeShort(titleFontSize)
                maxLines = numberLines
            }

            date.setTextSizeShort(dateFontSize)
            viewFavorite.setBackgroundTintColor(color)

            itemView.setOnSingleClickListener{
                callback?.onClickingNote(note)
            }
        }
    }

    fun setViewColor(color: Int) {
        this.color = color
    }

    fun setFontSizes(titleFontSize: Int, dateFontSize: Int) {
        this.titleFontSize = titleFontSize
        this.dateFontSize = dateFontSize
    }

    fun setNumberLines(numberLines: Int) {
        this.numberLines = numberLines
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilesList(newList: List<Note>) {
        list = newList
        notifyDataSetChanged()
    }

    interface CallbackSendAdapter {
        fun onClickingNote(note: Note)
    }

    companion object {
        private var callback: CallbackSendAdapter? = null

        fun listenerCallback(callback: CallbackSendAdapter) {
            this.callback = callback
        }
    }
}