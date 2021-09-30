package com.loskon.noteminimalism3.ui.recyclerview.update

import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.activities.update.MainActivityUpdate
import com.loskon.noteminimalism3.ui.snackbars.SnackbarMessage
import com.loskon.noteminimalism3.viewmodel.AppShortsCommand
import java.util.*

/**
 * Адаптер для помощи в выделении элементов
 */

abstract class SelectableAdapterUpdate<VH : RecyclerView.ViewHolder?> : RecyclerView.Adapter<VH>() {

    private val selectedItems: ArrayList<Note2> = ArrayList<Note2>()

    var colorStroke: Int = 0
    var borderStroke: Int = 0
    var radiusStroke: Int = 0

    var radiusStrokeDp = 0
    var boredStrokeDp: Int = 0
    var color: Int = 0

    // Установка параметров для цветной рамки на выбранных представлениях
    fun setVariablesGradDraw(radius: Int, border: Int, color: Int) {
        radiusStroke = radius
        borderStroke = border
        colorStroke = color
    }

    // Переключение статуса выбора элемента в заданной позиции
    fun toggleSelection(note: Note2, position: Int) {
        note.isChecked = !note.isChecked

        if (note.isChecked) {
            selectedItems.add(note)
        } else {
            selectedItems.remove(note)
        }

        notifyItemChanged(position)
    }

    // Очистить список выбранных элементов
    fun clearSelectionItems() {
        if (selectedItems.size != 0) {
            selectedItems.clear()
        }
    }

    // Снять статус выбора для выбранных элементов
    fun resetSelectedItems() {
        if (selectedItems.size != 0) {
            for (item in selectedItems) {
                item.isChecked = false
            }
        }
    }

    // Подсчет выбранных элементов
    val selectedItemsCount: Int
        get() {
            return selectedItems.size
        }

    // Выбрать/снять выбор всех элементов списка
    fun selectAllItem(list: List<Note2>, hasAllSelected: Boolean) {
        clearSelectionItems()

        if (hasAllSelected) {
            for (item in list) {
                item.isChecked = false
                selectedItems.remove(item)
            }
        } else {
            for (item in list) {
                item.isChecked = true
                selectedItems.add(item)
            }
        }
    }

    // Отправить выбранные элементы в корзину
    fun sendItemsToTrash(shortsCommand: AppShortsCommand) {
        for (item in selectedItems) {
            item.isDelete = true
            shortsCommand.update(item)
        }

        clearSelectionItems()
    }

    // Удалить навсегда выбранные элементы
    fun removeItems(shortsCommand: AppShortsCommand) {
        for (item in selectedItems) {
            shortsCommand.delete(item)
        }

        clearSelectionItems()
    }

    // Объединить несколько заметок в одну новую
    fun unificationItems(activity: MainActivityUpdate, shortsCommand: AppShortsCommand) {
        val stringBuilder: StringBuilder = StringBuilder()
        val note = Note2()
        var newTitle = ""
        var isFavorite = false

        try {
            for (item in selectedItems) {
                newTitle = uniteTitlesItems(item, stringBuilder)
                if (item.isFavorite) isFavorite = true
                shortsCommand.delete(item)
            }

            note.isFavorite = isFavorite
            note.title = newTitle
            shortsCommand.insert(note)

            activity.showSnackbarMessage(SnackbarMessage.MSG_COMBINED_NOTE_ADD, true)

        } catch (exception: Exception) {
            exception.stackTraceToString()

            shortsCommand.delete(note)

            for (item in selectedItems) {
                shortsCommand.delete(item)
                shortsCommand.insert(item)
            }

            activity.showSnackbarMessage(SnackbarMessage.MSG_ERROR_COMBINING_NOTES, false)
        }
    }

    private fun uniteTitlesItems(note: Note2, stringBuilder: StringBuilder): String {
        // Защита от добавления пустых строк для последнего объединенного текста
        val title = note.title.trim()

        if (note !== selectedItems[selectedItems.size - 1]) {
            stringBuilder.append(title).append("\n\n")
        } else {
            stringBuilder.append(title)
        }

        return stringBuilder.toString()
    }
}