package io.palette.ui.profile

import android.arch.lifecycle.MutableLiveData
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.palette.data.models.Response
import io.palette.data.models.Unsplash
import io.palette.repository.Repository
import io.palette.ui.base.BaseViewModel
import io.palette.utility.extentions.snapshotAsFlowable
import io.reactivex.rxkotlin.subscribeBy
import prithvi.io.mvvmstarter.utility.rx.Scheduler
import timber.log.Timber
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        val repository: Repository,
        val scheduler: Scheduler,
        val firebaseAuth: FirebaseAuth
) : BaseViewModel() {

    val user: MutableLiveData<Response<FirebaseUser>> = MutableLiveData()
    val palettes: MutableLiveData<Response<List<Unsplash>>> = MutableLiveData()

    fun setUser(response: IdpResponse?) {
        if (response == null)
            user.value = Response.success(if (firebaseAuth.currentUser != null) firebaseAuth.currentUser else null)
        else {
            Timber.e(response.error, "User could not sign in")
            user.value = Response.error(response.error!!.fillInStackTrace())
        }
    }

    fun getPalettes() {
        palettes.value = Response.loading()
        if (firebaseAuth.currentUser == null) {
            palettes.value = Response.success(listOf())
        } else {
            repository.profileRepository.getLikedPalettes()
                    .subscribeBy(
                            onNext = {
                                palettes.value = Response.success(it)
                            },
                            onError = {
                                palettes.value = Response.error(Throwable("Error getting user liked palettes"))
                                Timber.e(it, "Error getting user liked palettes")
                            }
                    )
        }
    }
}