package com.loskon.noteminimalism3.room

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.loskon.noteminimalism3.database.NoteDbSchema.NoteTable.DATABASE_NAME
import com.loskon.noteminimalism3.model.Note2
import kotlinx.coroutines.flow.Flow


/**
 *  Это одноэлементный класс (синглтон).
 */

class AppRepository(context: Context) {

    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(MIGRATION_1_2).build()

    val getAppDatabase: AppDatabase
        get() {
            return database
        }

    fun close() {
        database.close()
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

    private val noteDao = database.noteDao()

    fun getNotesById(): Flow<List<Note2>> = noteDao.getNotesById()
    fun getNotesByFavorite(): Flow<List<Note2>> = noteDao.getNotesByFavorite()
    fun getNotesByTrash(): Flow<List<Note2>> = noteDao.getNotesByTrash()

    fun getListAllByName(query: String): Flow<List<Note2>> {
        return noteDao.getListAllByName(query)
    }

    @WorkerThread
    suspend fun insert(note: Note2) {
        noteDao.insert(note)
    }

    @WorkerThread
    suspend fun update(note: Note2) {
        noteDao.update(note)
    }

    @WorkerThread
    suspend fun delete(note: Note2) {
        noteDao.delete(note)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    companion object {
        private var INSTANCE: AppRepository? = null

        // Инициализация нового экземпляра в репозиторий
        @JvmStatic
        fun initRepository(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = AppRepository(context)
            }
        }

        // Обеспечение доступа к репозиторию
        fun getRepository(): AppRepository {
            return INSTANCE ?: throw IllegalStateException("Repository must be initialized")
        }

        @JvmStatic
        fun getAppDatabase(): AppDatabase? {
            return INSTANCE?.database
        }

        // close database
        @JvmStatic
        fun destroyInstance() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}