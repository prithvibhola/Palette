package io.palette.utility.extentions

import android.os.Handler

fun withDelay(delay: Long = 100L, block: () -> Unit) = Handler().postDelayed(Runnable(block), delay)