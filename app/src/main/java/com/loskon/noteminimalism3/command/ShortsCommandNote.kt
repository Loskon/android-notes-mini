package com.loskon.noteminimalism3.command

import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter

/**
 * Быстрый доступ к методам работы с БД
 */

class ShortsCommandNote {

    private val dateBaseAdapter = DateBaseAdapter.getDateBase()

    fun insert(note: Note) {
        dateBaseAdapter.insert(note)
    }

    fun insertGetId(note: Note): Long {
        return dateBaseAdapter.insertGetId(note)
    }

    fun update(note: Note) {
        dateBaseAdapter.update(note)
    }

    fun delete(note: Note) {
        dateBaseAdapter.delete(note)
    }
}