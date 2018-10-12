package io.palette.utility.extentions

import android.os.Build
import android.os.Handler

fun withDelay(delay: Long = 100L, block: () -> Unit) = Handler().postDelayed(Runnable(block), delay)

fun minApi(sdkInt: Int) = Build.VERSION.SDK_INT >= sdkInt
fun aboveApi(sdkInt: Int) = Build.VERSION.SDK_INT > sdkInt
fun maxApi(sdkInt: Int) = Build.VERSION.SDK_INT <= sdkInt
fun belowApi(sdkInt: Int) = Build.VERSION.SDK_INT < sdkInt