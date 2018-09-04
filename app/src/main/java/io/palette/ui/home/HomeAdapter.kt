package io.palette.ui.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import io.palette.di.ActivityScoped
import io.palette.ui.pick.PickFragment
import io.palette.ui.profile.ProfileFragment
import io.palette.ui.unsplash.UnsplashFragment
import javax.inject.Inject

@ActivityScoped
class HomeAdapter @Inject constructor(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val pageCount = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PickFragment.newInstance()
            1 -> UnsplashFragment.newInstance()
            2 -> ProfileFragment.newInstance()
            else -> PickFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return pageCount
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Pick"
            1 -> "Unsplash"
            2 -> "Profile"
            else -> "Pick"
        }
    }
}