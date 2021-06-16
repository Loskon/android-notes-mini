package com.loskon.noteminimalism3.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.loskon.noteminimalism3.model.Note2
import kotlinx.coroutines.flow.Flow


/**
 *
 */

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes WHERE del_items = 0 ORDER BY _id DESC")
    fun getNotesById(): Flow<List<Note2>>

    @Query("SELECT * FROM notes WHERE favorites = 1 ORDER BY _id DESC")
    fun getNotesByFavorite(): Flow<List<Note2>>

    @Query("SELECT * FROM notes WHERE del_items = 1 ORDER BY date_del DESC")
    fun getNotesByTrash(): Flow<List<Note2>>

    @Query("SELECT * FROM notes WHERE del_items = 0 LIKE :query OR LOWER(title) LIKE LOWER(:query) ORDER BY _id DESC")
    fun getListAllByName(query: String): Flow<List<Note2>>

    @Query("SELECT * FROM notes ORDER BY _id DESC")
    fun getNotesByDate(): Flow<List<Note2>>

    @Insert
    suspend fun insert(note: Note2)

    @Update
    suspend fun update(note: Note2)

    @Delete
    suspend fun delete(note: Note2)
}