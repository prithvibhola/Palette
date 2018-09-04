package io.palette.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.palette.di.ViewModelKey
import io.palette.ui.detail.DetailViewModel
import io.palette.viewmodel.*

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PickViewModel::class)
    internal abstract fun pickViewModel(pickViewModel: PickViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UnsplashViewModel::class)
    internal abstract fun unsplashViewModel(unsplashViewModel: UnsplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    internal abstract fun detailViewModel(detailViewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(profileViewModel: ProfileViewModel): ViewModel
}