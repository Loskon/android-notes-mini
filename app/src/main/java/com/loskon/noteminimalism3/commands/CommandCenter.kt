package com.loskon.noteminimalism3.commands

import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter
import java.util.*

/**
 * Быстрый доступ к методам БД
 */

class CommandCenter {

    private val dateBase: DataBaseAdapter = DataBaseAdapter.getDateBase()

    fun getNotes(searchTerm: String?, notesCategory: String, sortingWay: Int): List<Note> {
        return dateBase.getNotes(searchTerm, notesCategory, sortingWay)
    }

    //--- Primary methods --------------------------------------------------------------------------
    fun insert(note: Note) {
        dateBase.insert(note)
    }

    fun update(note: Note) {
        dateBase.update(note)
    }

    fun delete(note: Note) {
        dateBase.delete(note)
    }

    //--- Second methods ---------------------------------------------------------------------------
    fun insertWithIdReturn(note: Note): Long {
        return dateBase.insertWithIdReturn(note)
    }

    fun insertUnification(note: Note, isFavorite: Boolean, newTitle: String) {
        note.isFavorite = isFavorite
        note.title = newTitle
    }

    fun selectDeleteOption(category: String, note: Note) {
        if (category == DataBaseAdapter.CATEGORY_TRASH) {
            delete(note)
        } else {
            sendToTrash(note)
        }
    }

    fun sendToTrash(note: Note) {
        note.dateDelete = Date()
        note.isDelete = true
        note.isFavorite = false
        dateBase.update(note)
    }

    fun resetFromTrash(note: Note, hasFavoriteStatus: Boolean) {
        note.isFavorite = hasFavoriteStatus
        note.isDelete = false
        dateBase.update(note)
    }

    fun cleanTrash() {
        dateBase.cleanTrash()
    }

    fun deleteAll() {
        dateBase.deleteAll()
    }
}