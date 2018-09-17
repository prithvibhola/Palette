package io.palette.ui.home

import android.os.Bundle
import android.support.v4.view.ViewPager
import io.palette.R
import io.palette.di.ActivityScoped
import io.palette.ui.base.BaseActivity
import io.palette.utility.extentions.visible
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

@ActivityScoped
class HomeActivity @Inject constructor() : BaseActivity() {

    @Inject
    lateinit var homeAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        window.setBackgroundDrawableResource(R.color.colorPrimary)

        ivPickImage.setOnClickListener { viewPager.currentItem = 0 }
        ivUnsplash.setOnClickListener { viewPager.currentItem = 1 }
        ivProfile.setOnClickListener { viewPager.currentItem = 2 }

        viewPager.apply {
            adapter = homeAdapter
            offscreenPageLimit = 2
        }
    }
}
