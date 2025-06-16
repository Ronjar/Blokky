package com.robingebert.blokky.feature_preferences.repository.models

import kotlinx.serialization.Serializable

@Serializable
data class App(
    val name: String,
    val blocked: Boolean,
    val blockedStart: Int,
    val blockedEnd: Int,
    val blockedTimer: Int,
    val features: List<Feature>
)
