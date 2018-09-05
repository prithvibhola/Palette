package io.palette

import dagger.Module

@Module
class FlavourDI {

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(cache: Cache) = OkHttpClient.Builder().cache(cache)
}