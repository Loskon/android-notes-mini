package com.loskon.noteminimalism3.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.room.AppRepository
import kotlinx.coroutines.launch

class NoteDetailViewModel : ViewModel() {

    companion object {
        private val TAG = "MyLogs_${NoteDetailViewModel::class.java.simpleName}"
    }

    var insertedId = 0L

    init {
        Log.d(TAG, "initialization")
    }

    private val repository = AppRepository.getRepository()

    fun insertWithId(note: Note2) {
        viewModelScope.launch {
            insertedId = repository.insertWithId(note)
        }
    }

    fun update(note: Note2) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    fun delete(note: Note2) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }
}