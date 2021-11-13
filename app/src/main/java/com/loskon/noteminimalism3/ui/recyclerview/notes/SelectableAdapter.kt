package com.loskon.noteminimalism3.ui.recyclerview.notes

import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.command.ShortsCommand
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.ui.activities.ListActivity
import com.loskon.noteminimalism3.ui.snackbars.SnackbarManager
import java.util.*

/**
 * Адаптер для помощи в выделении элементов
 */

abstract class SelectableAdapter<VH : RecyclerView.ViewHolder?> : RecyclerView.Adapter<VH>() {

    private val selectedItems: ArrayList<Note> = ArrayList<Note>()

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
    fun toggleSelection(note: Note, position: Int) {
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

    // Вернуть первый элемент
    val selectedItem: Note
        get() {
            return selectedItems[0]
        }

    // Выбрать/снять выбор всех элементов списка
    fun selectAllItem(list: List<Note>, hasAllSelected: Boolean) {
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
    fun sendItemsToTrash(shortsCommand: ShortsCommand) {
        for (item in selectedItems) {
            item.isDelete = true
            shortsCommand.update(item)
        }

        clearSelectionItems()
    }

    // Удалить навсегда выбранные элементы
    fun deleteItems(shortsCommand: ShortsCommand) {
        for (item in selectedItems) {
            shortsCommand.delete(item)
        }

        clearSelectionItems()
    }

    // Объединить несколько заметок в одну новую
    fun unification(activity: ListActivity, shortsCommand: ShortsCommand) {
        val stringBuilder: StringBuilder = StringBuilder()
        val note = Note()
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

            activity.showSnackbar(SnackbarManager.MSG_COMBINED_NOTE_ADD, true)

        } catch (exception: Exception) {

            shortsCommand.delete(note)

            for (item in selectedItems) {
                shortsCommand.delete(item)
                shortsCommand.insert(item)
            }

            activity.showSnackbar(SnackbarManager.MSG_ERROR_COMBINING_NOTES, false)
        }
    }

    private fun uniteTitlesItems(note: Note, stringBuilder: StringBuilder): String {
        // Защита от добавления пустых строк для последнего объединенного текста
        val title = note.title.trim()

        if (note !== selectedItems[selectedItems.size - 1]) {
            stringBuilder.append(title).append("\n\n")
        } else {
            stringBuilder.append(title)
        }

        return stringBuilder.toString()
    }

    // Отправить выбранные элементы в корзину
    fun changeFavorite(shortsCommand: ShortsCommand) {
        val note: Note = selectedItem
        note.isFavorite = !note.isFavorite
        shortsCommand.update(note)
    }
}