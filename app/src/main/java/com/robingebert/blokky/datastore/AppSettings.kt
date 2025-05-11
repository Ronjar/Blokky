package com.robingebert.blokky.datastore

import com.robingebert.blokky.models.App
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val instagram: App = App(
        name = "Instagram",
        blocked = false,
        blockedStart = 0,
        blockedEnd = 1439,
        blockedTimer = 0
    ),
    val youtube: App = App(
        name = "YouTube",
        blocked = false,
        blockedStart = 0,
        blockedEnd = 1439,
        blockedTimer = 0
    ),
    val tiktok: App = App(
        name = "TikTok",
        blocked = false,
        blockedStart = 0,
        blockedEnd = 1439,
        blockedTimer = 0
    )
)