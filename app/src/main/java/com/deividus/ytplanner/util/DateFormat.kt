package com.deividus.ytplanner.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val zoneId: ZoneId = ZoneId.systemDefault()
private val shortDateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("es", "ES"))
private val dayMonthFormatter = DateTimeFormatter.ofPattern("d MMM", Locale("es", "ES"))

fun Long.toLocalDate(): java.time.LocalDate =
    Instant.ofEpochMilli(this).atZone(zoneId).toLocalDate()

fun java.time.LocalDate.toEpochMillis(): Long =
    atStartOfDay(zoneId).toInstant().toEpochMilli()

fun Long.formatShortDate(): String = toLocalDate().format(shortDateFormatter)

fun Long.formatDayMonth(): String = toLocalDate().format(dayMonthFormatter)

fun java.time.Month.spanishLabel(): String =
    getDisplayName(TextStyle.FULL, Locale("es", "ES")).replaceFirstChar { it.uppercase() }
