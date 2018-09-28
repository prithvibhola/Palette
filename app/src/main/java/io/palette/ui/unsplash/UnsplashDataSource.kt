package io.palette.ui.unsplash

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.ItemKeyedDataSource
import io.palette.data.models.Response
import io.palette.data.models.Unsplash
import io.palette.repository.Repository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class UnsplashDataSource(
        private val repository: Repository,
        private val compositeDisposable: CompositeDisposable
) : ItemKeyedDataSource<Long, Unsplash>() {

    val networkState = MutableLiveData<Response<Unsplash>>()
    val initialLoad = MutableLiveData<Response<List<Unsplash>>>()
    var page = 1

    private var retryCompletable: Completable? = null

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Unsplash>) {
        networkState.postValue(Response.loading())
        initialLoad.postValue(Response.loading())
        compositeDisposable.add(
                repository.unsplashRepository.getUnsplash(page++)
                        .subscribeBy(
                                onNext = {
                                    networkState.postValue(Response.success(null))
                                    initialLoad.postValue(Response.success(it))
                                    callback.onResult(it)
                                },
                                onError = {
                                    page--
                                    setRetry(Action { loadInitial(params, callback) })
                                    networkState.postValue(Response.error(it))
                                    initialLoad.postValue(Response.error(it))
                                })
        )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Unsplash>) {
        networkState.postValue(Response.loading())
        compositeDisposable.add(
                repository.unsplashRepository.getUnsplash(page++)
                        .subscribeBy(
                                onNext = {
                                    networkState.postValue(Response.success(null))
                                    callback.onResult(it)
                                },
                                onError = {
                                    page--
                                    setRetry(Action { loadAfter(params, callback) })
                                    networkState.postValue(Response.error(it))
                                })
        )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Unsplash>) {}

    override fun getKey(item: Unsplash) = item.width

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ }, { throwable -> Timber.e(throwable.message) }))
        }
    }

    private fun setRetry(action: Action?) {
        this.retryCompletable = if (action == null) null else Completable.fromAction(action)
    }
}