package io.palette.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import io.palette.PaletteApplication
import io.palette.di.modules.*
import io.palette.viewmodel.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (AppModule::class),
    (NetModule::class),
    (ActivityModule::class),
    (ViewModelModule::class),
    (AuthModule::class),
    (AndroidSupportInjectionModule::class),
    (AndroidInjectionModule::class)
])
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(application: PaletteApplication)
    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}