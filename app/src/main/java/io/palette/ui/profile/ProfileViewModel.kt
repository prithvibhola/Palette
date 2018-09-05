package io.palette.ui.profile

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import io.palette.data.models.Response
import io.palette.repository.Repository
import io.palette.ui.base.BaseViewModel
import io.palette.utility.extentions.fromWorkerToMain
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import prithvi.io.mvvmstarter.utility.rx.Scheduler
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        val repository: Repository,
        val scheduler: Scheduler
) : BaseViewModel() {

    val user: MutableLiveData<Response<FirebaseUser>> = MutableLiveData()

    fun login(activity: Activity, account: GoogleSignInAccount) {
        user.value = Response(Response.Status.LOADING, null, null)
        repository.profileRepository.firebaseAuthWithGoogle(activity, account)
                .fromWorkerToMain(scheduler)
                .subscribeBy(
                        onNext = {
                            user.value = Response.success(it)
                        },
                        onError = {
                            user.value = Response.error(it)
                        }
                )
                .addTo(getCompositeDisposable())
    }
}