package date

interface BaseDate {
    fun format(pattern: String): String
}

expect fun createDate(year: Int, month: Int, day: Int): BaseDate
