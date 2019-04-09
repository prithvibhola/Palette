package io.palette.utility.extentions

import android.content.Context
import android.util.TypedValue

fun Number.toDp(context: Context): Int = (this.toFloat() / (context.resources.displayMetrics.densityDpi / 160f)).toInt()

fun Number.toPx(context: Context): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()