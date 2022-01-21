package com.loskon.noteminimalism3.ui.recyclerview.sharednotes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.databinding.RowNoteBinding
import com.loskon.noteminimalism3.managers.setBackgroundTintColor
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.ui.recyclerview.notes.NoteViewHolder
import com.loskon.noteminimalism3.utils.changeTextSize
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Адаптер для работы со списком заметок (для Share)
 */

class ReceivingNoteRecyclerAdapter : RecyclerView.Adapter<NoteViewHolder>() {

    private lateinit var clickListener: SharedNoteClickListener

    private var list: List<Note> = emptyList()

    private var color: Int = 0
    private var titleFontSize: Int = 0
    private var dateFontSize: Int = 0
    private var numberLines: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: RowNoteBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_note,
            parent,
            false
        )

        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note: Note = list[position]

        holder.apply {
            bind(note)

            title.configureTitleText()
            date.changeTextSize(dateFontSize)
            viewFavorite.setBackgroundTintColor(color)

            card.setOnSingleClickListener { clickListener.onSharedNoteClick(note) }
        }
    }

    private fun TextView.configureTitleText() {
        changeTextSize(titleFontSize)
        maxLines = numberLines
    }

    //----------------------------------------------------------------------------------------------
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

    //----------------------------------------------------------------------------------------------
    fun setSharedNoteList(newList: List<Note>) {
        list = newList
        updateChangedList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateChangedList() = notifyDataSetChanged()

    //--- interface --------------------------------------------------------------------------------
    interface SharedNoteClickListener {
        fun onSharedNoteClick(note: Note)
    }

    fun registerSharedNoteClickListener(clickListener: SharedNoteClickListener) {
        this.clickListener = clickListener
    }
}