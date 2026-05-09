@file:OptIn(ExperimentalMaterial3Api::class)
package com.fsales.app.smartcontact.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.smartcontact.ui.theme.SmartContactTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/**
 * Campo de data no padrão OutlinedTextField que abre um [DatePickerDialog]
 * ao ser clicado.
 *
 * A data é formatada conforme o Locale ativo no dispositivo Android
 * (obtido via [LocalConfiguration]), respeitando idioma e região do usuário.
 *
 * @param data           Valor atual da data (null = sem seleção).
 * @param onDateSelected Callback disparado quando o usuário confirma uma data.
 * @param label          Label exibida no campo.
 * @param modifier       Modifier aplicado ao OutlinedTextField.
 * @param erro           Mensagem de erro já formatada (null = sem erro).
 */
@Composable
fun SmartContactDatePickerField(
    data: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    erro: String? = null,
) {
    // Locale do dispositivo — respeita idioma e região configurados pelo usuário
    val locale: Locale = LocalConfiguration.current.locales[0]

    var showDialog by rememberSaveable { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = data
            ?.atStartOfDay(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
    )

    // Formata a data no padrão médio do Locale do dispositivo
    // Ex: pt-BR → "08 de mai. de 2026" | en-US → "May 8, 2026" | de-DE → "08.05.2026"
    val formatter = remember(locale) {
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale)
    }
    val textoData = data?.format(formatter) ?: ""

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed) showDialog = true
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onDateSelected(selectedDate)
                        }
                        showDialog = false
                    }
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(android.R.string.cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    OutlinedTextField(
        value             = textoData,
        onValueChange     = {},
        label             = { Text(label) },
        isError           = erro != null,
        supportingText    = erro?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
        trailingIcon      = {
            Icon(
                imageVector        = Icons.Default.CalendarMonth,
                contentDescription = label,
            )
        },
        readOnly          = true,
        singleLine        = true,
        interactionSource = interactionSource,
        modifier          = modifier,
    )
}

// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "DatePickerField · Vazio · Light")
@Preview(showBackground = true, name = "DatePickerField · Vazio · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SmartContactDatePickerFieldVazioPreview() {
    SmartContactTheme {
        SmartContactDatePickerField(
            data            = null,
            onDateSelected  = {},
            label           = "Data de nascimento",
        )
    }
}

@Preview(showBackground = true, name = "DatePickerField · Com data · Light", locale = "pt-BR")
@Preview(showBackground = true, name = "DatePickerField · Com data en-US", locale = "en")
@Composable
private fun SmartContactDatePickerFieldComDataPreview() {
    SmartContactTheme {
        SmartContactDatePickerField(
            data            = LocalDate.of(1990, 5, 8),
            onDateSelected  = {},
            label           = "Data de nascimento",
        )
    }
}

@Preview(showBackground = true, name = "DatePickerField · Com erro · Light")
@Composable
private fun SmartContactDatePickerFieldComErroPreview() {
    SmartContactTheme {
        SmartContactDatePickerField(
            data            = null,
            onDateSelected  = {},
            label           = "Data de nascimento",
            erro            = "O campo Data de nascimento é obrigatório.",
        )
    }
}
