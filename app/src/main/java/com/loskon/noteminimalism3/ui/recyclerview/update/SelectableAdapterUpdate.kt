package com.loskon.noteminimalism3.ui.recyclerview.update

import android.util.SparseBooleanArray
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.model.Note2
import java.util.*

/**
 * Адаптер для помощи в выделении элементов
 */

abstract class SelectableAdapterUpdate<VH : RecyclerView.ViewHolder?> : RecyclerView.Adapter<VH>() {

    private val selectedPositions: SparseBooleanArray = SparseBooleanArray()
    private val selectedItems: ArrayList<Note2> = ArrayList<Note2>()

    var colorStroke: Int = 0
    var borderStroke: Int = 0
    var radiusStroke: Int = 0

    var radiusStrokeDp = 0
    var boredStrokeDp: Int = 0
    var color: Int = 0

    // Установка параметров для цветной рамки на выбранных представлениях
    fun setVarGradDraw(radius: Int, border: Int, color: Int) {
        radiusStroke = radius
        borderStroke = border
        colorStroke = color
    }

    // Указывает, выбран ли элемент в позиции
    fun isSelected(position: Int): Boolean {
        return getSelectedItems().contains(position)
    }

    // Переключение статуса выбора элемента в заданной позиции
    fun toggleSelection(note: Note2, position: Int) {
        if (selectedPositions[position, false]) {
            selectedItems.remove(note)
            selectedPositions.delete(position)
        } else {
            selectedItems.add(note)
            selectedPositions.put(position, true)
        }

        notifyItemChanged(position)
    }

    // Снимите статус выбора для всех элементов
    fun clearSelection() {
        selectedPositions.clear()
        selectedItems.clear()
    }

    // Подсчитайте выбранные элементы
    fun getSelectedItemCount(): Int {
        return selectedPositions.size()
    }

    fun getSelectedItemCount2(): Int {
        return selectedItems.size
    }

    // Указывает список выбранных элементов
    private fun getSelectedItems(): List<Int> {
        val items: MutableList<Int> = ArrayList(selectedPositions.size())
        for (i in 0 until selectedPositions.size()) {
            items.add(selectedPositions.keyAt(i))
        }
        return items
    }

    // Указывает список выбранных элементов
    fun getRemoveItems(): ArrayList<Note2> {
        return selectedItems
    }
}