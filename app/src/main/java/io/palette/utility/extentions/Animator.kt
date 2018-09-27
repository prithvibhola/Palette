package io.palette.utility.extentions

import android.animation.Animator

fun Animator.addAnimatorListener(
        onRepeat: (Animator) -> Unit = {},
        onEnd: (Animator) -> Unit = {},
        onCancel: (Animator) -> Unit = {},
        onStart: (Animator) -> Unit = {}
) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {
            onRepeat(animation)
        }

        override fun onAnimationEnd(animation: Animator) {
            onEnd(animation)
        }

        override fun onAnimationCancel(animation: Animator) {
            onCancel(animation)
        }

        override fun onAnimationStart(animation: Animator) {
            onStart(animation)
        }
    })
}