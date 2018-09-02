package io.palette.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import io.palette.data.models.Response
import io.palette.data.models.Unsplash
import io.palette.repository.Repository
import io.palette.view.ui.unsplash.UnsplashDataSource
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UnsplashViewModel @Inject constructor(
        val repository: Repository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    var unsplash: LiveData<PagedList<Unsplash>>
    private var sourceFactory: UnsplashSourceFactory
    private var pageSize = 10

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
    fun getNetworkState(): LiveData<Response<Unsplash>> = Transformations.switchMap<UnsplashDataSource, Response<Unsplash>>(sourceFactory.unsplashDataSource, { it.networkState })
    fun getRefreshState(): LiveData<Response<Unsplash>> = Transformations.switchMap<UnsplashDataSource, Response<Unsplash>>(sourceFactory.unsplashDataSource, { it.initialLoad })

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    open inner class UnsplashSourceFactory : DataSource.Factory<Long, Unsplash>() {

        val unsplashDataSource = MutableLiveData<UnsplashDataSource>()

        override fun create(): DataSource<Long, Unsplash> {
            val dataSource = UnsplashDataSource(repository, compositeDisposable)
            unsplashDataSource.postValue(dataSource)
            return dataSource
        }
    }
}