package com.loskon.noteminimalism3.app.screens.notetrash.presentation

import com.loskon.noteminimalism3.app.screens.notetrash.domain.NoteTrashInteractor
import com.loskon.noteminimalism3.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteTrashViewModel(
    private val noteTrashInteractor: NoteTrashInteractor
) : BaseViewModel() {

    private val noteMutableStateFlow: MutableStateFlow<Note> = MutableStateFlow(Note())
    val noteStateFlow get() = noteMutableStateFlow.asStateFlow()

    fun getNote(id: Long) {
        launchErrorJob {
            noteTrashInteractor.getNote(id).collect { note -> noteMutableStateFlow.emit(note) }
        }
    }

    fun update(note: Note) {
        noteTrashInteractor.update(note)
    }

    fun delete(note: Note) {
        noteTrashInteractor.delete(note)
    }
}