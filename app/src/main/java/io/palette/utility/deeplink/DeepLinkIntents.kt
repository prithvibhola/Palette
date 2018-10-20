package io.palette.utility.deeplink

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.TaskStackBuilder
import io.palette.ui.detail.DetailActivity
import io.palette.ui.home.HomeActivity

object DeepLinkIntents {

    @JvmStatic
    @AppDeepLink("detail/{unsplash_id}")
    @WebDeepLink("detail/{unsplash_id}")
    fun detailIntent(context: Context, extras: Bundle): TaskStackBuilder {
        val homeIntent = Intent(context, HomeActivity::class.java)
        val detailIntent = Intent(context, DetailActivity::class.java)
        val taskStackBuilder = TaskStackBuilder.create(context)
        taskStackBuilder.addNextIntent(homeIntent)
        taskStackBuilder.addNextIntent(detailIntent)
        return taskStackBuilder
    }
}