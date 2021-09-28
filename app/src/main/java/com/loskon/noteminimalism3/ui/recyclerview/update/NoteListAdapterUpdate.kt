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

    private var list = emptyList<Note2>()

    private var hasLinearList: Boolean = true
    private var hasOneSizeCards: Boolean = false
    private var isSelectionMode: Boolean = false
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

                minLines = if (hasLinearList) {
                    1
                } else {
                    if (hasOneSizeCards) {
                        numberLines
                    } else {
                        1
                    }
                }
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
        if (isSelectionMode) {
            selectingItem(note, position)
        } else {
            callback?.onClickingItem(note)
        }
    }

    private fun selectingItem(note: Note2, position: Int) {
        toggleSelection(note, position)
        callback?.onSelectingItem(selectedItemsCount, hasAllSelected)
    }

    private val hasAllSelected get() = (selectedItemsCount == itemCount)

    private fun longClickingItem(note: Note2, position: Int) {
        if (!isSelectionMode) activationDeleteMode()
        selectingItem(note, position)
    }

    private fun activationDeleteMode() {
        isSelectionMode = true
        callback?.onActivationDeleteMode(true)
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

    fun disableDeleteMode() {
        isSelectionMode = false
        resetSelectedItems()
        clearSelectionItems()
    }

    fun selectAllNotes() {
        selectAllItem(list, hasAllSelected)
        itemsChanged()
        callback?.onSelectingItem(selectedItemsCount, hasAllSelected)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun itemsChanged() {
        notifyDataSetChanged()
    }

    // Callback
    interface CallbackAdapter {
        fun onClickingItem(note: Note2)
        fun onActivationDeleteMode(isDeleteMode: Boolean)
        fun onSelectingItem(selectedItemCount: Int, hasAllSelected: Boolean)
    }

    companion object {
        private var callback: CallbackAdapter? = null

        fun callbackAdapterListener(callback: CallbackAdapter) {
            this.callback = callback
        }
    }
}