package com.loskon.noteminimalism3.app.screens.note.presentation

import com.loskon.noteminimalism3.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.app.screens.note.domain.AutoBackupInteractor
import com.loskon.noteminimalism3.app.screens.note.domain.NoteInteractor
import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import java.io.File

class NoteViewModel(
    private val noteInteractor: NoteInteractor,
    private val autoBackupInteractor: AutoBackupInteractor
) : BaseViewModel() {

    private val noteState: MutableStateFlow<Note> = MutableStateFlow(Note())
    val getNoteState get() = noteState.asStateFlow()

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

    fun performBackup(databasePath: String, backupFilePath: String): Boolean {
        return autoBackupInteractor.performBackup(databasePath, backupFilePath)
    }

    fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int) {
        autoBackupInteractor.deleteExtraFiles(backupFolderPath, maxFilesCount)
    }

    fun backupFolderCreated(folderPath: String): Boolean {
        return autoBackupInteractor.folderCreated(folderPath)
    }

    fun createTextFile(file: File, fileTitle: String, text: String): Boolean {
        return autoBackupInteractor.createTextFile(file, fileTitle, text)
    }
}