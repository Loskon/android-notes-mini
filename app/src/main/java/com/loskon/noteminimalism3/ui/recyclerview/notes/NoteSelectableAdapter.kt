package com.loskon.noteminimalism3.ui.recyclerview.notes

import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.commands.CommandCenter
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.ui.snackbars.SnackbarControl
import java.util.*

/**
 * Адаптер для помощи в выделении элементов
 */

abstract class NoteSelectableAdapter<VH : RecyclerView.ViewHolder?> : RecyclerView.Adapter<VH>() {

    private val selectedItems: ArrayList<Note> = ArrayList<Note>()

    var radiusStroke: Int = 0
    var borderStroke: Int = 0
    var colorStroke: Int = 0

    var radiusStrokeDp = 0
    var boredStrokeDp: Int = 0
    var color: Int = 0

    // Установка параметров для цветной рамки на выбранных представлениях
    fun setVariablesGradDraw(isChecked: Boolean) {
        if (isChecked) {
            radiusStroke = radiusStrokeDp
            borderStroke = boredStrokeDp
            colorStroke = color
        } else {
            radiusStroke = 0
            borderStroke = 0
            colorStroke = 0
        }
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
    fun sendSelectedItemsToTrash(commandCenter: CommandCenter) {
        for (item in selectedItems) {
            item.isFavorite = false
            item.isDelete = true
            commandCenter.update(item)
        }

        clearSelectionItems()
    }

    // Удалить навсегда выбранные элементы
    fun deleteItems(commandCenter: CommandCenter) {
        for (item in selectedItems) {
            commandCenter.delete(item)
        }

        clearSelectionItems()
    }

    // Объединить несколько заметок в одну новую
    fun unification(activity: MainActivity, commandCenter: CommandCenter) {
        val stringBuilder: StringBuilder = StringBuilder()
        val note = Note()
        var newTitle = ""
        var isFavorite = false

        try {

            for (item in selectedItems) {
                newTitle = uniteTitlesItems(item, stringBuilder)
                if (item.isFavorite) isFavorite = true
                commandCenter.delete(item)
            }

            note.isFavorite = isFavorite
            note.title = newTitle
            commandCenter.insert(note)

            activity.showSnackbar(SnackbarControl.MSG_COMBINED_NOTE_ADD)

        } catch (exception: Exception) {

            commandCenter.delete(note)

            for (item in selectedItems) {
                commandCenter.delete(item)
                commandCenter.insert(item)
            }

            activity.showSnackbar(SnackbarControl.MSG_ERROR_COMBINING_NOTES)
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

    // Изменить статус избранного
    fun changeFavorite(activity: MainActivity, commandCenter: CommandCenter) {
        try {
            val note: Note = selectedItem
            note.isFavorite = !note.isFavorite
            commandCenter.update(note)
        } catch (exception: Exception) {
            activity.showSnackbar(SnackbarControl.MSG_SELECT_ONE_NOTE)
        }
    }
}