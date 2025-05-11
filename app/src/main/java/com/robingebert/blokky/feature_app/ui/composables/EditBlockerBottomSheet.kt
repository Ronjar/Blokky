package com.robingebert.blokky.feature_app.ui.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.robingebert.blokky.models.App

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAppBottomSheet(
    app: App,
    onDismiss: () -> Unit,
    onSave: (App) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
) {
    var blockedStart by remember { mutableIntStateOf(app.blockedStart) }
    var blockedEnd by remember { mutableIntStateOf(app.blockedEnd) }
    //var blockedTimer by remember { mutableIntStateOf(app.blockedTimer) }

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    fun save() {
        onSave(
            app.copy(
                blockedStart = blockedStart,
                blockedEnd = blockedEnd,
                //blockedTimer = blockedTimer
            )
        )
        onDismiss()
    }

    ModalBottomSheet(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 20.dp),
            ) {
                Text(
                    text = "Settings for ${app.name}",
                    style = MaterialTheme.typography.headlineSmall,
                )

                Spacer(Modifier.height(12.dp))
                IconRow(icon = Icons.Rounded.Event) {
                    Row {
                        val resetSourceStart = remember {
                            MutableInteractionSource()
                        }
                        if (resetSourceStart.collectIsPressedAsState().value) {
                            showStartTimePicker = true
                        }
                        TextField(
                            modifier = Modifier.weight(1f),
                            value = blockedStart.toTime(),
                            readOnly = true,
                            interactionSource = resetSourceStart,
                            onValueChange = {  },
                            label = { Text("Start") }
                        )
                        Spacer(Modifier.width(4.dp))
                        val resetSourceTime = remember {
                            MutableInteractionSource()
                        }
                        if (resetSourceTime.collectIsPressedAsState().value) {
                            showEndTimePicker = true
                        }
                        TextField(
                            modifier = Modifier.weight(1f),
                            value = blockedEnd.toTime(),
                            readOnly = true,
                            interactionSource = resetSourceTime,
                            onValueChange = {  },
                            label = { Text("End") }
                        )
                    }

                }
                Spacer(Modifier.height(16.dp))
                Row(Modifier.padding(start = 46.dp)) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { save() }
                    ) {
                        Icon(Icons.Rounded.Save, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Speichern")
                    }
                }
            }

        },
        sheetState = sheetState,
        onDismissRequest = { onDismiss() }
    )

    if(showStartTimePicker) {
        TimePickerDialog(
            initialTime = blockedStart,
            onConfirm = {
                blockedStart = it
                showStartTimePicker = false
            },
            onDismiss = {showStartTimePicker = false},
            title = "Choose time"
        )
    }

    if(showEndTimePicker) {
        TimePickerDialog(
            initialTime = blockedEnd,
            onConfirm = {
                blockedEnd = it
                showEndTimePicker = false
            },
            onDismiss = {showEndTimePicker = false},
            title = "Choose time"
        )
    }
}

@Composable
fun IconRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    content: @Composable () -> Unit,
) {
    Row(modifier = modifier) {
        Icon(modifier = Modifier.size(30.dp), imageVector = icon, contentDescription = "")
        Spacer(modifier = Modifier.width(16.dp))
        content()
    }
}

fun Int.toTime(): String {
    val hours = this / 60
    val minutes = this % 60
    return "%02d:%02d".format(hours, minutes)
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ShowResetBottomSheetPreview() {

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    LaunchedEffect(key1 = Unit) {
        bottomSheetState.expand()
    }
    EditAppBottomSheet(
        app = App(
            name = "Instagram",
            blocked = true,
            blockedStart = 0,
            blockedEnd = 86400,
            blockedTimer = 0
        ),
        sheetState = SheetState(
            skipPartiallyExpanded = true,
            density = Density(LocalContext.current),
            initialValue = SheetValue.Expanded
        ), onDismiss = {}, onSave = {})
}