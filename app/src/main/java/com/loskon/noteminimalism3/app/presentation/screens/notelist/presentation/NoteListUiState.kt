package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import com.loskon.noteminimalism3.model.Note

data class NoteListUiState(
    val notes: List<Note> = emptyList(),
    val scrollTop: Boolean = false,
    val quicklyListUpdate: Boolean = false
)