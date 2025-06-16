package com.robingebert.blokky.feature_preferences.repository.models

import kotlinx.serialization.Serializable

@Serializable
data class Feature(
    val name: String,
    val enabled: Boolean,
    val startTime: Int,
    val endTime: Int = 1439,
    val skips: Int = 0,
    val skipTime: Int = 120,
)