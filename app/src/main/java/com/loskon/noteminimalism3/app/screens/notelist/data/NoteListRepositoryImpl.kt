package com.loskon.noteminimalism3.app.screens.notelist.data

import com.loskon.noteminimalism3.app.screens.notelist.domain.NoteListRepository
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DatabaseAdapterNew
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NoteListRepositoryImpl(
    private val databaseAdapter: DatabaseAdapterNew
) : NoteListRepository {

    override suspend fun getNotesAsFlow(): Flow<List<Note>> {
        return flow {
            emit(databaseAdapter.getNotes())
        }
    }

    override fun deleteNote(note: Note) {
        databaseAdapter.delete(note)
    }

    override fun updateNote(note: Note) {
        databaseAdapter.update(note)
    }

    override fun deleteNotes(list: List<Note>) {
        databaseAdapter.deleteAll(list)
    }

    override fun cleanTrash() {
        databaseAdapter.cleanTrash()
    }

    override fun cleanTrash(day: Int) {
        databaseAdapter.deleteByTime(day)
    }

    override fun updateNotes(list: List<Note>) {
        databaseAdapter.updateAll(list)
    }

    override fun insertNote(note: Note) {
        databaseAdapter.insert(note)
    }
}