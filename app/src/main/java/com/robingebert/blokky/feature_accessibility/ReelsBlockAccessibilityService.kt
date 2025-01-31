package com.robingebert.blokky.feature_accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class ReelsBlockAccessibilityService : AccessibilityService() {

    companion object {
        val Context.dataStore by preferencesDataStore(name = "preferences")
    }

    var hasExitedTheDoom = false

    @OptIn(DelicateCoroutinesApi::class)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return

        val dataStore = applicationContext.dataStore
        val rootNode = rootInActiveWindow ?: return

        GlobalScope.launch(Dispatchers.IO) {
            when (rootNode.packageName.toString()) {
                // Instagram
                "com.instagram.android" -> {
                    dataStore.data.map { it[stringPreferencesKey("instagram_reels_blocked")] }
                        .collectLatest {
                            if (it == null || it.toBoolean() != false) {
                                val reelView =
                                    rootNode.findAccessibilityNodeInfosByViewId("com.instagram.android:id/clips_swipe_refresh_container")
                                        .firstOrNull()
                                reelView?.run {
                                    val feedButton =
                                        rootNode.findAccessibilityNodeInfosByViewId("com.instagram.android:id/feed_tab")
                                            .firstOrNull()
                                    feedButton?.exitTheDoom()

                                }
                            }
                        }
                }
                // YouTube
                "com.google.android.youtube" -> {
                    dataStore.data.map { it[stringPreferencesKey("youtube_shorts_blocked")] }
                        .collectLatest {
                            if (it == null || it.toBoolean() != false) {
                                val reelView =
                                    rootNode.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/reel_watch_fragment_root")
                                        .firstOrNull()
                                reelView?.run {
                                    val feedButton =
                                        rootNode.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/pivot_bar")
                                            .firstOrNull()?.getChild(0)?.getChild(0)
                                    feedButton?.exitTheDoom()
                                    feedButton?.performAction(AccessibilityNodeInfo.ACTION_CLICK)

                                }
                            }
                        }
                }

                else -> hasExitedTheDoom = false
            }
        }
    }

    override fun onInterrupt() {
        // Wird benötigt, aber hier nicht genutzt
    }

    suspend fun AccessibilityNodeInfo.exitTheDoom() {
        if (!hasExitedTheDoom) {
            hasExitedTheDoom = true
            this.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            delay(500)
            hasExitedTheDoom = false
        }
    }
}