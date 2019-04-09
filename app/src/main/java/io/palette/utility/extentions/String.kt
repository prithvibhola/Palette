package io.palette.utility.extentions

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

val String.titleCase get() = this.split("\\s+").joinToString(" ") { it.toLowerCase().capitalize() }

@SuppressLint("ConstantLocale")
val defaultDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

@SuppressLint("ConstantLocale")
val newDateFormat = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())

fun String.dateConvert(): String = newDateFormat.format(defaultDateFormat.parse(this@dateConvert.split("T")[0])).replace("-", " ")

fun String.alternative(alternate: String) = if (this.isEmpty()) alternate else this

fun String.defaultDate(): String = if (this.isEmpty()) "${defaultDateFormat.format(Date(System.currentTimeMillis()))}T00:00:00-00:00" else this

fun String.parseInt(): Int? {
    return try {
        Integer.parseInt(this)
    } catch (e: NumberFormatException) {
        null
    }
}