package com.loskon.noteminimalism3.commands

import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DatabaseAdapter
import java.time.LocalDateTime

/**
 * Быстрый доступ к методам БД
 */

class CommandCenter {

    private val dateBase: DatabaseAdapter = DatabaseAdapter.getInstance()

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
        dateBase.insert(note)
    }

    fun selectDeleteOption(category: String, note: Note) {
        if (category == DatabaseAdapter.CATEGORY_TRASH) {
            delete(note)
        } else {
            sendToTrash(note)
        }
    }

    fun sendToTrash(note: Note) {
        note.deletedDate = LocalDateTime.now()
        note.isDeleted = true
        note.isFavorite = false
        dateBase.update(note)
    }

    fun resetFromTrash(note: Note, hasFavoriteStatus: Boolean) {
        note.isFavorite = hasFavoriteStatus
        note.isDeleted = false
        dateBase.update(note)
    }

    fun cleanTrash() {
        dateBase.cleanTrash()
    }

    fun deleteAll() {
        dateBase.deleteAll()
    }
}