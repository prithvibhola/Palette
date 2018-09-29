package io.palette.ui.home

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import io.palette.R
import io.palette.di.ActivityScoped
import io.palette.ui.pick.PickFragment
import io.palette.ui.profile.ProfileFragment
import io.palette.ui.unsplash.UnsplashFragment
import javax.inject.Inject

@ActivityScoped
class HomeAdapter @Inject constructor(
        val context: Context,
        fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    private val pageCount = 3

    override fun getItem(position: Int) = when (position) {
        0 -> PickFragment.newInstance()
        1 -> UnsplashFragment.newInstance()
        2 -> ProfileFragment.newInstance()
        else -> PickFragment.newInstance()
    }

    override fun getCount() = pageCount

    override fun getPageTitle(position: Int): String = when (position) {
        0 -> context.getString(R.string.title_pick)
        1 -> context.getString(R.string.title_unsplash)
        2 -> context.getString(R.string.title_profile)
        else -> context.getString(R.string.title_pick)
    }
}