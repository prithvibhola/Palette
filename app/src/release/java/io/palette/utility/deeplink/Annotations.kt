package io.palette.utility.deeplink

import com.airbnb.deeplinkdispatch.DeepLinkSpec

@DeepLinkSpec(prefix = ["app://palette/"])
annotation class AppDeepLink(vararg val value: String)

@DeepLinkSpec(prefix = ["http://palette.com/", "https://palette.com/", "http://www.palette.com/", "https://www.palette.com/"])
annotation class WebDeepLink(vararg val value: String)
