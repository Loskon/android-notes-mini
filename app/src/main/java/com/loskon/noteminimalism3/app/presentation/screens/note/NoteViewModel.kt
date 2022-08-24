package com.loskon.noteminimalism3.app.presentation.screens.note

import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

class NoteViewModel(
    private val noteInteractor: NoteInteractor
) : BaseViewModel() {

    private val noteState = MutableStateFlow(Note())
    val getNoteState get() = noteState.asStateFlow()

    fun getNote(id: Long) {
        launchErrorJob {
            noteInteractor.getNote(id).collectLatest { note -> noteState.emit(note) }
        }
    }
}