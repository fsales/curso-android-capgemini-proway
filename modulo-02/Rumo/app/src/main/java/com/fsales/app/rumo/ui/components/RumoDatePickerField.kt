package com.fsales.app.rumo.ui.components

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.R
import com.fsales.app.rumo.ui.mapper.formatarParaLocale
import com.fsales.app.rumo.ui.theme.RumoTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

// =============================================================================
// Campo de data reutilizável — OutlinedTextField read-only + DatePickerDialog MD3
// Reutilizável em Ganho, Gasto e Sonho.
// =============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RumoDatePickerField(
    data: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    erro: String? = null,
) {
    var dialogAberto by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = data.formatarParaLocale(Locale.getDefault()),
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = { dialogAberto = true }) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = stringResource(R.string.campo_data_abrir_calendario),
                )
            }
        },
        isError = erro != null,
        supportingText = erro?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
        modifier = modifier,
    )

    if (dialogAberto) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = data
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
        )
        DatePickerDialog(
            onDismissRequest = { dialogAberto = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val localDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateSelected(localDate)
                    }
                    dialogAberto = false
                }) {
                    Text(stringResource(R.string.acao_confirmar))
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogAberto = false }) {
                    Text(stringResource(R.string.acao_cancelar))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "RumoDatePickerField · Light")
@Preview(showBackground = true, name = "RumoDatePickerField · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RumoDatePickerFieldPreview() {
    RumoTheme {
        RumoDatePickerField(
            data = LocalDate.of(2026, 4, 12),
            onDateSelected = {},
            label = "Data de recebimento",
        )
    }
}

@Preview(showBackground = true, name = "RumoDatePickerField · Erro · Light")
@Composable
private fun RumoDatePickerFieldErroPreview() {
    RumoTheme {
        RumoDatePickerField(
            data = LocalDate.of(2026, 4, 12),
            onDateSelected = {},
            label = "Data de recebimento",
            erro = "Data inválida",
        )
    }
}

