package io.palette.ui.about

import android.os.Bundle
import android.os.PersistableBundle
import io.palette.R
import io.palette.ui.base.BaseActivity

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_about)
    }
}