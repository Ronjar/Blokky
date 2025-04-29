package com.robingebert.blokky.feature_app.ui.composables

/*import TimePickerDialog
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
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material.icons.rounded.Woman
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.robingebert.paraparia.feature_betting.repository.Bet
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

import com.robingebert.paraparia.common.composables.datetimepickers.DatePickerModal
import kotlinx.datetime.Instant
import kotlinx.datetime.atTime
import kotlin.let

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBlockerBottomSheet(
    bet: Bet,
    onDismiss: () -> Unit,
    onSave: (Bet) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
) {
    var title by remember { mutableStateOf(bet.title) }
    var description by remember { mutableStateOf(bet.description) }
    var end by remember { mutableStateOf(bet.end.toLocalDateTime(TimeZone.currentSystemDefault())) }
    var wagerRobin by remember { mutableStateOf(bet.wagerRobin) }
    var wagerMone by remember { mutableStateOf(bet.wagerMone) }


    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    fun save() {
        onSave(
            bet.copy(
                title = title,
                description = description,
                end = end.toInstant(TimeZone.currentSystemDefault()),
                wagerRobin = wagerRobin,
                wagerMone = wagerMone
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

                IconRow(icon = Icons.Rounded.Title) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Titel") }
                    )
                }
                Spacer(Modifier.height(12.dp))
                IconRow(icon = Icons.Rounded.Menu) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Beschreibung") }
                    )
                }

                Spacer(Modifier.height(12.dp))
                IconRow(icon = Icons.Rounded.Event) {
                    Row {
                        val resetSourceDate = remember {
                            MutableInteractionSource()
                        }
                        if (resetSourceDate.collectIsPressedAsState().value) {
                            showDatePicker = true
                        }
                        TextField(
                            modifier = Modifier.weight(2f),
                            value = end.format(
                                LocalDateTime.Format{
                                    dayOfMonth()
                                    char('.')
                                    monthNumber()
                                    char('.')
                                    year()
                                    char(' ')
                                }),
                            readOnly = true,
                            interactionSource = resetSourceDate,
                            onValueChange = {},
                            label = { Text("Datum") }
                        )
                        Spacer(Modifier.width(4.dp))
                        val resetSourceTime = remember {
                            MutableInteractionSource()
                        }
                        if (resetSourceTime.collectIsPressedAsState().value) {
                            showTimePicker = true
                        }
                        TextField(
                            modifier = Modifier.weight(1f),
                            value = end.format(
                                LocalDateTime.Format{
                                    hour()
                                    char(':')
                                    minute()
                                }),
                            readOnly = true,
                            interactionSource = resetSourceTime,
                            onValueChange = {  },
                            label = { Text("Uhrzeit") }
                        )
                    }

                }
                Spacer(Modifier.height(12.dp))
                IconRow(icon = Icons.Rounded.Man) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = wagerRobin,
                        onValueChange = { wagerRobin = it },
                        label = { Text("Einsatz Robin") }
                    )
                }
                Spacer(Modifier.height(12.dp))
                IconRow(icon = Icons.Rounded.Woman) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = wagerMone,
                        onValueChange = { wagerMone = it },
                        label = { Text("Einsatz Mone") }
                    )
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

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = {
                it?.let {
                    val newDate = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault())
                    end = newDate.date.atTime(end.time)
                }
                showDatePicker = false
            }
        ) {
            showDatePicker = false
        }
    }

    if(showTimePicker) {
        TimePickerDialog(
            initialTime = end.time,
            onConfirm = {
                end = end.date.atTime(it)
                showTimePicker = false
            },
            onDismiss = {showTimePicker = false},
            title = "Zeit auswählen"
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


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ShowResetBottomSheetPreview() {

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    LaunchedEffect(key1 = Unit) {
        bottomSheetState.expand()
    }
    EditBetBottomSheet(
        bet = Bet(),
        sheetState = SheetState(
            skipPartiallyExpanded = true,
            density = Density(LocalContext.current),
            initialValue = SheetValue.Expanded
        ), onDismiss = {}, onSave = {})
}
*/