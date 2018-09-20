package io.palette.ui.unsplash

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.google.firebase.auth.FirebaseAuth
import io.palette.data.models.Response
import io.palette.data.models.Unsplash
import io.palette.repository.Repository
import io.palette.ui.base.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class UnsplashViewModel @Inject constructor(
        private val repository: Repository,
        private val firebaseAuth: FirebaseAuth
) : BaseViewModel() {

    var unsplash: LiveData<PagedList<Unsplash>>
    private var sourceFactory: UnsplashSourceFactory
    private var pageSize = 10

    val likedPalettes: MutableLiveData<Response<List<Unsplash>>> = MutableLiveData()

    init {
        sourceFactory = UnsplashSourceFactory()
        unsplash = LivePagedListBuilder<Long, Unsplash>(sourceFactory,
                PagedList.Config.Builder()
                        .setPageSize(pageSize)
                        .setInitialLoadSizeHint(pageSize * 2)
                        .setEnablePlaceholders(false)
                        .build())
                .build()
    }

    fun retry() = sourceFactory.unsplashDataSource.value!!.retry()
    fun refresh() = sourceFactory.unsplashDataSource.value!!.invalidate()
    fun getNetworkState(): LiveData<Response<Unsplash>> = Transformations.switchMap<UnsplashDataSource, Response<Unsplash>>(sourceFactory.unsplashDataSource) { it.networkState }
    fun getRefreshState(): LiveData<Response<List<Unsplash>>> = Transformations.switchMap<UnsplashDataSource, Response<List<Unsplash>>>(sourceFactory.unsplashDataSource) { it.initialLoad }

    fun getLikedPalettes() {
        likedPalettes.value = Response.loading()
        if (firebaseAuth.currentUser == null) {
            likedPalettes.value = Response.success(listOf())
        } else {
            repository.profileRepository.getLikedPalettes()
                    .subscribeBy(
                            onNext = {
                                likedPalettes.value = Response.success(it)
                            },
                            onError = {
                                likedPalettes.value = Response.error(Throwable("Error getting user liked palettes"))
                                Timber.e(it, "Error getting user liked palettes")
                            }
                    )
        }
    }

    open inner class UnsplashSourceFactory : DataSource.Factory<Long, Unsplash>() {

        val unsplashDataSource = MutableLiveData<UnsplashDataSource>()

        override fun create(): DataSource<Long, Unsplash> {
            val dataSource = UnsplashDataSource(repository, getCompositeDisposable())
            unsplashDataSource.postValue(dataSource)
            return dataSource
        }
    }
}