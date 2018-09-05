package io.palette.repository

import android.app.Activity
import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import java.sql.SQLException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor() {

    @Inject lateinit var mAuth: FirebaseAuth
//    @Inject lateinit var  activity: Activity

    fun firebaseAuthWithGoogle(activity: Activity, account: GoogleSignInAccount): Flowable<FirebaseUser> {
        var user: FirebaseUser? = null
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return Flowable.create({ emitter ->
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(activity) {
                        if (it.isSuccessful) {
                            emitter.onNext(mAuth.currentUser!!)
                            emitter.onComplete()
                        } else {
                            emitter.onError(Exception("Failed to get the user"))
                        }
                    }
        }, BackpressureStrategy.BUFFER)

//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(activity, {
//                    user = if (it.isSuccessful) {
//                        mAuth.currentUser
//                    } else {
//                        null
//                    }
//                })
//        return user
    }
}