package com.robingebert.blokky.feature_app.ui.composables

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Accessibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@Composable
fun AccessibilityServiceCard(
    isAccessibilityGranted: Boolean
) {
    var showAccessibilityServiceDialog by remember { mutableStateOf(false) }

    val update: (Boolean) -> Unit = {
        showAccessibilityServiceDialog = true
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (!isAccessibilityGranted) MaterialTheme.colorScheme.errorContainer else Color(
                0x807DEF87
            )
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .toggleable(
                value = isAccessibilityGranted,
                role = Role.Switch,
                onValueChange = update
            ),
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 4.dp),
                imageVector = Icons.Rounded.Accessibility,
                contentDescription = null
            )
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                Text(
                    text = "Accessibility Service (" + if (!isAccessibilityGranted) {
                        "Not"
                    } else {
                        ""
                    } + "Granted)",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (isAccessibilityGranted) {
                        "Accessibility Service is needed to monitor activity in Apps. Click here to view Settings."
                    } else {
                        "Accessibility Service is needed to monitor activity in Apps. Click here to grant it."
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(
                checked = isAccessibilityGranted,
                onCheckedChange = { update(it) }
            )
        }
    }

    if (showAccessibilityServiceDialog) {
        AccessibilityServiceDialog {
            showAccessibilityServiceDialog = false
        }
    }
}

@Composable
fun AccessibilityServiceDialog(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    var site by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp).height(200.dp),
            ) {
                Text(text = "Accessibility Service", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Row{
                    AnimatedVisibility(
                        site == 0,
                        exit = slideOutHorizontally() + fadeOut()
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "In order to block reels, Accessibility Service needs to be enabled.\nI promise to only use it for this purpose.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            TextButton(
                                onClick = { site = 1 }
                            ) {
                                Text("Show me how")
                            }
                        }

                    }
                    AnimatedVisibility(
                        site == 1,
                        enter = slideInHorizontally {
                            with(density) { -40.dp.roundToPx() }
                        } + fadeIn(
                            initialAlpha = 0.3f
                        ),
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "After you opened accessibility settings, click on \"Blokky\" in the \"Downloaded Apps\" Tab. Then click the slider to enable/disable the service.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp),
                                onClick = {
                                    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                                    onDismissRequest()
                                }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Open Accessibility Settings")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AccessibilityDialogPreview() {
    AccessibilityServiceDialog {}
}


@Preview
@Composable
fun AccessibilityServiceCardPreview() {
    AccessibilityServiceCard(false)
}