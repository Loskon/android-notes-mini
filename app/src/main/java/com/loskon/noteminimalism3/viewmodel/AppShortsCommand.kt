package com.loskon.noteminimalism3.viewmodel

import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter

class AppShortsCommand {

    private val baseAdapter = DateBaseAdapter.getDateBase()

    fun getNotes(notesCategory: String, sortingWay: Int): List<Note2> {
        return baseAdapter.getNotes(notesCategory, sortingWay)
    }

    fun insert(note: Note2) {
        baseAdapter.insert(note)
    }

    fun insertGetId(note: Note2): Long {
        return baseAdapter.insertGetId(note)
    }

    fun update(note: Note2) {
        baseAdapter.update(note)
    }

    fun delete(note: Note2) {
        baseAdapter.delete(note)
    }

    fun deleteById(id: Long) {
        baseAdapter.deleteById(id)
    }
}