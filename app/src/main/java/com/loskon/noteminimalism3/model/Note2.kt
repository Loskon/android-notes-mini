package com.loskon.noteminimalism3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 *
 */

@Parcelize
data class Note2(
    var id: Long = 0L,

    var title: String = "",

    var dateCreation: Date = Date(),

    var dateModification: Date = Date(),

    var dateDelete: Date = Date(),

    var isFavorite: Boolean = false,

    var isDelete: Boolean = false,

    var isChecked: Boolean = false
) : Parcelable