package com.robingebert.blokky.feature_app.ui.composables

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.strabled.composepreferences.utilis.Preference

@Composable
fun SwitchPreference(
    preference: Preference<Boolean>,
    title: String,
    summary: String,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit),
    onValueChange: (Boolean) -> Unit = {}
) {
    val preferenceValue by preference.collectState()

    fun edit(newValue: Boolean) {
        try {
            preference.updateValue(newValue)
            onValueChange(newValue)
        } catch (e: Exception) {
            Log.e("SwitchPreference", "Could not write preference $preference to database.", e)
        }
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.8F)
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .toggleable(
                value = preferenceValue,
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
                checked = preferenceValue,
                onCheckedChange = { edit(it) },
                enabled = enabled
            )
        }
    }
}