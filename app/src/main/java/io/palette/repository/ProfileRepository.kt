package io.palette.repository

import android.app.Activity
import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.palette.data.models.Unsplash
import io.palette.utility.extentions.snapshotAsFlowable
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import timber.log.Timber
import java.sql.SQLException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
        private val firebaseAuth: FirebaseAuth,
        private val fireStore: FirebaseFirestore
) {

    @Inject lateinit var mAuth: FirebaseAuth

    fun firebaseAuthWithGoogle(activity: Activity, account: GoogleSignInAccount): Flowable<FirebaseUser> {
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
    }

    fun getLikedPalettes(): Flowable<List<Unsplash>> =
            fireStore.collection("users")
                    .document(firebaseAuth.currentUser!!.uid)
                    .collection("palettes")
                    .snapshotAsFlowable()
                    .map { it.toObjects(Unsplash::class.java) }
}