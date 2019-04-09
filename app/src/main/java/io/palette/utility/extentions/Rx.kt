package io.palette.utility.extentions

import io.palette.utility.rx.Scheduler
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun <T> Flowable<T>.fromWorkerToMain(scheduler: Scheduler): Flowable<T> = this.subscribeOn(scheduler.io()).observeOn(scheduler.mainThread())

fun Disposable.addTo(compositeDisposable: CompositeDisposable) = compositeDisposable.add(this)
