package io.palette.utility.extentions

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import android.arch.lifecycle.ViewModelProviders
import android.os.Handler
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.widget.Toast

internal fun <T : ViewModel> Fragment.getViewModel(modelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory? = null): T {
    return viewModelFactory?.let { ViewModelProviders.of(this, it).get(modelClass) }
            ?: ViewModelProviders.of(this).get(modelClass)
}

fun Fragment.toast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
fun Fragment.toast(@StringRes resId: Int) = Toast.makeText(requireContext(), this.getString(resId), Toast.LENGTH_SHORT).show()
fun Fragment.longToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
fun Fragment.longToast(@StringRes resId: Int) = Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_LONG).show()