package io.palette.ui.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import io.palette.R
import io.palette.ui.base.BaseActivity

class AboutActivity : BaseActivity() {

    companion object {
        fun newInstance(context: Context) = Intent(context, AboutActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_about)
    }
}