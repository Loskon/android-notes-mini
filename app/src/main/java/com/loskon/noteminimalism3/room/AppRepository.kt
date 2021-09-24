package com.loskon.noteminimalism3.room

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.Room
import androidx.room.RoomDatabase
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.DATABASE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking


/**
 *  Это одноэлементный класс (синглтон).
 */

class AppRepository(context: Context) {

    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DATABASE_NAME
    ).setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
        .addMigrations(MIGRATION_1_2)
        .build()

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

    fun getNotesSearchById(query: String): Flow<List<Note2>> =
        noteDao.getNotesSearchById(query)

    fun getNotesSearchByFavorite(query: String): Flow<List<Note2>> =
        noteDao.getNotesSearchByFavorite(query)

    fun getNotesSearchByTrash(query: String): Flow<List<Note2>> =
        noteDao.getNotesSearchByTrash(query)


    @WorkerThread
    suspend fun deleteItemsAlways() {
        noteDao.deleteItemsAlways()
    }

    @WorkerThread
    suspend fun deleteItems() {
        noteDao.deleteItems()
    }

    @WorkerThread
    suspend fun updateCheckedStatus() {
        noteDao.updateCheckedStatus()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @WorkerThread
    suspend fun insertWithId(note: Note2): Long = runBlocking(Dispatchers.IO) {
        val count = async {
            noteDao.insertWithId(note)
        }
        count.start()
        count.await()
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

        @JvmStatic
        fun destroyInstance() {
            if (INSTANCE != null) INSTANCE?.close()
            INSTANCE = null
        }
    }
}