package io.palette.utility.notification

import android.app.Service
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasServiceInjector
import io.palette.utility.preference.PreferenceUtility
import timber.log.Timber
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService(), HasServiceInjector {

    @Inject lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Service>
    @Inject lateinit var preferences: PreferenceUtility

    override fun serviceInjector(): AndroidInjector<Service> = fragmentDispatchingAndroidInjector

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    /**
     * Called if token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        try {
            if (token != null) preferences.prefFCMToken = token
        } catch (e: Exception) {
            Timber.e(e, "Problem in registering user")
        }
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
    }
}