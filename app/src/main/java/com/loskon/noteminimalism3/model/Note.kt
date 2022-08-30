package com.loskon.noteminimalism3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Note(
    var id: Long = 0L,
    var title: String = "",
    var createdDate: LocalDateTime = LocalDateTime.now(),
    var modifiedDate: LocalDateTime = LocalDateTime.now(),
    var deletedDate: LocalDateTime = LocalDateTime.now(),
    var isFavorite: Boolean = false,
    var isDeleted: Boolean = false,
    var isChecked: Boolean = false
) : Parcelable