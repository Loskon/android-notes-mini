package com.loskon.noteminimalism3.app.presentation.screens.note.presentation

import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.app.presentation.screens.note.domain.NoteInteractor
import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

class NoteViewModel(
    private val noteInteractor: NoteInteractor
) : BaseViewModel() {

    private val noteState = MutableStateFlow(Note())
    private val savedNoteState = MutableStateFlow(Note())
    val getNoteState get() = noteState.asStateFlow()
    val getSavedNoteState get() = savedNoteState.asStateFlow()

    fun getNote(id: Long) {
        launchErrorJob {
            noteInteractor.getNote(id).collectLatest { note -> noteState.emit(note) }
        }
    }

    fun insertGetId(note: Note): Long {
        return noteInteractor.insertGetId(note)
    }

    fun setNote(note: Note) {
        noteState.tryEmit(note)
    }

    fun delete(note: Note) {
        noteInteractor.delete(note)
    }

    fun update(note: Note) {
        noteInteractor.update(note)
    }
}