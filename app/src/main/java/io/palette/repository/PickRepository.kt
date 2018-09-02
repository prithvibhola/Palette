package io.palette.repository

import android.content.Context
import android.net.Uri
import io.palette.data.models.Sources
import io.palette.utility.imagePicker.RxImagePicker
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PickRepository @Inject constructor() {

    fun getImage(context: Context, source: Sources): Flowable<Uri> {
        return RxImagePicker.with(context)
                .requestImage(source)
                .toFlowable(BackpressureStrategy.BUFFER)
    }
}