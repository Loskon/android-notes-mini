package com.loskon.noteminimalism3

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.loskon.noteminimalism3.commands.CommandCenter
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.not
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertNotNull
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Тест базы данных
 */

@RunWith(AndroidJUnit4::class)
class SQLiteTest {
    companion object {

        private lateinit var commandCenter: CommandCenter

        @BeforeClass
        @JvmStatic
        fun initDataBase() {
            val context: Context = ApplicationProvider.getApplicationContext()
            DataBaseAdapter.initDateBase(context)
            commandCenter = CommandCenter()
        }
    }

    @Test
    @Throws(NullPointerException::class)
    fun notNullCommandCenter() {
        assertNotNull(commandCenter)
    }

    @Test
    @Throws(Exception::class)
    fun insertNoteAndReadInList() {
        val note: Note = createAndInsertNote()
        assertThat(notes[0], `is`(note))
    }

    private fun createAndInsertNote(): Note {
        val note = Note()
        note.title = "Test Title"
        note.id = commandCenter.insertWithIdReturn(note)
        return note
    }

    private val notes: List<Note>
        get() {
            return commandCenter.getNotes(null, DataBaseAdapter.CATEGORY_ALL_NOTES, 0)
        }

    @Test
    @Throws(Exception::class)
    fun deleteNoteAndReadInList() {
        val note: Note = createAndInsertNote()
        createAndInsertNote()
        commandCenter.delete(note)
        assertThat(notes[0], `is`(not(note)))
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() {
        commandCenter.deleteAll()
        assertThat(notes.size, `is`(0))
    }
}