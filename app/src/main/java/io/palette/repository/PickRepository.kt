package io.palette.repository

import android.net.Uri
import android.support.v4.app.FragmentManager
import io.palette.utility.imagePicker.RxImagePicker
import io.palette.utility.imagePicker.Sources
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PickRepository @Inject constructor() {

    fun getImage(fragmentManager: FragmentManager, source: Sources): Flowable<Uri> =
            RxImagePicker.with(fragmentManager).requestImage(source).toFlowable(BackpressureStrategy.BUFFER)
}