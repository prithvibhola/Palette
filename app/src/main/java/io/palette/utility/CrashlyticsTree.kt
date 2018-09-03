package io.palette.utility

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CrashlyticsTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority !in arrayOf(Log.ERROR, Log.ASSERT, Log.WARN)
                || t is SocketTimeoutException
                || t is SocketException
                || t is UnknownHostException) return
        Crashlytics.getInstance().core.log(priority, tag, message)
        if (t != null) Crashlytics.getInstance().core.logException(t)
    }
}