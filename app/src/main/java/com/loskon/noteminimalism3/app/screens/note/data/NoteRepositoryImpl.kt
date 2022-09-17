package com.loskon.noteminimalism3.app.screens.note.data

import com.loskon.noteminimalism3.app.screens.note.domain.NoteRepository
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
        databaseAdapter.insert(note)
    }

    override fun updateNote(note: Note) {
        databaseAdapter.update(note)
    }

    override fun deleteNote(note: Note) {
        databaseAdapter.delete(note)
    }

    override fun insertGetId(note: Note): Long {
        return databaseAdapter.insertGetId(note)
    }
}