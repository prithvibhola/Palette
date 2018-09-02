package io.palette.utility.extentions

import io.palette.utility.DynamicHeightImageView

fun DynamicHeightImageView.maintainAspectRatio(width: Long, height: Long) {
    val aspectRatio = (width / height).toFloat()
    this.setAspectRatio(if (aspectRatio <= 0.0) 1.5F else aspectRatio)
}