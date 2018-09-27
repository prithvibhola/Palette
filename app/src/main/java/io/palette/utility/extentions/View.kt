package io.palette.utility.extentions

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.annotation.LayoutRes
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

fun View.pulseAnimation(): ObjectAnimator {
    return ObjectAnimator.ofPropertyValuesHolder(this,
            PropertyValuesHolder.ofFloat("scaleX", 1.2f),
            PropertyValuesHolder.ofFloat("scaleY", 1.2f)).apply {
        duration = 300
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.REVERSE
        addAnimatorListener(
                onStart = {
                    this@pulseAnimation.isClickable = false
                },
                onEnd = {
                    this@pulseAnimation.isClickable = true
                    this@pulseAnimation.scaleX = 1F
                    this@pulseAnimation.scaleY = 1F
                }
        )
        start()
    }
}
