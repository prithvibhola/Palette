package io.palette.utility.imagePicker

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import io.palette.data.models.Source

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RxImagePicker private constructor(private val context: Context) {
    private var publishSubject: PublishSubject<Uri>? = null
    private var publishSubjectMultipleImages: PublishSubject<List<Uri>>? = null

    val activeSubscription: Observable<Uri>?
        get() = publishSubject

    fun requestImage(imageSource: Source): Observable<Uri> {
        publishSubject = PublishSubject.create()
        startImagePickHiddenActivity(imageSource.ordinal, false)
        return publishSubject!!
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun requestMultipleImages(): Observable<List<Uri>> {
        publishSubjectMultipleImages = PublishSubject.create()
        startImagePickHiddenActivity(Source.GALLERY.ordinal, true)
        return publishSubjectMultipleImages!!
    }

    internal fun onImagePicked(uri: Uri) {
        if (publishSubject != null) {
            publishSubject!!.onNext(uri)
            publishSubject!!.onComplete()
        }
    }

    internal fun onImagesPicked(uris: List<Uri>) {
        if (publishSubjectMultipleImages != null) {
            publishSubjectMultipleImages!!.onNext(uris)
            publishSubjectMultipleImages!!.onComplete()
        }
    }

    private fun startImagePickHiddenActivity(imageSource: Int, allowMultipleImages: Boolean) {
        val intent = Intent(context, HiddenActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(HiddenActivity.ALLOW_MULTIPLE_IMAGES, allowMultipleImages)
        intent.putExtra(HiddenActivity.IMAGE_SOURCE, imageSource)
        context.startActivity(intent)
    }

    companion object {

        private var instance: RxImagePicker? = null

        @Synchronized
        fun with(context: Context): RxImagePicker {
            if (instance == null) {
                instance = RxImagePicker(context.applicationContext)
            }
            return instance!!
        }
    }

}

