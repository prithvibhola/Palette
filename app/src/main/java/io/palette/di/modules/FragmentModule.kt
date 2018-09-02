package io.palette.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.palette.di.FragmentScoped
import io.palette.view.ui.pick.PickFragment
import io.palette.view.ui.profile.ProfileFragment
import io.palette.view.ui.profile.ProfileModule
import io.palette.view.ui.unsplash.UnsplashFragment

@Module
abstract class FragmentModule {
//
//    @FragmentScoped
//    @ContributesAndroidInjector(modules = [])
//    internal abstract fun pickFragment(): PickFragment
//
//    @FragmentScoped
//    @ContributesAndroidInjector(modules = [])
//    internal abstract fun unsplashFragment(): UnsplashFragment
//
//    @FragmentScoped
//    @ContributesAndroidInjector(modules = [])
//    internal abstract fun profileFragment(): ProfileFragment
}