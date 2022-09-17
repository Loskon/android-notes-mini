package com.loskon.githubapi.app.base.widget.recyclerview

import androidx.recyclerview.widget.DiffUtil

/**
 * DiffUtil for items comparisons
 */
class RecyclerDiffUtil<T : Any>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val listDiffUtil: RecyclerListDiffUtil<T>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listDiffUtil.areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listDiffUtil.areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }
}

interface RecyclerListDiffUtil<T> {

    fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    fun areContentsTheSame(oldItem: T, newItem: T): Boolean
}