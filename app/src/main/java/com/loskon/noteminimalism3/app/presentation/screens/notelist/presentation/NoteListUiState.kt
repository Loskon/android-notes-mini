package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import com.loskon.noteminimalism3.model.Note

data class NoteListUiState(
    val notes: List<Note> = emptyList(),
    val category: String = NoteListViewModel.CATEGORY_ALL_NOTES1
)
