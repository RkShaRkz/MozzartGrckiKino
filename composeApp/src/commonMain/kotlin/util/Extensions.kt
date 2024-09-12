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

fun LocalDateTime.formatForGameList_DD_MM_HH_MM(): String {
    val day = this.dayOfMonth
    val month = this.monthNumber
    val hours = this.hour
    val minutes = this.minute

    // Format day/month to 2 numbers
    val formattedDays = if (day < 10) "0$day" else "$day"
    val formattedMonth = if (month < 10) "0$month" else "$month"

    // Format as 'hh:mm'
    val formattedHours = if (hours < 10) "0$hours" else "$hours"
    val formattedMins = if(minutes < 10) "0$minutes" else "$minutes"

    return "$formattedDays.$formattedMonth. $formattedHours:$formattedMins"
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

/**
 * Returns a list that has all the items that the [numberSet] has and some added bogus (negative)
 * numbers so it's size is cleanly-divisible with [rowLength] to avoid having disproportionate
 * number of items per row
 */
fun fillInSetSizeToMatchRowWidth(numberSet: Set<Int>, rowLength: Int): List<Int> {
    // To fill in, we need to see how much 'over' we are.
    // We cannot (should not) delete numbers from the set, but we can add more insignificant numbers
    val retVal = mutableListOf<Int>()
    retVal.addAll(numberSet)

    val howManyOver = numberSet.size % rowLength
    // If we're even we don't need to add anything
    if (howManyOver > 0) {
        // With the 'howManyOver' calculated, we need to add the inverse value to fill it up to width
        val howMuchToAdd = rowLength - howManyOver

        // And finally add those things in
        for (i in 0 until howMuchToAdd) {
            // Lets add negative numbers so we can filter them out later if we need to
            retVal.add(-i)
        }
    }

    return retVal.toList()
}
