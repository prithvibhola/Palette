package io.palette.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.palette.di.FragmentScoped
import io.palette.utility.notification.MyFirebaseMessagingService

@Module
abstract class ServiceModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [])
    abstract fun myFirebaseMessagingService(): MyFirebaseMessagingService

}