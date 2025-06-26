package com.example.adsimpl.util

import android.util.Log
import android.view.View
import com.example.adsimpl.viewmodel.RemoteEntry

fun getSubscriptionStatus(): Boolean {
    return false
}

fun checkAndHandleAdPermission(
    adName: String,
    adList: List<RemoteEntry>,
    onAllowed: () -> Unit
) {
    val adEntry = adList.find { it.name == adName }

    when (adEntry?.ads_enabled) {
        true -> {
            Log.d("AdControl", "$adName is enabled")
            onAllowed()
        }

        false -> {
            Log.d("AdControl", "$adName is disabled in remote config")
        }

        null -> {
            Log.d("AdControl", "Ad entry for $adName not found in remote config")
        }
    }
}

fun View.setSafeClickListener(inactiveTime: Long, onSafeClick: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener { view ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= inactiveTime) {
            lastClickTime = currentTime
            onSafeClick(view)
        }
    }
}