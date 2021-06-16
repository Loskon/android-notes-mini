package com.loskon.noteminimalism3.ui.recyclerview.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.databinding.ItemProfileBinding
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.recyclerview.FilterCustomerSearch2
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import java.util.*

/**
 * Адаптер для работы со списком тренировочных профилей
 */

class NoteListAdapter : RecyclerView.Adapter<NoteListViewHolder>(), Filterable {

    private var list = emptyList<Note2>()
    private var search = emptyList<Note2>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val view: ItemProfileBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_profile,
            parent,
            false
        )

        return NoteListViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val note = list[position]

        holder.apply {
            bind(note)

            itemView.setOnSingleClickListener {
                clickListener?.onItemClick(note)
            }

            itemView.setOnLongClickListener {
                clickListener?.onLongItemClick(note)
                true
            }
        }
    }

    override fun getItemCount(): Int = list.size

    // Other methods
    fun setListProfiles(newList: List<Note2>) {
        val diffUtil = NoteDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil, false)
        list = newList
        search = list
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItemProgram(position: Int): Note2 {
        return list[position]
    }

    override fun getFilter(): Filter? {
        return FilterCustomerSearch2(this, list, search)
    }

    fun setNotes(searchList: List<Note2>) {
        list = searchList
    }


    // Callback
    interface OnItemClickListener {
        fun onItemClick(note: Note2)
        fun onLongItemClick(note: Note2)
    }

    companion object {
        private var clickListener: OnItemClickListener? = null

        fun setClickListener(clickListener: OnItemClickListener) {
            this.clickListener = clickListener
        }
    }
}