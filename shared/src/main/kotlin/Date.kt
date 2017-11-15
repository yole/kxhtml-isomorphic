package org.jetbrains.kxhtml.isomorphic

expect class Date() {
    constructor(value: Number)
    fun getDate(): Int
    fun getMonth(): Int
    fun getFullYear(): Int
    fun getHours(): Int
    fun getMinutes(): Int
    fun getSeconds(): Int
    fun getTime(): Number
}

expect fun parseDate(dateString: String): Date
expect fun Date.toReadableDateString(): String
expect fun Date.toReadableTimeString(): String

fun Date.toReadableDateTimeString() = "${toReadableDateString()} ${toReadableTimeString()}"
