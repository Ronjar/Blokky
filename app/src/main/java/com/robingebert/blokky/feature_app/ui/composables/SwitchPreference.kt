package com.robingebert.blokky.feature_app.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun SwitchPreference(
    value: Boolean,
    title: String,
    summary: String,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit),
    settingsIcon: @Composable (() -> Unit)? = null,
    onValueChange: (Boolean) -> Unit,
) {

    var showDisableBlockerDialog by remember { mutableStateOf(false) }

    fun edit(newValue: Boolean, force: Boolean = false) {
        if (!force) {
            if (!newValue) {
                showDisableBlockerDialog = true
                return
            }
        }
        onValueChange(newValue)
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.8F)
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .toggleable(
                value = value,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = { edit(it) }
            ),
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon()
            Spacer(modifier = Modifier.width(4.dp))
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = summary, style = MaterialTheme.typography.bodyMedium)
            }
            Switch(
                checked = value,
                onCheckedChange = { edit(it) },
                enabled = enabled
            )
            settingsIcon?.invoke()
        }
    }

    if (showDisableBlockerDialog) {
        DisableBlockerDialog(
            onDismissRequest = { showDisableBlockerDialog = false },
            onConfirmation = {
                showDisableBlockerDialog = false
                edit(newValue = false, force = true)
            }
        )
    }
}
/*
@Composable
fun DisableBlockerDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(Icons.Rounded.Warning, contentDescription = null)
        },
        title = {
            Text(text = "Are you sure?")
        },
        text = {
            Text(text = "Are you sure you want / can allow yourself to doomscroll again?")
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("I can handle it")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Changed my mind")
            }
        }
    )
}*/