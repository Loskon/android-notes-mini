package com.loskon.noteminimalism3.app.screens.notetrash.presentation

import com.loskon.noteminimalism3.app.screens.notetrash.domain.NoteTrashInteractor
import com.loskon.noteminimalism3.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

class NoteTrashViewModel(
    private val noteTrashInteractor: NoteTrashInteractor
) : BaseViewModel() {

    private val noteState: MutableStateFlow<Note> = MutableStateFlow(Note())
    val getNoteState get() = noteState.asStateFlow()

    fun getNote(id: Long) {
        launchErrorJob {
            noteTrashInteractor.getNote(id).collectLatest { note -> noteState.emit(note) }
        }
    }

    fun update(note: Note) {
        TODO("Not yet implemented")
    }

    fun delete(note: Note) {
        TODO("Not yet implemented")
    }
}