package com.loskon.noteminimalism3.ui.recyclerview.update

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.databinding.ItemNoteBinding
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.recyclerview.profile.NoteDiffUtil
import com.loskon.noteminimalism3.ui.recyclerview.profile.NoteListViewHolder
import com.loskon.noteminimalism3.utils.setBackgroundTintColor
import com.loskon.noteminimalism3.utils.setFontSize
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Адаптер для работы со списком заметок
 */

class NoteListAdapterUpdate : SelectableAdapterUpdate<NoteListViewHolder>() {

    private var list: List<Note2> = emptyList()

    private var hasLinearList: Boolean = true
    private var hasOneSizeCards: Boolean = false
    private var isSelMode: Boolean = false
    private var titleFontSize: Int = 0
    private var dateFontSize: Int = 0
    private var numberLines: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val view: ItemNoteBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_note,
            parent,
            false
        )

        return NoteListViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val note = list[position]
        val gradientDrawable = GradientDrawable()

        holder.apply {
            bind(note)

            title.apply {
                setFontSize(titleFontSize)
                maxLines = numberLines
                minLines = getTitleMinLines()
            }

            date.setFontSize(dateFontSize)
            view.setBackgroundTintColor(color)

            itemView.apply {
                setOnSingleClickListener {
                    clickingItem(note, position)
                }

                setOnLongClickListener {
                    longClickingItem(note, position)
                    true
                }
            }

            if (note.isChecked) {
                setVariablesGradDraw(radiusStrokeDp, boredStrokeDp, color)
            } else {
                setVariablesGradDraw(0, 0, 0)
            }

            gradientDrawable.apply {
                cornerRadius = radiusStroke.toFloat()
                setStroke(borderStroke, colorStroke)
                linearLayout.background = this
            }
        }
    }

    private fun clickingItem(note: Note2, position: Int) {
        if (isSelMode) {
            selectingItem(note, position)
        } else {
            callback?.onClickingNote(note)
        }
    }

    private fun selectingItem(note: Note2, position: Int) {
        toggleSelection(note, position)
        callback?.onSelectingNote(selectedItemsCount, hasAllSelected)
    }

    private val hasAllSelected get() = (selectedItemsCount == itemCount)

    private fun longClickingItem(note: Note2, position: Int) {
        if (!isSelMode) activationDeleteMode()
        selectingItem(note, position)
    }

    private fun activationDeleteMode() {
        isSelMode = true
        callback?.onActivatingSelectionMode(true)
        clearSelectionItems()
    }

    private fun getTitleMinLines(): Int {
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

    // Обновление списка заметок
    fun setNotesList(newList: List<Note2>) {
        val diffUtil = NoteDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil, false)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun setQuicklyNotesList(newList: List<Note2>) {
        list = newList
        itemsChanged()
    }

    fun getNote(position: Int): Note2 {
        return list[position]
    }

    fun disableSelectionMode() {
        isSelMode = false
        resetSelectedItems()
        clearSelectionItems()
    }

    fun selectAllNotes() {
        selectAllItem(list, hasAllSelected)
        itemsChanged()
        callback?.onSelectingNote(selectedItemsCount, hasAllSelected)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun itemsChanged() {
        notifyDataSetChanged()
    }

    // Callback
    interface CallbackAdapter {
        fun onClickingNote(note: Note2)
        fun onActivatingSelectionMode(isSelMode: Boolean)
        fun onSelectingNote(selectedItemsCount: Int, hasAllSelected: Boolean)
    }

    companion object {
        private var callback: CallbackAdapter? = null

        fun listenerCallback(callback: CallbackAdapter) {
            this.callback = callback
        }
    }
}