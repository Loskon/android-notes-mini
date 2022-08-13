package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.app.presentation.screens.notelist.domain.NoteListInteractor
import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

class NoteListViewModel(
    private val noteListInteractor: NoteListInteractor
) : BaseViewModel() {

    private val noteListState = MutableStateFlow<List<Note>>(emptyList())
    private val noteListSearchState = MutableStateFlow(false)
    private val noteListSelectionState = MutableStateFlow(false)
    val getNoteListState get() = noteListState.asStateFlow()
    val getNoteListSearchState get() = noteListSearchState.asStateFlow()
    val getNoteListSelectionState get() = noteListSelectionState.asStateFlow()

    private var job: Job? = null

    fun getNotes(category: String, sort: Int) {
        launchErrorJob {
            noteListInteractor.getNotes(category, sort).collectLatest { notes -> noteListState.emit(notes) }
        }
    }

    fun searchNotes(query: String?) {
        job?.cancel()
        job = launchErrorJob { noteListInteractor.searchNotes(query) }
    }

    fun toggleSearchMode(activateSearchMode: Boolean) {
        noteListSearchState.tryEmit(activateSearchMode)
    }

    fun toggleSelectionMode(activateSelectionMode: Boolean) {
        noteListSelectionState.tryEmit(activateSelectionMode)
    }

    fun deleteItem(note: Note) {
        launchErrorJob {
            noteListInteractor.deleteItem(note)
        }
    }

    companion object {
        const val CATEGORY_ALL_NOTES1 = "category_all_notes"
        const val CATEGORY_FAVORITES1 = "category_favorites"
        const val CATEGORY_TRASH1 = "category_trash"
    }
}