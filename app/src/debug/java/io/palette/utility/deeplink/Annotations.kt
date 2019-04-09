package io.palette.utility.deeplink

import com.airbnb.deeplinkdispatch.DeepLinkSpec

@DeepLinkSpec(prefix = ["app://palette/"])
annotation class AppDeepLink(vararg val value: String)

@DeepLinkSpec(prefix = ["http://dev.palette.com/", "https://dev.palette.com/"])
annotation class WebDeepLink(vararg val value: String)
