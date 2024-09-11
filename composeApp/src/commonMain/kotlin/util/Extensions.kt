package util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs

fun Long.epochMillisToLocalTime(): LocalDateTime {
    val time: Instant = Instant.fromEpochMilliseconds(this)

    return time.toLocalDateTime(TimeZone.currentSystemDefault())
}

fun Instant.toLocalDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
    return this.toLocalDateTime(timeZone)
}

fun LocalDateTime.formatForGameList_HH_MM(): String {
    val hours = this.hour
    val minutes = this.minute

    // Format as 'hh:mm'
    val formattedHours = if (hours < 10) "0$hours" else "$hours"
    val formattedMins = if(minutes < 10) "0$minutes" else "$minutes"

    return "$formattedHours:$formattedMins"
}

fun LocalDateTime.minusToWholeSeconds(other: LocalDateTime, timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
    val instant1 = this.toInstant(timeZone)
    val instant2 = other.toInstant(timeZone)
    return instant1.minus(instant2).inWholeSeconds
}

fun formatForGameList_MM_SS(seconds: Long): String {
    val minutes = abs(seconds/60)
    val secs = abs(seconds % 60)
    val sign = if(seconds < 0) "-" else ""

    val formattedMins = if(minutes < 10) "0$minutes" else "$minutes"
    val formattedSecs = if(secs < 10) "0$secs" else "$secs"

    return "$sign$formattedMins:$formattedSecs"
}

fun formatForGameList_MM_SS(seconds: Int): String {
    return formatForGameList_MM_SS(seconds.toLong())
}
