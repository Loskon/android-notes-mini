package com.loskon.noteminimalism3.ui.recyclerview.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.databinding.ItemProfileBinding
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import java.util.*
import kotlin.collections.ArrayList


/**
 * Адаптер для работы со списком тренировочных профилей
 */

class NoteListAdapter : RecyclerView.Adapter<NoteListViewHolder>() {

    private var list = emptyList<Note2>()

    private val removeItemsList = ArrayList<Note2>()
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

        holder.apply {
            bind(note)

            itemView.setOnSingleClickListener {
                handlingClick(note)
            }

            itemView.setOnLongClickListener {
                handlingLongClick(note)
                true
            }
        }
    }

    private fun handlingClick(note: Note2) {
        if (isSelectionMode) {
            onItemSelection(note)
        } else {
            clickListener?.onItemClick(note)
        }
    }

    private fun handlingLongClick(note: Note2) {
        if (isSelectionMode) {

        } else {
            isSelectionMode = true
            clickListener?.onDeleteMode(true)
            removeItemsList.clear()

            //for (item in list) note.isChecked = false
        }

        onItemSelection(note)
    }

    private fun onItemSelection(note: Note2) {
        if (note.isChecked) {
            removeItemsList.remove(note)
        } else {
            removeItemsList.add(note)
        }

        clickListener?.onSelectedItem(note, removeItemsList.size)
    }

    // Other methods
    fun setListNote(newList: List<Note2>) {
        val diffUtil = NoteDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil, false)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItemNote(position: Int): Note2 {
        return list[position]
    }

    fun disableDeleteMode() {
        isSelectionMode = false
    }

    fun getRemoveList() : List<Note2> = removeItemsList.toList()


    // Callback
    interface OnItemClickListener {
        fun onItemClick(note: Note2)
        fun onSelectedItem(note: Note2, numSelItem: Int)
        fun onDeleteMode(isDelMode: Boolean)
    }

    companion object {
        private var clickListener: OnItemClickListener? = null

        fun setClickListener(clickListener: OnItemClickListener) {
            this.clickListener = clickListener
        }
    }
}