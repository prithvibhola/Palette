package io.palette.utility.extentions

import android.app.Activity
import android.arch.lifecycle.*
import android.content.Context
import android.os.Handler
import android.support.annotation.StringRes
import android.support.v4.app.FragmentActivity
import android.support.v7.app.ActionBar
import android.view.View
import android.widget.ImageView
import android.widget.Toast


internal fun <T : ViewModel> FragmentActivity.getViewModel(modelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory? = null): T {
    return viewModelFactory?.let { ViewModelProviders.of(this, it).get(modelClass) }
            ?: ViewModelProviders.of(this).get(modelClass)
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}

fun Activity?.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
fun Activity?.toast(@StringRes resId: Int) = Toast.makeText(this, this?.getString(resId), Toast.LENGTH_SHORT).show()
fun Activity.longToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
fun Activity.longToast(@StringRes resId: Int) = Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show()

fun Activity.withDelay(delay: Long = 100L, block: () -> Unit) = Handler().postDelayed(Runnable(block), delay)

fun ActionBar.setScreenTitle(screenTitle: String) {
    this.apply {
        setDisplayHomeAsUpEnabled(true)
        title = screenTitle
    }
}