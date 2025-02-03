package com.robingebert.blokky.feature_app.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreTime
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class AppOption {
    TIMER,
    LIMIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTaskOptionsBottomSheet(
    onDismiss: () -> Unit,
    onSelected: (AppOption) -> Unit
) {
    ModalBottomSheet(
        content = {
            ListItem(
                icon = Icons.Rounded.MoreTime,
                title = "Add timer"
            ) {
                onSelected(AppOption.TIMER)
            }

        },
        onDismissRequest = { onDismiss() }
    )
}

@Composable
fun ListItem(modifier: Modifier = Modifier, icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onClick()
            }
    ) {
        Row(
            Modifier.padding(15.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Icon(modifier = Modifier.size(30.dp), imageVector = icon, contentDescription = "")
            Spacer(Modifier.width(10.dp))
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview
@Composable
fun ListItemPreview() {
    Column {
        ListItem(
            icon = Icons.Rounded.MoreTime,
            title = "Add timer"
        ) {

        }
        ListItem(icon = Icons.Rounded.Timelapse, title = "Add time limit") { }
    }
}