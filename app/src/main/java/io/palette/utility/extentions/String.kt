package io.palette.utility.extentions

import java.text.SimpleDateFormat
import java.util.*

val String.titleCase get() = this.split("\\s+").joinToString(" ") { it.toLowerCase().capitalize() }

fun String.dateConvert(): String {

    val defaultDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val newDateFormat = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())

    return newDateFormat.format(defaultDateFormat.parse(this@dateConvert.split("T")[0])).replace("-", " ")
}