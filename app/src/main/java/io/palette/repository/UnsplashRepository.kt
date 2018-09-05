package io.palette.repository

import io.palette.data.api.Api
import io.palette.data.models.Unsplash
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(
        private val api: Api
) {

    companion object {
        const val PAGE_SIZE = 20
    }

    fun getUnsplash(page: Int): Flowable<List<Unsplash>> = api.getUnsplash(PAGE_SIZE, page).toFlowable()
}