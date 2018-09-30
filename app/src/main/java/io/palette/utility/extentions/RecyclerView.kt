package io.palette.utility.extentions

import android.support.v7.widget.RecyclerView

private val onScrollStateChangedStub = { _: RecyclerView?, _: Int -> Unit }
private val onScrolledStub = { _: RecyclerView?, _: Int, _: Int -> Unit }

fun RecyclerView.scrollWatcher(
        onScrollStateChange: (recyclerView: RecyclerView?, newState: Int) -> Unit = onScrollStateChangedStub,
        onScroll: (recyclerView: RecyclerView?, dx: Int, dy: Int) -> Unit = onScrolledStub
) {

    addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            onScrollStateChange(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            onScroll(recyclerView, dx, dy)
        }
    })
}