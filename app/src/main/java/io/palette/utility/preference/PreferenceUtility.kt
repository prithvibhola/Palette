package io.palette.utility.preference

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceUtility @Inject constructor(preferences: SharedPreferences) : BasePreferences(preferences) {

    enum class Key {
        PREF_KEY_UNSPLASH_STAGGERED
    }

    var prefUnsplashStaggered by bindInt(Key.PREF_KEY_UNSPLASH_STAGGERED.name, 1)

}