package io.palette

import android.content.Context
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.google.firebase.FirebaseApp
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.fabric.sdk.android.Fabric
import io.palette.di.component.DaggerAppComponent
import io.palette.utility.CrashlyticsTree
import io.palette.utility.notification.Channels
import timber.log.Timber
import javax.inject.Inject

class PaletteApplication : DaggerApplication() {

    @Inject lateinit var channels: Channels

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics(), Answers())
        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) + ":" + element.lineNumber
                }
            })
        } else {
            Timber.plant(CrashlyticsTree())
        }

        channels.register()
    }
}
