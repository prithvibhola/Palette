package io.palette.ui.profile

import android.arch.lifecycle.MutableLiveData
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.palette.data.models.Response
import io.palette.repository.Repository
import io.palette.ui.base.BaseViewModel
import prithvi.io.mvvmstarter.utility.rx.Scheduler
import timber.log.Timber
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        val repository: Repository,
        val scheduler: Scheduler
) : BaseViewModel() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    val user: MutableLiveData<Response<FirebaseUser>> = MutableLiveData()

    fun setUser(response: IdpResponse?) {
        if (response == null)
            user.value = Response.success(if (firebaseAuth.currentUser != null) firebaseAuth.currentUser else null)
        else {
            Timber.e(response.error, "User could not sign in")
            user.value = Response.error(response.error!!.fillInStackTrace())
        }
    }
}