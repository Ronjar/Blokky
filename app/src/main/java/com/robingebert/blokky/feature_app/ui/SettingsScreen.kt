package com.robingebert.blokky.feature_app.ui

//import com.robingebert.blokky.feature_accessibility.ReelsBlockAccessibilityService.Companion.dataStore
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.robingebert.blokky.R
import com.robingebert.blokky.feature_app.SettingsViewModel
import com.robingebert.blokky.feature_app.ui.composables.AccessibilityServiceCard
import com.robingebert.blokky.feature_app.ui.composables.InstagramColoredIcon
import com.robingebert.blokky.feature_app.ui.composables.SwitchPreference
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = koinViewModel()) {

    val context = LocalContext.current

    val appSettings by settingsViewModel.appSettings.collectAsState()


    //region Accessibility Service
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
    //endregion


    Column(modifier = Modifier.padding(8.dp)) {

        AccessibilityServiceCard(isAccessibilityGranted)
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = spacedBy(8.dp)) {
            SwitchPreference(
                value = appSettings.instagram.blocked,
                enabled = isAccessibilityGranted,
                title = "Instagram Reels",
                summary = "Block Instagram Reels",
                leadingIcon = {
                    InstagramColoredIcon()
                },
                settingsIcon = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = "Settings for Instagram",
                        )
                    }

                },
            ) {
                settingsViewModel.updateInstagram(
                    appSettings.instagram.copy(
                        blocked = it
                    )
                )
            }
            SwitchPreference(
                value = appSettings.youtube.blocked,
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
            ) {
                settingsViewModel.updateYoutube(
                    appSettings.youtube.copy(
                        blocked = it
                    )
                )
            }
            SwitchPreference(
                value = appSettings.tiktok.blocked,
                enabled = isAccessibilityGranted,
                title = "TikTok",
                summary = "Block TikTok (whole app)",
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.ic_tiktok),
                        null,
                        tint = Color.Unspecified
                    )
                }
            ){
                settingsViewModel.updateTikTok(
                    appSettings.tiktok.copy(
                        blocked = it
                    )
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

@Preview
@Composable
fun SettingsLayoutPreview() {
    SettingsScreen()
}