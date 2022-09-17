package com.loskon.noteminimalism3.app.screens.notelist.presentation

import com.loskon.noteminimalism3.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.app.screens.notelist.domain.NoteListInteractor
import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

class NoteListViewModel(
    private val noteListInteractor: NoteListInteractor
) : BaseViewModel() {

    private val noteListUiState = MutableStateFlow(NoteListUiState())
    private val noteListCategoryState = MutableStateFlow(CATEGORY_ALL_NOTES1)
    private val noteListSearchState = MutableStateFlow(false)
    private val noteListSelectionState = MutableStateFlow(false)
    val getNoteListUiState get() = noteListUiState.asStateFlow()
    val getNoteListCategoryState get() = noteListCategoryState.asStateFlow()
    val getNoteListSearchState get() = noteListSearchState.asStateFlow()
    val getNoteListSelectionState get() = noteListSelectionState.asStateFlow()

    private var sortWay: Int? = null

    fun setSortWay(sortWay: Int) {
        this.sortWay = sortWay
    }

    fun getNotes(scrollTop: Boolean, quicklyListUpdate: Boolean) {
        launchErrorJob {
            noteListInteractor.getNotes(noteListCategoryState.value, sortWay).collectLatest { notes ->
                noteListUiState.emit(
                    NoteListUiState(
                        notes = notes,
                        scrollTop = scrollTop,
                        quicklyListUpdate = quicklyListUpdate
                    )
                )
            }
        }
    }

    fun searchNotes(query: String?) {
        noteListInteractor.searchNotes(query)
    }

    fun toggleSearchMode(activateSearchMode: Boolean) {
        noteListSearchState.tryEmit(activateSearchMode)
    }

    fun toggleSelectionMode(activateSelectionMode: Boolean) {
        noteListSelectionState.tryEmit(activateSelectionMode)
    }

    fun setCategory(category: String) {
        noteListCategoryState.tryEmit(category)
    }

    fun deleteNote(note: Note) {
        noteListInteractor.deleteNote(note)
    }

    fun updateNote(note: Note) {
        noteListInteractor.updateNote(note)
    }

    fun deleteNotes(list: List<Note>) {
        noteListInteractor.deleteNotes(list)
    }

    fun cleanTrash() {
        noteListInteractor.cleanTrash()
    }

    fun cleanTrash(day: Int) {
        noteListInteractor.cleanTrash(day)
    }

    fun updateNotes(list: List<Note>) {
        noteListInteractor.updateNotes(list)
    }

    fun insertNote(note: Note) {
        noteListInteractor.insertNote(note)
    }

    companion object {
        const val CATEGORY_ALL_NOTES1 = "category_all_notes"
        const val CATEGORY_FAVORITES1 = "category_favorites"
        const val CATEGORY_TRASH1 = "category_trash"
    }
}