package io.palette.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.palette.view.ui.home.HomeActivity
import io.palette.di.ActivityScoped
import io.palette.view.ui.detail.DetailActivity
import io.palette.view.ui.home.HomeModule

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(HomeModule::class)])
    abstract fun homeActivity(): HomeActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [])
    abstract fun detailActivity(): DetailActivity
}