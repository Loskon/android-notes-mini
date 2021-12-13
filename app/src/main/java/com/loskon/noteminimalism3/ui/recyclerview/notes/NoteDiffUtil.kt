package com.loskon.noteminimalism3.ui.recyclerview.notes

import androidx.recyclerview.widget.DiffUtil
import com.loskon.noteminimalism3.model.Note

/**
 * Вычисление обновления списка
 */

class NoteDiffUtil(
    private val oldList: List<Note>,
    private val newList: List<Note>
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