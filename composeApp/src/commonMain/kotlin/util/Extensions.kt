package util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.epochMillisToLocalTime(): LocalDateTime {
    val time: Instant = Instant.fromEpochMilliseconds(this)

    return time.toLocalDateTime(TimeZone.currentSystemDefault())
}

fun LocalDateTime.formatForGameList(): String {
    val hours = this.hour
    val minutes = this.minute

    // Format as 'hh:mm'
    val formattedHours = if (hours < 10) "0$hours" else "$hours"
    val formattedMins = if(minutes < 10) "0$minutes" else "$minutes"

    return "$formattedHours:$formattedMins"
}
