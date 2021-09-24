package com.loskon.noteminimalism3.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_CHECKED
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_DATE
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_DATE_DEL
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_DATE_MOD
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_DEL_ITEMS
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_FAVORITES
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_ID
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_TITLE
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.NAME_TABLE
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 *
 */

@Parcelize
@Entity(tableName = NAME_TABLE)
data class Note2(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var id: Long = 0L,

    @ColumnInfo(name = COLUMN_TITLE)
    var title: String = "",

    @ColumnInfo(name = COLUMN_DATE)
    var dateCreation: Date = Date(),

    @ColumnInfo(name = COLUMN_DATE_MOD)
    var dateModification: Date = Date(),

    @ColumnInfo(name = COLUMN_DATE_DEL)
    var dateDelete: Date = Date(),

    @ColumnInfo(name = COLUMN_FAVORITES)
    var isFavorite: Boolean = false,

    @ColumnInfo(name = COLUMN_DEL_ITEMS)
    var isDelete: Boolean = false,

    @ColumnInfo(name = COLUMN_CHECKED)
    var isChecked: Boolean = false
) : Parcelable