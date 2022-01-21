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
import com.loskon.noteminimalism3.utils.changeTextSize
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Адаптер для работы со списком заметок
 */

class NoteRecyclerAdapter : NoteSelectableAdapter<NoteViewHolder>() {

    private lateinit var clickListener: NoteClickListener

    private var list: List<Note> = emptyList()

    private var hasLinearList: Boolean = true
    private var hasOneSizeCards: Boolean = false
    private var isSelectedMode: Boolean = false
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
        val gradientDrawable = GradientDrawable()

        holder.apply {
            bind(note)

            viewFavorite.setBackgroundTintColor(color)
            title.configureTitleText()
            date.changeTextSize(dateFontSize)

            card.setOnSingleClickListener { onItemClick(note, position) }
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
            clickListener.onNoteClick(note)
        }
    }

    private fun selectingItem(note: Note, position: Int) {
        toggleSelection(note, position)
        clickListener.selectingNote(selectedItemsCount, hasAllSelected)
        if (selectedItemsCount in 1..1) clickListener.changeStatusOfFavorite(selectedItem)
    }

    private val hasAllSelected get() = (selectedItemsCount == itemCount)

    private fun onItemLongClick(note: Note, position: Int): Boolean {
        if (!isSelectedMode) activationDeleteMode()
        selectingItem(note, position)
        return true
    }

    private fun activationDeleteMode() {
        isSelectedMode = true
        clickListener.activatingSelectionMode()
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
        clickListener.selectingNote(selectedItemsCount, hasAllSelected)
        updateChangedList()
    }

    fun changeFavoriteStatus(activity: MainActivity, commandCenter: CommandCenter) {
        changeFavorite(activity, commandCenter)
        clickListener.changeStatusOfFavorite(selectedItem)
        updateChangedList()
    }

    //--- interface --------------------------------------------------------------------------------
    interface NoteClickListener {
        fun onNoteClick(note: Note)
        fun activatingSelectionMode()
        fun selectingNote(selectedItemsCount: Int, hasAllSelected: Boolean)
        fun changeStatusOfFavorite(note: Note)
    }

    fun registerNoteClickListener(clickListener: NoteClickListener) {
        this.clickListener = clickListener
    }
}