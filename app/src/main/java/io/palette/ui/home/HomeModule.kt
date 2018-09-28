package io.palette.ui.home

import android.support.v4.app.FragmentManager
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.palette.di.ActivityScoped
import io.palette.di.FragmentScoped
import io.palette.ui.pick.PickFragment
import io.palette.ui.profile.ProfileFragment
import io.palette.ui.unsplash.UnsplashFragment

@Module
abstract class HomeModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [])
    internal abstract fun pickFragment(): PickFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [])
    internal abstract fun unsplashFragment(): UnsplashFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [])
    internal abstract fun profileFragment(): ProfileFragment

    @Module
    companion object {
        @ActivityScoped
        @Provides
        @JvmStatic
        fun fragmentManager(homeActivity: HomeActivity): FragmentManager = homeActivity.supportFragmentManager
    }
}