package com.loskon.noteminimalism3.app.presentation.screens.note.presentation

import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.app.presentation.screens.note.domain.AutoBackupInteractor
import com.loskon.noteminimalism3.app.presentation.screens.note.domain.NoteInteractor
import com.loskon.noteminimalism3.model.Note
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber
import java.io.File

class NoteViewModel(
    private val noteInteractor: NoteInteractor,
    private val autoBackupInteractor: AutoBackupInteractor
) : BaseViewModel() {

    private val _eventChannel2 = Channel<NoteAction>(Channel.BUFFERED)
    val getEvents2 get() = _eventChannel2.receiveAsFlow()

    private val _eventChannel = Channel<String>()
    val events = _eventChannel.receiveAsFlow()
    val getEvents get() = _eventChannel.receiveAsFlow()
    /*    private val _uiEffect = MutableSharedFlow<String>(replay = 1, onBufferOverflow = BufferOverflow.SUSPEND)
        val uiEffect: SharedFlow<String> = _uiEffect*/

    private val noteState: MutableStateFlow<Note> = MutableStateFlow(Note())
    val noteAction2 = MutableSharedFlow<String>()
    val noteAction = MutableSharedFlow<NoteAction>(replay = 0)
    val getNoteState get() = noteState.asStateFlow()
    val getNoteActon get() = noteAction.asSharedFlow()

    init {
        Timber.d(noteAction.toString())
    }

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

    fun performBackup(databasePath: String, backupFilePath: String, folderPath: String, maxFilesCount: Int) {
        launchErrorJob {
            val backupSuccess = autoBackupInteractor.performBackup(databasePath, backupFilePath)

            if (backupSuccess) {
                deleteExtraFiles(folderPath, maxFilesCount)
                noteAction.emit(NoteAction.Action2("YES"))
            } else {
                noteAction.emit(NoteAction.ShowErrorMessage("BAD"))
            }
        }
    }

    fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int) {
        launchErrorJob {
            autoBackupInteractor.deleteExtraFiles(backupFolderPath, maxFilesCount)
        }
    }

    fun backupFolderCreated(folderPath: String) {
        launchErrorJob {
            val folderCreated = autoBackupInteractor.folderCreated(folderPath)

            if (folderCreated) {
                //Timber.d(noteAction.toString())
                //noteAction.emit(NoteAction.CreateBackupAction(folderPath))
                _eventChannel2.send(NoteAction.CreateBackupAction(folderPath))
            }
        }


    }

    fun createTextFile(file: File, fileTitle: String, text: String) {
        launchErrorJob {
            autoBackupInteractor.createTextFile(file, fileTitle, text)
        }
    }

    fun show() {
        launchErrorJob {
            //_eventChannel.send("BAD")
            _eventChannel2.send(NoteAction.ShowErrorMessage("BAD"))
        }
    }
}