package io.palette.view.ui.home

import android.support.v4.app.FragmentManager
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.palette.di.ActivityScoped
import io.palette.di.FragmentScoped
import io.palette.view.ui.pick.PickFragment
import io.palette.view.ui.profile.ProfileFragment
import io.palette.view.ui.profile.ProfileModule
import io.palette.view.ui.unsplash.UnsplashFragment

@Module
abstract class HomeModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [])
    internal abstract fun pickFragment(): PickFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [])
    internal abstract fun unsplashFragment(): UnsplashFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [ProfileModule::class])
    internal abstract fun profileFragment(): ProfileFragment

    @Module
    companion object {
        @ActivityScoped
        @Provides
        @JvmStatic
        fun fragmentManager(homeActivity: HomeActivity): FragmentManager = homeActivity.supportFragmentManager
    }
}