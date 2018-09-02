package io.palette.view.ui.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.widget.RecyclerView
import io.palette.data.models.GeneratedPalette
import io.palette.data.models.Response
import io.palette.repository.Repository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repository: Repository
    var palette: MutableLiveData<Response<List<GeneratedPalette>>> = MutableLiveData()
    var shareUri: MutableLiveData<Response<Uri>> = MutableLiveData()
    var savePalette: MutableLiveData<Response<Uri>> = MutableLiveData()

    fun generatePalette(bitmap: Bitmap) {
        repository.detailRepository
                .getPalette(bitmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            palette.value = (Response(status = Response.ViewState.SUCCESS, data = it, error = null))
                        },
                        onError = {
                            palette.value = (Response(status = Response.ViewState.ERROR, data = null, error = it))
                        }
                )
    }

    fun savePalette(view: RecyclerView, sharePalette: Boolean, bitmap: Bitmap) {
        repository.detailRepository
                .saveBitmap(repository.detailRepository.generateBitmap(view, bitmap)!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (sharePalette)
                                shareUri.value = (Response(status = Response.ViewState.SUCCESS, data = it, error = null))
                            else
                                savePalette.value = (Response(status = Response.ViewState.SUCCESS, data = it, error = null))
                        },
                        onError = {
                            if (sharePalette)
                                shareUri.value = (Response(status = Response.ViewState.ERROR, data = null, error = it))
                            else
                                savePalette.value = (Response(status = Response.ViewState.SUCCESS, data = null, error = null))
                        }
                )
    }
}