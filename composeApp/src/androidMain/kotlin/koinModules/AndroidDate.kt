package koinModules

import koinModules.`interface`.BaseDate
import kotlinx.datetime.LocalDate

class AndroidDate(year: Int, month: Int, day: Int): BaseDate {
    val date: LocalDate = LocalDate(year, month, day)

    override fun format(pattern: String): String {
        // Since SimpleDateFormat works with Date objects
        // and DateTimeFormatter is API26+ and I dont want to use desugarring
        // lets just roll one out ourselves... flaky but should work fine.

        val year = date.year.toString()
        // Pad to be of length 2, to transform single-char numbers (eg 9) into two-char ones (eg 09)
        val month = date.monthNumber.toString().padStart(2, '0')
        // Same thing for days too
        val day = date.dayOfMonth.toString().padStart(2, '0')

        return pattern
            .replace("yyyy", year) // Handle 4-digit year first
            .replace("yy", year.takeLast(2)) // Handle 2-digit year
            .replace("MM", month) // Handle 2-digit month
            .replace("dd", day) // Handle 2-digit day
    }

}
