package io.palette.di.modules

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import prithvi.io.mvvmstarter.utility.rx.AppScheduler
import prithvi.io.mvvmstarter.utility.rx.Scheduler
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
    }
}