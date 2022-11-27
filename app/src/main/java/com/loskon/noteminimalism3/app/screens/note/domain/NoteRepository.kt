package com.loskon.noteminimalism3.app.screens.note.domain

import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun getNoteAsFlow(id: Long): Flow<Note>

    fun insertNote(note: Note)
    fun updateNote(note: Note)
    fun deleteNote(note: Note)
    fun insertGetId(note: Note): Long
}