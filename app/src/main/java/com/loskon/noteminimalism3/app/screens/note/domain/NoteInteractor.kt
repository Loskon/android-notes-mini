package com.loskon.noteminimalism3.app.screens.note.domain

import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.Flow

class NoteInteractor(
    private val noteRepository: NoteRepository
) {

    suspend fun getNote(id: Long): Flow<Note> {
        return noteRepository.getNoteAsFlow(id)
    }

    fun insertGetId(note: Note): Long {
        return noteRepository.insertGetId(note)
    }

    fun delete(note: Note) {
        noteRepository.deleteNote(note)
    }

    fun update(note: Note) {
        noteRepository.updateNote(note)
    }
}