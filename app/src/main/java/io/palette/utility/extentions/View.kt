package io.palette.utility.extentions

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Handler
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.createBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.draw(canvas)
    return bitmap
}

fun View.animateAlpha(animDuration: Long = 700, animStartDelay: Long = 0) {
    ObjectAnimator.ofFloat(this, "alpha", 0F, 1F).apply {
        duration = animDuration
        startDelay = animStartDelay
        interpolator = LinearInterpolator()
        addAnimatorListener(
                onStart = { this@animateAlpha.isClickable = false },
                onEnd = { this@animateAlpha.isClickable = true }
        )
        start()
    }
}
