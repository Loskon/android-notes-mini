package com.loskon.noteminimalism3.viewmodel

import android.text.TextUtils
import androidx.lifecycle.*
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.room.AppRepository
import kotlinx.coroutines.launch

/**
 *
 */

class NoteViewModel : ViewModel() {

    companion object Category {
        const val CATEGORY_ALL_NOTES = "CATEGORY_ALL_NOTES"
        const val CATEGORY_FAVORITES = "CATEGORY_NOTES"
        const val CATEGORY_TRASH = "CATEGORY_TRASH"
        const val CATEGORY_SEARCH = "CATEGORY_SEARCH"
    }

    private val repository = AppRepository.getRepository()

    private val getNotesById: LiveData<List<Note2>> =
        repository.getNotesById().asLiveData()
    private val getNotesByFavorite: LiveData<List<Note2>> =
        repository.getNotesByFavorite().asLiveData()
    private val getNotesByTrash: LiveData<List<Note2>> =
        repository.getNotesByTrash().asLiveData()

    private fun allProductsByNames(query: String): LiveData<List<Note2>> =
        repository.getListAllByName(query).asLiveData()


    private val getSearchList = MutableLiveData<String>()

    val searchList: LiveData<List<Note2>> =
        Transformations.switchMap(getSearchList) { query ->
            if (TextUtils.isEmpty(query)) {
                getNotes
            } else {
                allProductsByNames(query)
            }
        }

    fun searchNameChanged(query: String) {
        getSearchList.value = query
    }

    val getNotes = MediatorLiveData<List<Note2>>()

    private var currentOrder = CATEGORY_ALL_NOTES

    init {
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

/*        getNotes.addSource(searchList) { result ->
            if (currentOrder == CATEGORY_SEARCH) {
                result?.let { getNotes.value = it }
            }
        }*/
    }

    fun categoryNotes(order: String) = when (order) {
        CATEGORY_FAVORITES -> getNotesByFavorite.value.let { getNotes.value = it }
        CATEGORY_TRASH -> getNotesByTrash.value.let { getNotes.value = it }
        else -> getNotesById.value.let { getNotes.value = it }
    }.also { currentOrder = order }

    fun insert(note: Note2) {
        viewModelScope.launch {
            repository.insert(note)
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