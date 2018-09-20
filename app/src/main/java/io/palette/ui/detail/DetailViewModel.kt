package io.palette.ui.detail

import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import io.palette.data.models.GeneratedPalette
import io.palette.data.models.Response
import io.palette.data.models.Unsplash
import io.palette.repository.Repository
import io.palette.ui.base.BaseViewModel
import io.palette.utility.extentions.fromWorkerToMain
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import prithvi.io.mvvmstarter.utility.rx.Scheduler
import timber.log.Timber
import javax.inject.Inject

class DetailViewModel @Inject constructor(
        private val repository: Repository,
        private val scheduler: Scheduler,
        private val firebaseAuth: FirebaseAuth
) : BaseViewModel() {

    var palette: MutableLiveData<Response<List<GeneratedPalette>>> = MutableLiveData()
    var shareUri: MutableLiveData<Response<Uri>> = MutableLiveData()
    var savePalette: MutableLiveData<Response<Uri>> = MutableLiveData()
    var likePalette: MutableLiveData<Response<Boolean>> = MutableLiveData()

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

    fun likePalette(unsplash: Unsplash) {
        if (firebaseAuth.currentUser == null) likePalette.value = Response.error(Exception("User is not registered"))

        likePalette.value = Response.loading()
        repository.detailRepository.likePalette(unsplash)
                .fromWorkerToMain(scheduler)
                .subscribeBy(
                        onNext = {
                            likePalette.value = Response.success(it)
                        },
                        onError = {
                            likePalette.value = Response.error(it)
                            Timber.e(it, "Error liking Palette")
                        }
                )
                .addTo(getCompositeDisposable())
    }
}