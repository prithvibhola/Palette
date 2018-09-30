package io.palette.utility.extentions

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator

fun View.snackBar(@StringRes resId: Int) = Snackbar.make(this, resId, Snackbar.LENGTH_SHORT).show()
fun View.snackBarLong(@StringRes resId: Int) = Snackbar.make(this, resId, Snackbar.LENGTH_LONG).show()
fun View.snackBar(text: String) = Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show()
fun View.snackBarLong(text: String) = Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

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
                onStart = { this@pulseAnimation.isClickable = false },
                onEnd = {
                    this@pulseAnimation.isClickable = true
                    this@pulseAnimation.scaleX = 1F
                    this@pulseAnimation.scaleY = 1F
                }
        )
        start()
    }
}

fun View.downloadAnimation(): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "translationY", -100F, 80F).apply {
        duration = 1400
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.RESTART
        addAnimatorListener(
                onStart = { this@downloadAnimation.isClickable = false },
                onEnd = {
                    this@downloadAnimation.isClickable = true
                    this@downloadAnimation.translationY = 0F
                }
        )
        start()
    }
}