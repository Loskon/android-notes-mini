package com.loskon.noteminimalism3.app.base.datetime

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date

fun Date.toLocalDate(): LocalDate {
    return toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

fun Date.toLocalDateTime(): LocalDateTime {
    return toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}

fun LocalDate.toDate(): Date {
    return Date.from(atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun LocalDateTime.toDate(): Date {
    return Date(atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
}

fun LocalDateTime.formatString(): String {
    return format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT))
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}