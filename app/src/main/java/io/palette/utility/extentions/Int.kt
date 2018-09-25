package io.palette.utility.extentions

import android.graphics.Color
import java.util.*

fun Int.toHex(): String = Integer.toHexString(this)

fun List<Int>.getRandom() = this[Random().nextInt(this.size)]

fun Int.rgbString(): String = "(${Color.red(this@rgbString)}, ${Color.green(this@rgbString)}, ${Color.blue(this@rgbString)})"