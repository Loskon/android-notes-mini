package com.loskon.noteminimalism3.app.presentation.screens.note

import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.Flow

class NoteInteractor(
    private val noteRepository: NoteRepository
    ) {

    suspend fun getNote(id: Long): Flow<Note> {
        return noteRepository.getNoteAsFlow(id)
    }
}