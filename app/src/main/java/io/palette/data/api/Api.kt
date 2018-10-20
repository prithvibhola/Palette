package io.palette.data.api

import io.palette.data.models.Unsplash
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("/allureunsplash_photos")
    fun getUnsplash(
            @Query("per_page") size: Int,
            @Query("page") page: Int
    ): Single<List<Unsplash>>

    @GET("/unsplash_image/{unsplash_id}")
    fun getUnsplashPhoto(
            @Path("unsplash_id") unsplashId: String,
            @Query("client_id") clientId: String
    ): Single<Unsplash>
}