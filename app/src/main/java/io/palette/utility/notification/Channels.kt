package io.palette.utility.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import io.palette.BuildConfig
import io.palette.R
import io.palette.utility.extentions.minApi

class Channels(val context: Context) {

    enum class Channel {
        GENERAL,
        UNSPLASH;

        val id = "${BuildConfig.APPLICATION_ID}.$name"
    }

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun register() {
        if (minApi(Build.VERSION_CODES.O)) {
            notificationManager.createNotificationChannels(listOf(
                    createGeneralChannel(),
                    createUnsplashChannel()
            ))
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun createGeneralChannel(): NotificationChannel {
        val id = Channel.GENERAL.id
        val name = context.getString(R.string.channel_general_title)
        val description = context.getString(R.string.channel_general_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)
        channel.description = description
        channel.setShowBadge(true)
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        return channel
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createUnsplashChannel(): NotificationChannel {
        val id = Channel.UNSPLASH.id
        val name = context.getString(R.string.channel_unsplash_title)
        val description = context.getString(R.string.channel_unsplash_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)
        channel.description = description
        channel.setShowBadge(true)
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        return channel
    }
}