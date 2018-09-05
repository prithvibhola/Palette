package io.palette.di.modules

import android.app.Application
import android.content.Context
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.palette.R
import io.palette.data.api.Api
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetModule {

    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache = Cache(application.cacheDir, (10 * 1024 * 1024).toLong())

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideRetrofit(
            context: Context,
            moshi: Moshi,
            okHttpClientBuilder: OkHttpClient.Builder
    ): Retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(context.resources.getString(R.string.base_url_unsplash))
            .client(okHttpClientBuilder.build())
            .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit) = retrofit.create(Api::class.java)
}