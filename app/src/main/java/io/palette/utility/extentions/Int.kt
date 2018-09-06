package io.palette.utility.extentions

import java.util.*

fun Int.toHex() = Integer.toHexString(this)

fun List<Int>.getRandom() = this[Random().nextInt(this.size - 1)]