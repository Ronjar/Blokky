package com.robingebert.blokky.feature_app.ui.composables

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Accessibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun AccessibilityServiceCard(
    isAccessibilityGranted: Boolean
) {
    val context = LocalContext.current


    val update: (Boolean) -> Unit = {
        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        Toast.makeText(context, "Click on \"Blokky\" in the \"Downloaded Apps\" Tab. Then click the slider", Toast.LENGTH_LONG).show()
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
}

@Preview
@Composable
fun AccessibilityServiceCardPreview() {
    AccessibilityServiceCard(false)
}