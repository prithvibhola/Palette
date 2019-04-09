package io.palette.ui.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface HasDisposableManager {
    fun getCompositeDisposable(): CompositeDisposable
    fun addDisposable(disposable: Disposable)
    fun dispose()
}