package com.loskon.noteminimalism3.app.screens.notetrash.domain

import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteTrashRepository {
    fun getNote(id: Long): Flow<Note>
}