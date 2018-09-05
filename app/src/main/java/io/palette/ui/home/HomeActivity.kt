package io.palette.ui.home

import android.os.Bundle
import io.palette.R
import io.palette.di.ActivityScoped
import io.palette.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

@ActivityScoped
class HomeActivity @Inject constructor() : BaseActivity() {

    @Inject lateinit var homeAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        window.setBackgroundDrawableResource(R.color.colorPrimary)

        viewPager.apply {
            adapter = homeAdapter
            offscreenPageLimit = 2
        }
    }
}
