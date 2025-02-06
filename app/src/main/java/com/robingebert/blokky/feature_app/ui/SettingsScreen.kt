package com.robingebert.blokky.feature_app.ui

import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.robingebert.blokky.R
import com.robingebert.blokky.feature_accessibility.ReelsBlockAccessibilityService.Companion.dataStore
import com.robingebert.blokky.feature_app.ui.composables.AccessibilityServiceCard
import com.robingebert.blokky.feature_app.ui.composables.SwitchPreference
import com.strabled.composepreferences.LocalDataStoreManager
import com.strabled.composepreferences.ProvideDataStoreManager
import com.strabled.composepreferences.getPreference
import com.strabled.composepreferences.setPreferences
import com.strabled.composepreferences.utilis.DataStoreManager
import com.strabled.composepreferences.utilis.buildPreferences

@Composable
fun SettingsScreen() {

    val context = LocalContext.current
    
    var isAccessibilityGranted by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                isAccessibilityGranted = context.isAccessibilityGranted()
            }
        }
    }
    Column(modifier = Modifier.padding(8.dp)) {

        AccessibilityServiceCard(isAccessibilityGranted)
        Spacer(modifier = Modifier.height(12.dp))

        ProvideDataStoreManager(dataStoreManager = DataStoreManager(context, context.dataStore)) {
            val preferences = buildPreferences(LocalDataStoreManager.current) {
                "instagram_reels_blocked" defaultValue true
                "youtube_shorts_blocked" defaultValue true
            }

            setPreferences(preferences)

            Column(verticalArrangement = spacedBy(8.dp)) {
                    SwitchPreference(
                        preference = getPreference("instagram_reels_blocked"),
                        enabled = isAccessibilityGranted,
                        title = "Instagram Reels",
                        summary = "Block Instagram Reels",
                        leadingIcon = {
                            InstagramColoredIcon()
                        }
                    )
                SwitchPreference(
                    preference = getPreference("youtube_shorts_blocked"),
                    enabled = isAccessibilityGranted,
                    title = "YouTube Shorts",
                    summary = "Block YouTube Shorts",
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.ic_youtube),
                            null,
                            tint = Color.Red
                        )
                    }
                )
            }
        }
    }
}


fun Context.isAccessibilityGranted(): Boolean {
    val am = this.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val runningServices =
        am.getEnabledAccessibilityServiceList(AccessibilityEvent.TYPE_VIEW_CLICKED)
    return runningServices.any { it.id == "com.robingebert.blokky/.feature_accessibility.ReelsBlockAccessibilityService" }
}

@Composable
fun InstagramColoredIcon() {
    val gradientBrush = remember {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFF58529),
                Color(0xFFDD2A7B),
                Color(0xFF8134AF),
                Color(0xFF515BD4)
            )
        )
    }

    Icon(
        painter = painterResource(R.drawable.ic_instagram),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(gradientBrush, blendMode = BlendMode.SrcAtop)
                }
            }
    )
}

@Preview
@Composable
fun SettingsLayoutPreview() {
    SettingsScreen()
}