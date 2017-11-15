package org.jetbrains.kxhtml.isomorphic

import java.text.SimpleDateFormat
import java.util.*

actual class Date {
    private val calendar: Calendar

    actual constructor() {
        calendar = Calendar.getInstance()
    }

    actual constructor(value: Number) {
        calendar = Calendar.getInstance().apply {
            timeInMillis = value.toLong()
        }
    }

    constructor(date: java.util.Date) {
        calendar = Calendar.getInstance().apply {
            time = date
        }
    }

    val date: java.util.Date get() = calendar.time

    actual fun getDate() = calendar[Calendar.DAY_OF_MONTH]
    actual fun getMonth() = calendar[Calendar.MONTH]
    actual fun getFullYear() = calendar[Calendar.YEAR]
    actual fun getHours() = calendar[Calendar.HOUR_OF_DAY]
    actual fun getMinutes() = calendar[Calendar.MINUTE]
    actual fun getSeconds() = calendar[Calendar.SECOND]
    actual fun getTime(): Number = calendar.timeInMillis

    override fun equals(other: Any?): Boolean = other is Date && other.calendar.time == calendar.time
}

val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
val readableDateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
val readableTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

actual fun parseDate(dateString: String): Date = Date(apiDateFormat.parse(dateString))

actual fun Date.toReadableDateString() = readableDateFormat.format(date)
actual fun Date.toReadableTimeString() = readableTimeFormat.format(date)
