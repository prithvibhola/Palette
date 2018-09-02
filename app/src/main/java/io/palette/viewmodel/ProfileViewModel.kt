package io.palette.viewmodel

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import io.palette.data.models.Response
import io.palette.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        val repository: Repository
) : ViewModel() {

    val user: MutableLiveData<Response<FirebaseUser>> = MutableLiveData()

    fun login(activity: Activity, account: GoogleSignInAccount) {
        user.value = Response(Response.ViewState.LOADING, null, null)
        repository.profileRepository.firebaseAuthWithGoogle(activity, account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            user.value = Response(Response.ViewState.SUCCESS, it, null)
                        },
                        onError = {
                            user.value = Response(Response.ViewState.ERROR, null, it)
                        }
                )
    }
}