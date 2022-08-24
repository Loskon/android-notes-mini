package com.loskon.noteminimalism3.app.presentation.screens.note

import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DatabaseAdapterNew
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NoteRepositoryImpl(
    private val databaseAdapter: DatabaseAdapterNew
) : NoteRepository {

    override suspend fun getNoteAsFlow(id: Long): Flow<Note> {
        return flow {
            emit(databaseAdapter.getNote(id))
        }
    }

    override fun insertNote(note: Note) {
        TODO("Not yet implemented")
    }

    override fun updateNote(note: Note) {
        TODO("Not yet implemented")
    }

    override fun deleteNote(note: Note) {
        TODO("Not yet implemented")
    }

}