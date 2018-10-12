package io.palette.di.modules

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.palette.BuildConfig
import io.palette.utility.notification.Channels
import io.palette.utility.rx.AppScheduler
import io.palette.utility.rx.Scheduler
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    abstract fun provideContext(application: Application): Context

    @Module
    companion object {

        @Provides
        @Singleton
        @JvmStatic
        fun provideScheduler(): Scheduler = AppScheduler()

        @Provides
        @Singleton
        @JvmStatic
        fun provideSharedPreference(context: Context) = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

        @Provides
        @Singleton
        @JvmStatic
        fun provideChannels(context: Context) = Channels(context)
    }
}