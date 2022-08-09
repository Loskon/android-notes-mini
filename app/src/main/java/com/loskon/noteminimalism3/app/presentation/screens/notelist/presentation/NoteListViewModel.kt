package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.app.presentation.screens.notelist.domain.NoteListInteractor
import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

class NoteListViewModel(
    private val noteListInteractor: NoteListInteractor
) : BaseViewModel() {

    private val noteListState = MutableStateFlow<List<Note>>(emptyList())
    val getNoteListState get() = noteListState.asStateFlow()

    fun getNotes(category: String, sort: Int) {
        launchErrorJob {
            noteListInteractor.getNotes(category, sort).collectLatest {
                Timber.d(it.size.toString())
                noteListState.emit(it)
            }
        }
    }

    companion object {
        const val CATEGORY_ALL_NOTES1 = "category_all_notes"
        const val CATEGORY_FAVORITES1 = "category_favorites"
        const val CATEGORY_TRASH1 = "category_trash"
    }
}