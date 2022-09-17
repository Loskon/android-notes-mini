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
import com.loskon.noteminimalism3.base.extension.view.setBackgroundTintColorKtx
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.commands.CommandCenter
import com.loskon.noteminimalism3.databinding.ItemNoteBinding
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.utils.changeTextSize

/**
 * Адаптер для работы со списком заметок
 */

class NoteRecyclerAdapter : NoteSelectableAdapter<NoteViewHolder>() {

    private lateinit var actionListener: NoteActionListener

    private var list: List<Note> = emptyList()

    private var hasLinearList: Boolean = true
    private var hasOneSizeCards: Boolean = false
    private var isSelectedMode: Boolean = false
    private var titleFontSize: Int = 0
    private var dateFontSize: Int = 0
    private var numberLines: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: ItemNoteBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_note,
            parent,
            false
        )

        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note: Note = list[position]
        val gradientDrawable = GradientDrawable()

        holder.apply {
            bind(note)

            viewFavorite.setBackgroundTintColorKtx(color)
            title.configureTitleText()
            date.changeTextSize(dateFontSize)

            card.setDebounceClickListener { onItemClick(note, position) }
            card.setOnLongClickListener { onItemLongClick(note, position) }

            setVariablesGradDraw(note.isChecked)
            gradientDrawable.configureGradDraw(linearLayout)
        }
    }

    private fun TextView.configureTitleText() {
        changeTextSize(titleFontSize)
        maxLines = numberLines
        minLines = lines
    }

    private val lines: Int
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

    private fun GradientDrawable.configureGradDraw(linLayout: LinearLayout) {
        cornerRadius = radiusStroke.toFloat()
        setStroke(borderStroke, colorStroke)
        linLayout.background = this
    }

    private fun onItemClick(note: Note, position: Int) {
        if (isSelectedMode) {
            selectingItem(note, position)
        } else {
            actionListener.onNoteClick(note)
        }
    }

    private fun selectingItem(note: Note, position: Int) {
        toggleSelection(note, position)
        actionListener.onSelectingNote(selectedItemsCount, hasAllSelected)
        if (selectedItemsCount in 1..1) actionListener.onChangeStatusOfFavorite(selectedItem)
    }

    private val hasAllSelected get() = (selectedItemsCount == itemCount)

    private fun onItemLongClick(note: Note, position: Int): Boolean {
        if (!isSelectedMode) activationDeleteMode()
        selectingItem(note, position)
        return true
    }

    private fun activationDeleteMode() {
        isSelectedMode = true
        actionListener.onActivatingSelectionMode()
        clearSelectionItems()
    }

    //----------------------------------------------------------------------------------------------
    fun setViewsColor(color: Int) {
        this.color = color
    }

    fun setLinearList(hasLinearList: Boolean) {
        this.hasLinearList = hasLinearList
    }

    fun setViewSizes(radiusStrokeDp: Int, boredStrokeDp: Int) {
        this.radiusStrokeDp = radiusStrokeDp
        this.boredStrokeDp = boredStrokeDp
    }

    fun setFontSizes(titleFontSize: Int, dateFontSize: Int) {
        this.titleFontSize = titleFontSize
        this.dateFontSize = dateFontSize
    }

    fun setNumberLines(numberLines: Int) {
        this.numberLines = numberLines
    }

    fun setOneSizeCards(hasOneSizeCards: Boolean) {
        this.hasOneSizeCards = hasOneSizeCards
    }

    //----------------------------------------------------------------------------------------------
    fun setNoteList(newList: List<Note>) {
        val diffUtil = NoteDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil, false)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun setQuicklyNoteList(newList: List<Note>) {
        list = newList
        updateChangedList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateChangedList() = notifyDataSetChanged()

    fun getNote(position: Int): Note = list[position]

    //----------------------------------------------------------------------------------------------
    fun disableSelectionMode() {
        isSelectedMode = false
        resetSelectedItems()
        clearSelectionItems()
    }

    fun selectAllNotes() {
        selectAllItem(list, hasAllSelected)
        actionListener.onSelectingNote(selectedItemsCount, hasAllSelected)
        updateChangedList()
    }

    fun changeFavoriteStatus(commandCenter: CommandCenter) {
        changeFavorite(commandCenter)
        actionListener.onChangeStatusOfFavorite(selectedItem)
        updateChangedList()
    }

    override fun showSnackbar(messageType: String) {
        actionListener.onShowSnackbar(messageType)
    }

    //--- interface --------------------------------------------------------------------------------
    interface NoteActionListener {
        fun onNoteClick(note: Note)
        fun onActivatingSelectionMode()
        fun onSelectingNote(selectedItemsCount: Int, hasAllSelected: Boolean)
        fun onChangeStatusOfFavorite(note: Note)
        fun onShowSnackbar(messageType: String)
    }

    fun registerNoteClickListener(actionListener: NoteActionListener) {
        this.actionListener = actionListener
    }
}