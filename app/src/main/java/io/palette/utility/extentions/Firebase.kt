package io.palette.utility.extentions

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import com.google.firebase.firestore.MetadataChanges
import io.palette.data.models.Optional
import io.reactivex.Completable

fun Query.getAsFlowable(): Flowable<QuerySnapshot> = Flowable.create({ emitter ->
    this.get().addOnCompleteListener {
        if (it.isSuccessful) {
            emitter.onNext(it.result)
            emitter.onComplete()
        } else {
            emitter.onError(it.exception ?: RuntimeException("Failed to get collection data"))
        }
    }
}, BackpressureStrategy.BUFFER)

fun Query.snapshotAsFlowable(): Flowable<QuerySnapshot> = Flowable.create({ emitter ->
    val registration = this@snapshotAsFlowable.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, exception ->
        if (exception != null) {
            emitter.onError(exception)
            return@addSnapshotListener
        }
        snapshot?.let { emitter.onNext(it) }
    }
    emitter.setCancellable { registration.remove() }
}, BackpressureStrategy.BUFFER)

fun DocumentReference.getAsFlowable(): Flowable<DocumentSnapshot> = Flowable.create({ emitter ->
    this.get().addOnCompleteListener {
        if (it.isSuccessful) {
            emitter.onNext(it.result)
            emitter.onComplete()
        } else {
            emitter.onError(it.exception ?: RuntimeException("Failed to get document data"))
        }
    }
}, BackpressureStrategy.BUFFER)

fun DocumentReference.snapshotAsFlowable(): Flowable<DocumentSnapshot> = Flowable.create({ emitter ->
    val registration = this.addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            emitter.onError(exception)
            return@addSnapshotListener
        }
        snapshot?.let { emitter.onNext(it) }
    }
    emitter.setCancellable { registration.remove() }
}, BackpressureStrategy.BUFFER)


fun <T : Any> Task<T>.toFlowable(): Flowable<Boolean> {
    return Flowable.create({ emitter ->
        this@toFlowable.addOnCompleteListener {
            if (it.isSuccessful) {
                emitter.onNext(true)
                emitter.onComplete()
            } else {
                emitter.onError(it.exception ?: RuntimeException("Failed to finish the task"))
            }
        }
    }, BackpressureStrategy.BUFFER)
}

fun FirebaseAuth.currentUserFlowable(): Flowable<Optional<FirebaseUser>> {
    return Flowable.create({ emitter ->
        val authChangeListener = { _: FirebaseAuth ->
            emitter.onNext(Optional(currentUser))
        }
        addAuthStateListener(authChangeListener)
        emitter.setCancellable { removeAuthStateListener(authChangeListener) }
    }, BackpressureStrategy.BUFFER)
}

fun <T : Any> Task<T>.getCompletable(): Completable {
    return Completable.create { emitter ->
        addOnSuccessListener { emitter.onComplete() }
        addOnFailureListener { emitter.onError(it) }
    }
}