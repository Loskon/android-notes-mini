package com.loskon.noteminimalism3.ui.recyclerview.notes

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.commands.CommandCenter
import com.loskon.noteminimalism3.databinding.RowNoteBinding
import com.loskon.noteminimalism3.managers.setBackgroundTintColor
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setTextSizeShort

/**
 * Адаптер для работы со списком заметок
 */

class NotesListAdapter : SelectableAdapter<NotesListViewHolder>() {

    private var list: List<Note> = emptyList()

    private var hasLinearList: Boolean = true
    private var hasOneSizeCards: Boolean = false
    private var isSelectedMode: Boolean = false
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
        val note: Note = list[position]
        val gradientDrawable = GradientDrawable()

        holder.apply {
            bind(note)

            viewFavorite.setBackgroundTintColor(color)
            title.configureTitle()
            date.setTextSizeShort(dateFontSize)

            itemView.apply {
                setOnSingleClickListener {
                    clickingItem(note, position)
                }

                setOnLongClickListener {
                    longClickingItem(note, position)
                    true
                }
            }

            setVariablesGradDraw(note.isChecked)
            gradientDrawable.configureGradDraw(linearLayout)
        }
    }

    private fun TextView.configureTitle() {
        setTextSizeShort(titleFontSize)
        maxLines = numberLines
        minLines = titleMinLines
    }

    private fun GradientDrawable.configureGradDraw(linearLayout: LinearLayout) {
        cornerRadius = radiusStroke.toFloat()
        setStroke(borderStroke, colorStroke)
        linearLayout.background = this
    }

    private val titleMinLines: Int
        get() {
            return if (hasLinearList) {
                1
            } else {
                if (hasOneSizeCards) {
                    numberLines
                } else {
                    1
                }
            }
        }

    private fun clickingItem(note: Note, position: Int) {
        if (isSelectedMode) {
            selectingItem(note, position)
        } else {
            callback?.onClickingNote(note)
        }
    }

    private fun selectingItem(note: Note, position: Int) {
        toggleSelection(note, position)
        callback?.onSelectingNote(selectedItemsCount, hasAllSelected)
        if (selectedItemsCount in 1..1) callback?.onVisibleFavorite(selectedItem)
    }

    private val hasAllSelected get() = (selectedItemsCount == itemCount)

    private fun longClickingItem(note: Note, position: Int) {
        if (!isSelectedMode) activationDeleteMode()
        selectingItem(note, position)
    }

    private fun activationDeleteMode() {
        isSelectedMode = true
        callback?.onActivatingSelectionMode(true)
        clearSelectionItems()
    }

    // Внешние методы
    fun setViewSizes(radiusStrokeDp: Int, boredStrokeDp: Int) {
        this.radiusStrokeDp = radiusStrokeDp
        this.boredStrokeDp = boredStrokeDp
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

    fun setLinearList(hasLinearList: Boolean) {
        this.hasLinearList = hasLinearList
    }

    fun setOneSizeCards(hasOneSizeCards: Boolean) {
        this.hasOneSizeCards = hasOneSizeCards
    }

    fun setNotesList(newList: List<Note>) {
        val diffUtil = NoteDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil, false)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun setQuicklyNotesList(newList: List<Note>) {
        list = newList
        updateChangedList()
    }

    fun getNote(position: Int): Note {
        return list[position]
    }

    fun disableSelectionMode() {
        isSelectedMode = false
        resetSelectedItems()
        clearSelectionItems()
    }

    fun selectAllNotes() {
        selectAllItem(list, hasAllSelected)
        callback?.onSelectingNote(selectedItemsCount, hasAllSelected)
        updateChangedList()
    }

    fun changeFavoriteStatus(activity: MainActivity, commandCenter: CommandCenter) {
        changeFavorite(activity, commandCenter)
        callback?.onVisibleFavorite(selectedItem)
        updateChangedList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateChangedList() {
        notifyDataSetChanged()
    }

    interface CallbackNoteAdapter {
        fun onClickingNote(note: Note)
        fun onActivatingSelectionMode(isSelMode: Boolean)
        fun onSelectingNote(selectedItemsCount: Int, hasAllSelected: Boolean)
        fun onVisibleFavorite(note: Note)
    }

    companion object {
        private var callback: CallbackNoteAdapter? = null

        fun listenerCallback(callback: CallbackNoteAdapter) {
            this.callback = callback
        }
    }
}