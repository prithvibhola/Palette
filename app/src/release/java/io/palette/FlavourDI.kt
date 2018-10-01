package io.palette

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import okhttp3.Cache
import okhttp3.OkHttpClient

@Module
class FlavourDI {

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(cache: Cache) = OkHttpClient.Builder().cache(cache)
}