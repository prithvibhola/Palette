package io.palette.utility.deeplink

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import io.palette.ui.home.HomeActivity

@DeepLinkHandler(AppDeepLinkModule::class)
class DeepLinkActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deepLinkDelegate = DeepLinkDelegate(AppDeepLinkModuleLoader())
        val result = deepLinkDelegate.dispatchFrom(this)
        if (!result.isSuccessful) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        finish()
    }
}
