package com.example.adsimpl.viewmodel

import kotlinx.serialization.Serializable

@Serializable
data class RemoteEntry(
    var name: String = "",
    var ads_enabled: Boolean = true
)
