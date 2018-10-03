package io.palette.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.palette.data.models.Unsplash
import io.palette.utility.extentions.paletteCollection
import io.palette.utility.extentions.snapshotAsFlowable
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
        private val firebaseAuth: FirebaseAuth,
        private val fireStore: FirebaseFirestore
) {

    fun getLikedPalettes(): Flowable<List<Unsplash>> =
            fireStore.paletteCollection(firebaseAuth.currentUser!!.uid)
                    .snapshotAsFlowable()
                    .filter { !it.isEmpty }
                    .map { it.toObjects(Unsplash::class.java) }
}