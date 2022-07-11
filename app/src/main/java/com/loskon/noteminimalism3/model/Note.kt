package com.loskon.noteminimalism3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Модель Note
 */

@Parcelize
data class Note(

    var id: Long = 0L,

    var title: String = "",

    var dateCreation: Date = Date(),

    var dateModification: Date = Date(),

    var dateDelete: Date = Date(),

    var isFavorite: Boolean = false,

    var isDeleted: Boolean = false,

    var isChecked: Boolean = false

) : Parcelable