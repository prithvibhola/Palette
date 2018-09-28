package io.palette.ui.settings

import android.os.Bundle
import io.palette.R
import io.palette.ui.base.BaseActivity
import io.palette.utility.extentions.setScreenTitle
import io.palette.utility.preference.PreferenceUtility
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

class SettingsActivity : BaseActivity() {

    @Inject lateinit var preferences: PreferenceUtility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar)
        supportActionBar?.setScreenTitle(getString(R.string.title_settings))

        switchRgb.apply {
            isChecked = preferences.prefShowRGB
            setOnCheckedChangeListener { _, value -> preferences.prefShowRGB = value }
        }
    }
}