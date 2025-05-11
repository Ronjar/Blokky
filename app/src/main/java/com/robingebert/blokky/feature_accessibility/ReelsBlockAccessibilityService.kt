package com.robingebert.blokky.feature_accessibility

import android.accessibilityservice.AccessibilityService
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
import java.util.TimeZone

class ReelsBlockAccessibilityService : AccessibilityService(), KoinComponent {

    private val dataStore: DataStoreManager by inject()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Volatile
    private var settings = AppSettings()

    // statt Boolean + Handler: letztes Click-Timestamp
    private var lastActionTime = 0L
    // passe hier an, wie lange du blockieren willst (z. B. 200ms)
    private val debounceMillis = 200L

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceScope.launch {
            dataStore.appSettings.collect { latest ->
                settings = latest
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val pkg = event?.packageName?.toString() ?: return
        val root = rootInActiveWindow ?: return

        val nowMin = currentMinuteOfDay()
        when (pkg) {
            "com.instagram.android" -> {
                val insta = settings.instagram
                if (insta.blocked && isWithinInterval(insta.blockedStart, insta.blockedEnd, nowMin)) {
                    blockInstagram(root)
                }
            }
            "com.google.android.youtube" -> {
                val yt = settings.youtube
                if (yt.blocked && isWithinInterval(yt.blockedStart, yt.blockedEnd, nowMin)) {
                    blockYouTube(root)
                }
            }
            "com.zhiliaoapp.musically" -> {
                val tt = settings.tiktok
                if (tt.blocked && isWithinInterval(tt.blockedStart, tt.blockedEnd, nowMin)) {
                    blockTikTok()
                }
            }
            // alle anderen Pakete ignorieren
        }
    }

    override fun onInterrupt() { /* no-op */ }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun currentMinuteOfDay(): Int {
        val now = System.currentTimeMillis()
        val offset = TimeZone.getDefault().getOffset(now)
        return (((now + offset) / 60000) % 1440).toInt()
    }

    private fun isWithinInterval(start: Int, end: Int, minute: Int): Boolean {
        return if (start <= end) {
            minute in start..end
        } else {
            minute >= start || minute <= end
        }
    }

    private fun blockInstagram(root: AccessibilityNodeInfo) {
        val reelView = root.findAccessibilityNodeInfosByViewId(
            "com.instagram.android:id/clips_swipe_refresh_container"
        ).firstOrNull()

        if (reelView != null) {
            val feedTab = root.findAccessibilityNodeInfosByViewId(
                "com.instagram.android:id/feed_tab"
            ).firstOrNull()
            exitTheDoom(feedTab)
        }
    }

    private fun blockYouTube(root: AccessibilityNodeInfo) {
        root.findAccessibilityNodeInfosByViewId(
            "com.google.android.youtube:id/reel_watch_fragment_root"
        ).firstOrNull() ?: return

        val pivotBar = root.findAccessibilityNodeInfosByViewId(
            "com.google.android.youtube:id/pivot_bar"
        ).firstOrNull()
        val homeTab = pivotBar?.getChild(0)?.getChild(0)

        exitTheDoom(homeTab) {
            homeTab?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }

    private fun blockTikTok() {
        exitTheDoom(null) {
            performGlobalAction(GLOBAL_ACTION_HOME)
        }
    }

    private fun exitTheDoom(
        node: AccessibilityNodeInfo?,
        extra: (() -> Unit)? = null
    ) {
        val now = System.currentTimeMillis()
        if (now - lastActionTime < debounceMillis) return
        lastActionTime = now

        if (node != null) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        } else {
            performGlobalAction(GLOBAL_ACTION_BACK)
        }
        extra?.invoke()
    }
}
