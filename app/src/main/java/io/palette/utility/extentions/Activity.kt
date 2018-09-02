package io.palette.utility.extentions

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.annotation.StringRes
import android.support.v4.app.FragmentActivity
import android.widget.Toast

internal fun <T : ViewModel> FragmentActivity.getViewModel(modelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory? = null): T {
    return viewModelFactory?.let { ViewModelProviders.of(this, it).get(modelClass) }
            ?: ViewModelProviders.of(this).get(modelClass)
}

fun Activity?.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
fun Activity?.toast(@StringRes resId: Int) = Toast.makeText(this, this?.getString(resId), Toast.LENGTH_SHORT).show()
fun Activity.longToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
fun Activity.longToast(@StringRes resId: Int) = Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show()