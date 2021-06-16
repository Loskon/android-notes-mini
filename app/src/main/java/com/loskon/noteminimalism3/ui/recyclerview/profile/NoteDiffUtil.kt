package com.loskon.noteminimalism3.ui.recyclerview.profile

import androidx.recyclerview.widget.DiffUtil
import com.loskon.noteminimalism3.model.Note2

/**
 * Вычисление обновления адаптера RecyclerView для Profile
 */

class NoteDiffUtil(
    private val oldList: List<Note2>,
    private val newList: List<Note2>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return false
    }
}