package com.loskon.noteminimalism3.room

import androidx.room.*
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

    @Query("SELECT * FROM notes WHERE del_items = 0 AND (title LIKE :query OR LOWER(title) like LOWER(:query)) ORDER BY _id DESC")
    fun getNotesSearchById(query: String): Flow<List<Note2>>

    @Query("SELECT * FROM notes WHERE favorites = 1 AND (title LIKE :query OR LOWER(title) like LOWER(:query)) ORDER BY _id DESC")
    fun getNotesSearchByFavorite(query: String): Flow<List<Note2>>

    @Query("SELECT * FROM notes WHERE del_items = 1 AND (title LIKE :query OR LOWER(title) like LOWER(:query)) ORDER BY date_del DESC")
    fun getNotesSearchByTrash(query: String): Flow<List<Note2>>

    @Query("SELECT * FROM notes ORDER BY _id DESC")
    fun getNotesByDate(): Flow<List<Note2>>

    @Query("UPDATE notes SET del_items = 1 WHERE is_checked = 1")
    suspend fun deleteItems()

    @Query("DELETE FROM notes WHERE del_items = 1")
    suspend fun deleteItemsAlways()

    @Query("UPDATE notes SET is_checked = 0")
    suspend fun updateCheckedStatus()

    @Insert
    suspend fun insert(note: Note2)

    @Update
    suspend fun update(note: Note2)

    @Delete
    suspend fun delete(note: Note2)
}