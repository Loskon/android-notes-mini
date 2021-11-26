package com.loskon.noteminimalism3.command

import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter

/**
 * Быстрый доступ к методам работы с БД
 */

class ShortsCommandReceivingData {

    private val dateBaseAdapter = DateBaseAdapter.getDateBase()

    fun getNotes(searchTerm: String?, notesCategory: String, sortingWay: Int): List<Note> {
        return dateBaseAdapter.getNotes(searchTerm, notesCategory, sortingWay)
    }

    fun insert(note: Note) {
        dateBaseAdapter.insert(note)
    }

    fun update(note: Note) {
        dateBaseAdapter.update(note)
    }

    fun delete(note: Note) {
        dateBaseAdapter.delete(note)
    }
}