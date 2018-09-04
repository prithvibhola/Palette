package io.palette.utility.extentions

import android.graphics.Bitmap
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

private val onResourceReadyStub = { _: Bitmap?, _: Any?, _: Target<Bitmap>?, _: DataSource?, _: Boolean -> Unit }
private val onLoadFailedStub = { _: GlideException?, _: Any?, _: Target<Bitmap>?, _: Boolean -> Unit }

fun RequestBuilder<Bitmap>.listen(
        resourceReady: (resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean) -> Unit = onResourceReadyStub,
        loadFailed: (e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean) -> Unit = onLoadFailedStub
): RequestBuilder<Bitmap> {
    return listener(object : RequestListener<Bitmap> {

        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            resourceReady(resource, model, target, dataSource, isFirstResource)
            return true
        }

        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
            loadFailed(e, model, target, isFirstResource)
            return true
        }
    })
}