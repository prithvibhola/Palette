package io.palette.data.api

import io.palette.data.models.Unsplash
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/allureunsplash_photos")
    fun getUnsplash(
            @Query("per_page") size: Int,
            @Query("page") page: Int
    ): Single<List<Unsplash>>
}