package io.palette.utility.extentions

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import io.palette.utility.DynamicHeightImageView

fun DynamicHeightImageView.maintainAspectRatio(width: Long, height: Long) {
    val aspectRatio = (width / height).toFloat()
    this.setAspectRatio(if (aspectRatio <= 0.0) 1.5F else aspectRatio)
}

fun ImageView.setColor(@ColorRes color: Int) = this.setColorFilter(ContextCompat.getColor(context, color))

fun ImageView.setImage(@DrawableRes image: Int) = this.setImageDrawable(ContextCompat.getDrawable(context, image))

