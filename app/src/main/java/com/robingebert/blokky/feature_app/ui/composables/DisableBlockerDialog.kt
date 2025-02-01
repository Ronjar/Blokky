package com.robingebert.blokky.feature_app.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DisableBlockerDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Are you sure?",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Are you sure you want / can allow yourself to doomscroll again?",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Changed my mind")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TimedFilledButton(
                        text = "I can handle it",
                        onClick = onConfirmation,
                        modifier = Modifier
                            .height(48.dp)
                            .widthIn(min = 150.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TimedFilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isClickEnabled by remember { mutableStateOf(false) }
    var animationEnabled by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animationEnabled = true
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animationEnabled) 1.0f else 0f,
        animationSpec = tween(durationMillis = 5000, easing = LinearEasing),
        finishedListener = {
            isClickEnabled = true
        }
    )
    Box(modifier = modifier) {
        LinearProgressIndicator(
            progress = { animatedProgress },
            strokeCap = StrokeCap.Butt,
            gapSize = 0.dp,
            drawStopIndicator = {},
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(24.dp)),
            color = MaterialTheme.colorScheme.surfaceTint,
            trackColor = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.2f)
        )
        Button(
            onClick = {
                if (isClickEnabled) {
                    onClick()
                }
            },
            modifier = Modifier.matchParentSize(),
            colors = ButtonDefaults.buttonColors(
                containerColor =  Color.Transparent
            ),
        ) {
            Text(text)
        }
    }
}


@Preview
@Composable
fun DisableBlockerDialogPreview() {
    DisableBlockerDialog({}, {})
}