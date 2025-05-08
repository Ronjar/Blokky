package com.robingebert.blokky.models

import kotlinx.serialization.Serializable

@Serializable
data class App(
    val name: String,
    val blocked: Boolean,
    val blockedStart: Int,
    val blockedEnd: Int,
    val blockedTimer: Int
)
