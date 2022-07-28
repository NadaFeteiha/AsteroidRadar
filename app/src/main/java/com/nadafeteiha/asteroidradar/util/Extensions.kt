package com.nadafeteiha.asteroidradar.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun String.convertDateToLong(): Long {
    val date: Date = SimpleDateFormat("yyyy-MM-dd").parse(this) as Date
    return date.time
}

fun Long.convertLongToDate():String{
    val date = Date(this)
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.format(date)
}