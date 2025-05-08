package com.robingebert.blokky.feature_accessibility

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.robingebert.blokky.datastore.AppSettings
import com.robingebert.blokky.datastore.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ContentBlockAccessibilityService : AccessibilityService(), KoinComponent {

    // DataStoreManager liefert: val appSettings: Flow<AppSettings>
    private val dataStore: DataStoreManager by inject()

    // Service-Scope, wird mit onDestroy gecancelt
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Zwischengespeicherte Einstellungen
    @Volatile
    private var settings = AppSettings()

    // Für das Debouncing der "Back/Click"-Aktion
    private val mainHandler = Handler(Looper.getMainLooper())
    private var hasExitedTheDoom = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        // Einmaliges Sammeln der aktuellen Settings
        serviceScope.launch {
            dataStore.appSettings.collect { latest ->
                settings = latest
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val pkg = event?.packageName?.toString() ?: return
        val rootNode = rootInActiveWindow ?: return

        when (pkg) {
            "com.instagram.android" -> blockInstagram(rootNode)
            "com.google.android.youtube" -> blockYouTube(rootNode)
            "com.zhiliaoapp.musically" -> blockTikTok()
            else -> hasExitedTheDoom = false
        }
    }

    override fun onInterrupt() {
        // nothing to do
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun blockInstagram(root: AccessibilityNodeInfo) {
        val instagram = settings.instagram
        if (!instagram.blocked) return

        // ID des Reels-Containers
        val reelView = root.findAccessibilityNodeInfosByViewId(
            "com.instagram.android:id/clips_swipe_refresh_container"
        ).firstOrNull()

        if (reelView != null) {
            // Klick auf den Feed-Tab erzwingen
            val feedTab = root.findAccessibilityNodeInfosByViewId(
                "com.instagram.android:id/feed_tab"
            ).firstOrNull()
            exitTheDoom(feedTab)
        }
    }

    private fun blockYouTube(root: AccessibilityNodeInfo) {
        val yt = settings.youtube
        if (!yt.blocked) return

        val reelRoot = root.findAccessibilityNodeInfosByViewId(
            "com.google.android.youtube:id/reel_watch_fragment_root"
        ).firstOrNull() ?: return

        // Unteres Pivot-Bar-Element (Home-Tab) antippen
        val pivotBar = root.findAccessibilityNodeInfosByViewId(
            "com.google.android.youtube:id/pivot_bar"
        ).firstOrNull()
        val homeTab = pivotBar
            ?.getChild(0)
            ?.getChild(0)

        exitTheDoom(homeTab) {
            // Bei YouTube zusätzlich sicherstellen, dass wir im Feed sind
            homeTab?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }

    private fun blockTikTok() {
        val tt = settings.tiktok
        if (!tt.blocked) return

        // Komplett nach Home springen
        exitTheDoom(null) {
            performGlobalAction(GLOBAL_ACTION_HOME)
        }
    }

    /**
     * Führe genau einmal innerhalb eines kurzen Zeitfensters eine Aktion aus.
     * Danach ist für 500ms gesperrt (Debounce).
     */
    private fun exitTheDoom(
        node: AccessibilityNodeInfo?,
        extra: (() -> Unit)? = null
    ) {
        if (hasExitedTheDoom) return
        hasExitedTheDoom = true

        // Klick auf Node oder global Back
        if (node != null) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        } else {
            performGlobalAction(GLOBAL_ACTION_BACK)
        }
        extra?.invoke()

        // Reset nach 500ms
        mainHandler.postDelayed({ hasExitedTheDoom = false }, 500)
    }
}
