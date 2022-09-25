package com.loskon.noteminimalism3.app.screens.notetrash.data

import com.loskon.noteminimalism3.app.screens.notetrash.domain.NoteTrashRepository
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DatabaseAdapterNew
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NoteTrashRepositoryImpl(
    private val databaseAdapter: DatabaseAdapterNew
) : NoteTrashRepository {

    override fun getNote(id: Long): Flow<Note> {
        return flow {
            emit(databaseAdapter.getNote(id))
        }
    }
}