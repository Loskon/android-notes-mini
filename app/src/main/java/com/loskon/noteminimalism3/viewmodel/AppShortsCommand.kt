package com.loskon.noteminimalism3.viewmodel

import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter

class AppShortsCommand {

    private val dateBaseAdapter = DateBaseAdapter.getDateBase()

    fun getNotes(searchTerm: String?, notesCategory: String, sortingWay: Int): List<Note2> {
        return dateBaseAdapter.getNotes(searchTerm, notesCategory, sortingWay)
    }

    fun insert(note: Note2) {
        dateBaseAdapter.insert(note)
    }

    fun insertGetId(note: Note2): Long {
        return dateBaseAdapter.insertGetId(note)
    }

    fun update(note: Note2) {
        dateBaseAdapter.update(note)
    }

    fun delete(note: Note2) {
        dateBaseAdapter.delete(note)
    }
}