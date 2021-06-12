package com.loskon.noteminimalism3.model

import java.util.*

data class Note2(
    var id: Long = 0L,
    var title: String,
    var dateCreation: Date,
    var dateModification: Date,
    var dateDelete: Date,
    var isFavorite: Boolean,
    var isChecked: Boolean
)