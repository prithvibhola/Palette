package io.palette.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.palette.ui.home.HomeActivity
import io.palette.di.ActivityScoped
import io.palette.ui.about.AboutActivity
import io.palette.ui.detail.DetailActivity
import io.palette.ui.home.HomeModule
import io.palette.ui.settings.SettingsActivity

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(HomeModule::class)])
    abstract fun homeActivity(): HomeActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [])
    abstract fun detailActivity(): DetailActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [])
    abstract fun settingsActivity(): SettingsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [])
    abstract fun aboutActivityv(): AboutActivity
}