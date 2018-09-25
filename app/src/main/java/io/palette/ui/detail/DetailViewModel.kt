package io.palette.ui.detail

import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.support.v7.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
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
import java.io.File
import javax.inject.Inject

class DetailViewModel @Inject constructor(
        private val repository: Repository,
        private val scheduler: Scheduler,
        private val firebaseAuth: FirebaseAuth,
        private val fireStore: FirebaseFirestore
) : BaseViewModel() {

    private val PALETTE_WALL_FOLDER = File(Environment.getExternalStorageDirectory(), "Palette_Wall")
    private val PALETTE_FOLDER = File(Environment.getExternalStorageDirectory(), "Palette")

    var palette: MutableLiveData<Response<List<GeneratedPalette>>> = MutableLiveData()
    var shareUri: MutableLiveData<Response<Uri>> = MutableLiveData()
    var savePalette: MutableLiveData<Response<Uri>> = MutableLiveData()
    var likeUnlikePalette: MutableLiveData<Response<Boolean>> = MutableLiveData()
    var saveWallpaper: MutableLiveData<Response<Uri>> = MutableLiveData()

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
        repository.detailRepository.saveBitmap(repository.detailRepository.generateBitmap(view, bitmap)!!, PALETTE_FOLDER)
                .fromWorkerToMain(scheduler)
                .subscribeBy(
                        onNext = { (if (sharePalette) shareUri else savePalette).value = Response.success(it) },
                        onError = {
                            Timber.e(it, "Error in saving image")
                            (if (sharePalette) shareUri else savePalette).value = Response.error(it)
                        }
                )
                .addTo(getCompositeDisposable())
    }

    fun likeUnlikePalette(unsplash: Unsplash, isLiked: Boolean) {
        if (firebaseAuth.currentUser == null) {
            likeUnlikePalette.value = Response.error(Exception("User is not registered"))
            return
        }

        likeUnlikePalette.value = Response.loading()

        (if (isLiked) repository.detailRepository.unlikePalette(unsplash) else repository.detailRepository.likePalette(unsplash))
                .fromWorkerToMain(scheduler)
                .subscribeBy(
                        onNext = {
                            likeUnlikePalette.value = Response.success(it)
                        },
                        onError = {
                            likeUnlikePalette.value = Response.error(it)
                            Timber.e(it, "Error liking Palette")
                        }
                )
                .addTo(getCompositeDisposable())
    }

    fun saveWallpaper(bitmap: Bitmap) {
        saveWallpaper.value = Response.loading()
        repository.detailRepository.saveBitmap(bitmap, PALETTE_WALL_FOLDER)
                .fromWorkerToMain(scheduler)
                .subscribeBy(
                        onNext = {
                            saveWallpaper.value = Response.success(it)
                        },
                        onError = {
                            saveWallpaper.value = Response.error(it)
                            Timber.e(it, "Error in saving wallpaper")
                        }
                )
    }
}