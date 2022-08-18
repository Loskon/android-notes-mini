package com.loskon.noteminimalism3.app.presentation.screens.notelist.domain

import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteListRepository {

    suspend fun getNotesAsFlow(): Flow<List<Note>>
    fun deleteNote(note: Note)
    fun updateNote(note: Note)
    fun deleteNotes(list: List<Note>)
    fun cleanTrash()
    fun updateNotes(list: List<Note>)
    fun insertNote(note: Note)
}