package com.loskon.noteminimalism3.ui.recyclerview.update

import android.util.SparseBooleanArray

import androidx.recyclerview.widget.RecyclerView


abstract class SelectableAdapter<VH : RecyclerView.ViewHolder?> : RecyclerView.Adapter<VH>() {

    private val selectedItems: SparseBooleanArray = SparseBooleanArray()

    // Указывает, выбран ли элемент в позиции
    fun isSelected(position: Int): Boolean {
        return getSelectedItems().contains(position)
    }

    // Переключение статуса выбора элемента в заданной позиции
    fun toggleSelection(position: Int) {
        if (selectedItems[position, false]) {
            selectedItems.delete(position)
        } else {
            selectedItems.put(position, true)
        }

        notifyItemChanged(position)
    }

    // Снимите статус выбора для всех элементов
    fun clearSelection() {
        val selection = getSelectedItems()
        selectedItems.clear()
        for (i in selection) {
            notifyItemChanged(i)
        }
    }

    fun removeSelection() {
        val selection = getSelectedItems()
        selectedItems.clear()
        for (i in selection) {
            notifyItemRemoved(i)
        }
    }

    // Подсчитайте выбранные элементы
    val selectedItemCount: Int = selectedItems.size()


    // Указывает список выбранных элементов
    fun getSelectedItems(): List<Int> {
        val items: MutableList<Int> = ArrayList(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }
}