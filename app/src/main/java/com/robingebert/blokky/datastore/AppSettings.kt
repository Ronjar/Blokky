package com.robingebert.blokky.datastore

import com.robingebert.blokky.feature_preferences.repository.models.App
import com.robingebert.blokky.feature_preferences.repository.models.Feature
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val instagram: App = App(
        name = "Instagram",
        blocked = false,
        blockedStart = 0,
        blockedEnd = 1439,
        blockedTimer = 0,
        features = listOf(
            Feature(
                name = "Reels",
                enabled = true,
                startTime = 0,
                endTime = 1439,
            ),
            Feature(
                name = "Stories",
                enabled = false,
                startTime = 0,
                endTime = 1439,
            ),
            Feature(
                name = "Search",
                enabled = false,
                startTime = 0,
                endTime = 1439,
            ),
            Feature(
                name = "Comments",
                enabled = false,
                startTime = 0,
                endTime = 1439,
            )
        )
    ),
    val youtube: App = App(
        name = "YouTube",
        blocked = false,
        blockedStart = 0,
        blockedEnd = 1439,
        blockedTimer = 0,
        features = listOf(
            Feature(
                name = "Shorts",
                enabled = true,
                startTime = 0,
                endTime = 1439,
            ),
        )
    ),
    val tiktok: App = App(
        name = "TikTok",
        blocked = false,
        blockedStart = 0,
        blockedEnd = 1439,
        blockedTimer = 0,
        features = emptyList()
    )
)