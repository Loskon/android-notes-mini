package com.loskon.noteminimalism3.app.presentation.screens.notelist.domain

import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteListRepository {

    suspend fun getNotes(): Flow<List<Note>>
    suspend fun deleteItem(note: Note)
}