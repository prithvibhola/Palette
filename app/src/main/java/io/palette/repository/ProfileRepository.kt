package io.palette.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.palette.data.models.Unsplash
import io.palette.utility.extentions.snapshotAsFlowable
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
        firebaseAuth: FirebaseAuth,
        fireStore: FirebaseFirestore
) {

    private val fireStoreCollectionPath = fireStore.collection("users").document(firebaseAuth.currentUser!!.uid).collection("palettes")

    fun getLikedPalettes(): Flowable<List<Unsplash>> =
            fireStoreCollectionPath
                    .snapshotAsFlowable()
                    .map { it.toObjects(Unsplash::class.java) }
}