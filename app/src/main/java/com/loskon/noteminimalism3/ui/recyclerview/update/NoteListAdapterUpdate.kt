package com.loskon.noteminimalism3.ui.recyclerview.update

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.databinding.ItemProfileBinding
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.recyclerview.profile.NoteDiffUtil
import com.loskon.noteminimalism3.ui.recyclerview.profile.NoteListViewHolder
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Адаптер для работы со списком заметок
 */

class NoteListAdapterUpdate : SelectableAdapterUpdate<NoteListViewHolder>() {

    private var list = emptyList<Note2>()

    private var isSelectionMode: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val view: ItemProfileBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_profile,
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
                getLinearLayout.background = this
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
        callback?.onSelectingItem(getSelectedItemCount(), hasAllSelected())
    }

    private fun hasAllSelected(): Boolean {
        return getSelectedItemCount() == itemCount
    }

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

    fun setListNote(newList: List<Note2>) {
        val diffUtil = NoteDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil, false)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItemNote(position: Int): Note2 {
        return list[position]
    }

    fun getListNote(): List<Note2> {
        return list
    }

    fun disableDeleteMode() {
        isSelectionMode = false
        resetSelectedItems()
        clearSelectionItems()
    }

    fun selectAllItems() {
        selectAllItem(list, hasAllSelected())
        itemChanged()
        callback?.onSelectingItem(getSelectedItemCount(), hasAllSelected())
    }

    fun itemChanged() {
        notifyItemRangeChanged(0, itemCount)
    }

    // Callback
    interface CallbackAdapter {
        fun onClickingItem(note: Note2)
        fun onActivationDeleteMode(isDeleteMode: Boolean)
        fun onSelectingItem(selectedItemCount: Int, hasAllSelected: Boolean)
    }

    companion object {
        private val TAG = "MyLogs_${NoteListAdapterUpdate::class.java.simpleName}"

        private var callback: CallbackAdapter? = null

        fun callbackAdapterListener(callback: CallbackAdapter) {
            this.callback = callback
        }
    }
}