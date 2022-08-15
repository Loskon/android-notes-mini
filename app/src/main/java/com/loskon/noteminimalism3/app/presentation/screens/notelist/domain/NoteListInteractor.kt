package com.loskon.noteminimalism3.app.presentation.screens.notelist.domain

import com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation.NoteListViewModel.Companion.CATEGORY_ALL_NOTES1
import com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation.NoteListViewModel.Companion.CATEGORY_FAVORITES1
import com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation.NoteListViewModel.Companion.CATEGORY_TRASH1
import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.time.LocalDateTime

class NoteListInteractor(
    private val noteListRepository: NoteListRepository
) {

    private val searchFlow = MutableStateFlow<String?>(null)

    suspend fun getNotes(category: String, sort: Int): Flow<List<Note>> {
        return combine(
            noteListRepository.getNotesAsFlow(),
            searchFlow
        ) { notes, query ->
            notes
                .filter { note -> filterByQuery(note, query) && filterByCategory(note, category) }
                .sortedByDescending { note -> sortedByDate(note, category, sort) }
        }
    }

    private fun filterByQuery(note: Note, query: String?): Boolean {
        return note.title.lowercase().contains(query?.lowercase() ?: "")
    }

    private fun filterByCategory(note: Note, category: String?): Boolean {
        return when (category) {
            CATEGORY_ALL_NOTES1 -> note.isDeleted.not()
            CATEGORY_FAVORITES1 -> note.isFavorite
            CATEGORY_TRASH1 -> note.isDeleted
            else -> note.isDeleted.not()
        }
    }

    private fun sortedByDate(note: Note, category: String?, sort: Int): LocalDateTime {
        return if (category == CATEGORY_TRASH1) {
            note.deletedDate
        } else {
            if (sort == 1) {
                note.modifiedDate
            } else {
                note.createdDate
            }
        }
    }

    suspend fun searchNotes(query: String?) {
        searchFlow.emit(query)
    }

    fun deleteNote(note: Note) {
        noteListRepository.deleteNote(note)
    }

    fun updateNote(note: Note) {
        noteListRepository.updateNote(note)
    }

    fun deleteNotes(list: List<Note>) {
        noteListRepository.deleteNotes(list)
    }

    fun cleanTrash() {
        noteListRepository.cleanTrash()
    }

    fun updateNotes(list: List<Note>) {
        noteListRepository.updateNotes(list)
    }
}