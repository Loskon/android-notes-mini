package com.loskon.noteminimalism3.app.screens.notelist.domain

import com.loskon.noteminimalism3.app.screens.notelist.presentation.NoteListViewModel
import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.time.LocalDateTime

class NoteListInteractor(
    private val noteListRepository: NoteListRepository
) {

    private val searchFlow = MutableStateFlow<String?>(null)

    suspend fun getNotes(category: String, sort: Int?): Flow<List<Note>> {
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
            NoteListViewModel.CATEGORY_ALL_NOTES1 -> note.isDeleted.not()
            NoteListViewModel.CATEGORY_FAVORITES1 -> note.isFavorite
            NoteListViewModel.CATEGORY_TRASH1 -> note.isDeleted
            else -> note.isDeleted.not()
        }
    }

    private fun sortedByDate(note: Note, category: String?, sort: Int?): LocalDateTime {
        return if (category == NoteListViewModel.CATEGORY_TRASH1) {
            note.deletedDate
        } else {
            if (sort == SORT_BY_MODIFIED_DATE) {
                note.modifiedDate
            } else {
                note.createdDate
            }
        }
    }

    fun searchNotes(query: String?) {
        searchFlow.tryEmit(query)
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

    fun cleanTrash(day: Int) {
        noteListRepository.cleanTrash(day)
    }

    fun updateNotes(list: List<Note>) {
        noteListRepository.updateNotes(list)
    }

    fun insertNote(note: Note) {
        noteListRepository.insertNote(note)
    }

    companion object {
        // TODO
        private const val SORT_BY_MODIFIED_DATE = 1
    }
}