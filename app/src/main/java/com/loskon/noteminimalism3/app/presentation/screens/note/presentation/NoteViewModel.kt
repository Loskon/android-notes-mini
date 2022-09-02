package com.loskon.noteminimalism3.app.presentation.screens.note.presentation

import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.app.presentation.screens.note.domain.AutoBackupInteractor
import com.loskon.noteminimalism3.app.presentation.screens.note.domain.NoteInteractor
import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import java.io.File

class NoteViewModel(
    private val noteInteractor: NoteInteractor,
    private val autoBackupInteractor: AutoBackupInteractor
) : BaseViewModel() {

    private val noteState = MutableStateFlow(Note())
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

    fun performBackup(databasePath: String, backupFilePath: String) {
        launchErrorJob {
            autoBackupInteractor.performBackup(databasePath, backupFilePath)
        }
    }

    fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int) {
        launchErrorJob {
            autoBackupInteractor.deleteExtraFiles(backupFolderPath, maxFilesCount)
        }
    }

    fun folderCreated(folderPath: String) {
        launchErrorJob {
            autoBackupInteractor.folderCreated(folderPath)
        }
    }

    fun createTextFile(file: File, fileTitle: String, text: String) {
        launchErrorJob {
            autoBackupInteractor.createTextFile(file, fileTitle, text)
        }
    }
}