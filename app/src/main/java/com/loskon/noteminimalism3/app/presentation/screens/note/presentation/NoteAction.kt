package com.loskon.noteminimalism3.app.presentation.screens.note.presentation

sealed class NoteAction {
    class CreateBackupAction(val folderPath: String) : NoteAction()
    class Action2(val message: String) : NoteAction()
    class ShowErrorMessage(val message: String) : NoteAction()
    object Action4 : NoteAction()
}