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
            noteListRepository.getNotes(),
            searchFlow
        ) { notes, search ->
            notes
                .filter { note -> note.title.lowercase().contains(search?.lowercase() ?: "") }
                .filter { note -> filterByCategory(note, category) }
                .sortedByDescending { note -> sortedByDate(note, category, sort) }
        }
    }

    suspend fun searchNotes(query: String?) {
        searchFlow.emit(query)
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
}