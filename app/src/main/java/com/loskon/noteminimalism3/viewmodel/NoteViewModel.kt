package com.loskon.noteminimalism3.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.room.AppRepository
import kotlinx.coroutines.launch

/**
 *
 */

private val TAG = "MyLogs_${NoteViewModel::class.java.simpleName}"

class NoteViewModel : ViewModel() {

    companion object {
        const val CATEGORY_ALL_NOTES = "CATEGORY_ALL_NOTES"
        const val CATEGORY_FAVORITES = "CATEGORY_FAVORITES"
        const val CATEGORY_TRASH = "CATEGORY_TRASH"
    }

    private var currentOrder = CATEGORY_ALL_NOTES

    //private var currentSaveOrder = currentOrder
    private val repository = AppRepository.getRepository()
    private val getSearchList = MutableLiveData<String>()

    val getNotes = MediatorLiveData<List<Note2>>()

    val getNotesById: LiveData<List<Note2>> = repository.getNotesById().asLiveData()

    fun getNotesById(): LiveData<List<Note2>> = repository.getNotesById().asLiveData()

    private fun getNotesByFavorite(): LiveData<List<Note2>> = repository.getNotesByFavorite().asLiveData()

    fun getNotesByTrash(): LiveData<List<Note2>> = repository.getNotesByTrash().asLiveData()

    private val getNotesByFavorite: LiveData<List<Note2>> = repository.getNotesByFavorite().asLiveData()

    private val getNotesByTrash: LiveData<List<Note2>> = repository.getNotesByTrash().asLiveData()

    val getNotesBySearch: LiveData<List<Note2>> =
        Transformations.switchMap(getSearchList) { query ->
            if (TextUtils.isEmpty(query)) {
                getNotes
            } else {
                when (currentOrder) {
                    CATEGORY_FAVORITES -> getNotesSearchByFavorite(query)
                    CATEGORY_TRASH -> getNotesSearchByTrash(query)
                    else -> getNotesSearchById(query)
                }
            }
        }

    private fun getNotesSearchById(query: String): LiveData<List<Note2>> =
        repository.getNotesSearchById(query).asLiveData()

    private fun getNotesSearchByFavorite(query: String): LiveData<List<Note2>> =
        repository.getNotesSearchByFavorite(query).asLiveData()

    private fun getNotesSearchByTrash(query: String): LiveData<List<Note2>> =
        repository.getNotesSearchByTrash(query).asLiveData()


    init {
        Log.d(TAG, "initialization")

        getNotes.addSource(getNotesById) { result ->
            if (currentOrder == CATEGORY_ALL_NOTES) {
                result?.let { getNotes.value = it }
            }
        }

        getNotes.addSource(getNotesByFavorite) { result ->
            if (currentOrder == CATEGORY_FAVORITES) {
                result?.let { getNotes.value = it }
            }
        }

        getNotes.addSource(getNotesByTrash) { result ->
            if (currentOrder == CATEGORY_TRASH) {
                result?.let { getNotes.value = it }
            }
        }

        disableCheckedStatus()
    }

    fun categoryNotes(order: String) = when (order) {
        CATEGORY_FAVORITES -> getNotesByFavorite.value.let { getNotes.value = it }
        CATEGORY_TRASH -> getNotesByTrash.value.let { getNotes.value = it }
        else -> getNotesById.value.let { getNotes.value = it }
    }.also { currentOrder = order }

    fun searchNameChanged(query: String) {
        // getSearchList.value = query
    }

/*    fun setCategory(string: String) {
        currentSaveOrder = string
    }*/

    fun deleteItemsAlways() {
        viewModelScope.launch {
            repository.deleteItemsAlways()
        }
    }

    fun deleteItems() {
        viewModelScope.launch {
            repository.deleteItems()
        }
    }

    fun activateCheckedStatus() {
        viewModelScope.launch {
            repository.activateCheckedStatus()
        }
    }

    fun disableCheckedStatus() {
        viewModelScope.launch {
            repository.updateCheckedStatus()
        }
    }

    fun insert(note: Note2) {
        viewModelScope.launch {
            Log.d("NoteListFragment.TAG", "insert")
            repository.insert(note)
        }
    }

    fun update(note: Note2) {
        viewModelScope.launch {
            Log.d("NoteListFragment.TAG", "update")
            repository.update(note)
        }
    }

    fun delete(note: Note2) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }
}