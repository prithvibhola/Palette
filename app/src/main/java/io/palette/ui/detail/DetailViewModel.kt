package io.palette.ui.detail

import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.widget.RecyclerView
import io.palette.data.models.GeneratedPalette
import io.palette.data.models.Response
import io.palette.repository.Repository
import io.palette.ui.base.BaseViewModel
import io.palette.utility.extentions.fromWorkerToMain
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import prithvi.io.mvvmstarter.utility.rx.Scheduler
import javax.inject.Inject

class DetailViewModel @Inject constructor(
        private val repository: Repository,
        private val scheduler: Scheduler
) : BaseViewModel() {

    var palette: MutableLiveData<Response<List<GeneratedPalette>>> = MutableLiveData()
    var shareUri: MutableLiveData<Response<Uri>> = MutableLiveData()
    var savePalette: MutableLiveData<Response<Uri>> = MutableLiveData()

    fun generatePalette(bitmap: Bitmap) {
        repository.detailRepository.getPalette(bitmap)
                .fromWorkerToMain(scheduler)
                .subscribeBy(
                        onNext = { palette.value = Response.success(it) },
                        onError = { palette.value = Response.error(it) }
                )
                .addTo(getCompositeDisposable())
    }

    fun savePalette(view: RecyclerView, sharePalette: Boolean, bitmap: Bitmap) {
        repository.detailRepository.saveBitmap(repository.detailRepository.generateBitmap(view, bitmap)!!)
                .fromWorkerToMain(scheduler)
                .subscribeBy(
                        onNext = { (if (sharePalette) shareUri else savePalette).value = Response.success(it) },
                        onError = { (if (sharePalette) shareUri else savePalette).value = Response.error(it) }
                )
                .addTo(getCompositeDisposable())
    }
}