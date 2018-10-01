package io.palette.utility.preference

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceUtility @Inject constructor(preferences: SharedPreferences) : BasePreferences(preferences) {

    enum class Key {
        PREF_KEY_UNSPLASH_STAGGERED,
        PREF_KEY_LIKED_STAGGERED,
        PREF_KEY_SHOW_RGB
    }

    var prefUnsplashStaggered by bindInt(Key.PREF_KEY_UNSPLASH_STAGGERED.name, 2)
    var prefLikedStaggered by bindInt(Key.PREF_KEY_LIKED_STAGGERED.name, 2)
    var prefShowRGB by bindBoolean(Key.PREF_KEY_SHOW_RGB.name, true)
}