package io.palette.utility.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasServiceInjector
import io.palette.ui.home.HomeActivity
import io.palette.utility.extentions.belowApi
import io.palette.utility.extentions.parseInt
import io.palette.utility.notification.Channels.Channel
import io.palette.utility.preference.PreferenceUtility
import timber.log.Timber
import io.palette.R
import java.util.*
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService(), HasServiceInjector {

    @Inject lateinit var preferences: PreferenceUtility
    @Inject lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Service>

    override fun serviceInjector(): AndroidInjector<Service> = fragmentDispatchingAndroidInjector

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        try {
            if (token != null) preferences.prefFCMToken = token
            Log.d("Token", token)
        } catch (e: Exception) {
            Timber.e(e, "Problem in registering user")
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        when {
            remoteMessage.data == null -> return
            else -> showNotification(remoteMessage)
        }
    }

    private fun showNotification(data: RemoteMessage) {
        try {

            val content = NotificationContent.from(data)
            val contentIntent = content.intent(this)

            if (content.body.isEmpty() || contentIntent == null) return

            contentIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            val contentPendingIntent = PendingIntent.getActivity(this,
                    content.id,
                    contentIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT)

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(this, content.action.channel.id)
            builder.setContentTitle(if (content.title.isEmpty()) getString(R.string.app_name) else content.title)
                    .setContentText(content.body)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .setSmallIcon(R.drawable.ic_style_black_24dp)
                    .setContentIntent(contentPendingIntent)

            if (belowApi(Build.VERSION_CODES.O)) builder.setDefaults(Notification.DEFAULT_SOUND)

            if (content.imageUrl.isNotEmpty()) {
                builder.setStyle(NotificationCompat.BigPictureStyle()
                        .bigPicture(Glide.with(this).asBitmap()
                                .load(content.imageUrl)
                                .submit(100, 100)
                                .get())
                        .setBigContentTitle(content.title)
                        .setSummaryText(content.body)
                )
            } else {
                builder.setStyle(NotificationCompat.BigTextStyle().bigText(content.body))
            }
            manager.notify(content.id, builder.build())
        } catch (e: Exception) {
            Timber.e(e, "Not Application Issue")
        }
    }

    enum class Action(val channel: Channel) {
        GENERAL(Channel.GENERAL),
        ARTICLE(Channel.UNSPLASH);

        companion object {
            fun from(value: String) = try {
                Action.valueOf(value)
            } catch (e: Exception) {
                GENERAL
            }
        }
    }

    class NotificationContent(
            val id: Int,
            val action: Action,
            val title: String,
            val body: String,
            val imageUrl: String,
            val deepLinkUrl: String) {

        companion object {
            fun from(remoteMessage: RemoteMessage) = NotificationContent(
                    id = remoteMessage.data["id"]?.parseInt() ?: Random().nextInt(),
                    action = Action.from(remoteMessage.data["action"] as String),
                    title = remoteMessage.data["title"] ?: remoteMessage.notification?.title ?: "",
                    body = remoteMessage.data["body"] ?: remoteMessage.notification?.body ?: "",
                    imageUrl = remoteMessage.data["image_url"] ?: "",
                    deepLinkUrl = remoteMessage.data["deep_link_url"] as String
            )
        }

        fun intent(context: Context): Intent? = when {
            deepLinkUrl.isEmpty() -> Intent(context, HomeActivity::class.java)
            else -> Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkUrl))
        }
    }
}