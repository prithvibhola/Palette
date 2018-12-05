package io.palette.ui.home

import android.os.Bundle
import android.support.v4.view.ViewPager
import com.airbnb.deeplinkdispatch.DeepLink
import com.google.firebase.messaging.FirebaseMessaging
import io.palette.R
import io.palette.di.ActivityScoped
import io.palette.ui.base.BaseActivity
import io.palette.utility.deeplink.AppDeepLink
import io.palette.utility.deeplink.WebDeepLink
import io.palette.utility.extentions.setColor
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

@ActivityScoped
@AppDeepLink("home/{id}")
@WebDeepLink("home/{id}")
class HomeActivity @Inject constructor() : BaseActivity() {

    @Inject
    lateinit var homeAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        FirebaseMessaging.getInstance().subscribeToTopic("all")

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        window.setBackgroundDrawableResource(R.color.colorPrimary)

        when {
            intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false) -> {
                viewPager.currentItem = intent?.extras?.getInt("id") ?: 0
            }
            else -> {
                viewPager.currentItem = 0
            }
        }

        ivPickImage.setColor(R.color.colorAccent)

        ivPickImage.setOnClickListener { viewPager.currentItem = 0 }
        ivUnsplash.setOnClickListener { viewPager.currentItem = 1 }
        ivProfile.setOnClickListener { viewPager.currentItem = 2 }

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                ivPickImage.setColor(if (position == 0) R.color.colorAccent else R.color.colorWhite)
                ivUnsplash.setColor(if (position == 1) R.color.colorAccent else R.color.colorWhite)
                ivProfile.setColor(if (position == 2) R.color.colorAccent else R.color.colorWhite)
            }
        })

        viewPager.apply {
            adapter = homeAdapter
            offscreenPageLimit = 2
        }
    }
}
