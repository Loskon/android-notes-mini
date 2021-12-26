package com.loskon.noteminimalism3.commands

import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter
import java.util.*

/**
 * Быстрый доступ к методам БД
 */

class CommandCenter {

    private val dateBaseAdapter: DataBaseAdapter = DataBaseAdapter.getDateBase()

    fun getNotes(searchTerm: String?, notesCategory: String, sortingWay: Int): List<Note> {
        return dateBaseAdapter.getNotes(searchTerm, notesCategory, sortingWay)
    }

    // primary
    fun insert(note: Note) {
        dateBaseAdapter.insert(note)
    }

    fun update(note: Note) {
        dateBaseAdapter.update(note)
    }

    fun delete(note: Note) {
        dateBaseAdapter.delete(note)
    }

    // second
    fun insertWithIdReturn(note: Note): Long {
        return dateBaseAdapter.insertWithIdReturn(note)
    }

    fun sendToTrash(note: Note) {
        note.dateDelete = Date()
        note.isDelete = true
        note.isFavorite = false
        dateBaseAdapter.update(note)
    }

    fun resetFromTrash(note: Note, hasFavStatus: Boolean) {
        note.isFavorite = hasFavStatus
        note.isDelete = false
        dateBaseAdapter.update(note)
    }

    fun cleanTrash() {
        dateBaseAdapter.cleanTrash()
    }

    fun deleteAll() {
        dateBaseAdapter.deleteAll()
    }
}