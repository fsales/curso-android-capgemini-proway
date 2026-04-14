package com.fsales.app.rumo.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.R
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.spacing
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

// =============================================================================
// Seletor de mês/ano — reutilizado em Ganho, Gasto e Sonho
// =============================================================================

private val formatadorMesAno: DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())

fun YearMonth.formatarMesAno(): String =
    this.format(formatadorMesAno)
        .replaceFirstChar { it.uppercaseChar() }

@Composable
fun SeletorMes(
    mesAno: YearMonth,
    onAnterior: () -> Unit,
    onProximo: () -> Unit,
    modifier: Modifier = Modifier,
    onSelecionarMesAno: (YearMonth) -> Unit = {},
) {
    var dialogAberto by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(onClick = onAnterior) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.seletor_mes_anterior),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        TextButton(onClick = { dialogAberto = true }) {
            Text(
                text = mesAno.formatarMesAno(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        IconButton(onClick = onProximo) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.seletor_mes_proximo),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }

    if (dialogAberto) {
        SeletorMesDialog(
            mesAno = mesAno,
            onConfirmar = { novoMesAno ->
                onSelecionarMesAno(novoMesAno)
                dialogAberto = false
            },
            onDismiss = { dialogAberto = false },
        )
    }
}

@Preview(showBackground = true, name = "SeletorMes · Light")
@Preview(showBackground = true, name = "SeletorMes · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SeletorMesPreview() {
    RumoTheme {
        Surface {
            var mesAno by remember { mutableStateOf(YearMonth.now()) }
            SeletorMes(
                mesAno = mesAno,
                onAnterior = { mesAno = mesAno.minusMonths(1) },
                onProximo = { mesAno = mesAno.plusMonths(1) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeletorMesDialog(
    mesAno: YearMonth,
    onConfirmar: (YearMonth) -> Unit,
    onDismiss: () -> Unit,
) {
    val anoAtual = YearMonth.now().year
    val anos = (anoAtual - 5..anoAtual + 2).toList()

    var mesSelecionado by remember { mutableIntStateOf(mesAno.monthValue) }
    var anoSelecionado by remember { mutableIntStateOf(mesAno.year) }

    var expandidoMes by remember { mutableStateOf(false) }
    var expandidoAno by remember { mutableStateOf(false) }

    val formatadorMes = remember {
        DateTimeFormatter.ofPattern("MMMM", Locale.getDefault())
    }

    fun nomeMes(mes: Int): String =
        YearMonth.of(anoSelecionado, mes)
            .atDay(1)
            .format(formatadorMes)
            .replaceFirstChar { it.uppercaseChar() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.seletor_mes_dialog_titulo),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)) {
                ExposedDropdownMenuBox(
                    expanded = expandidoMes,
                    onExpandedChange = { expandidoMes = it },
                ) {
                    OutlinedTextField(
                        value = nomeMes(mesSelecionado),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.seletor_mes_label_mes)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoMes) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                    )
                    ExposedDropdownMenu(
                        expanded = expandidoMes,
                        onDismissRequest = { expandidoMes = false },
                    ) {
                        (1..12).forEach { mes ->
                            DropdownMenuItem(
                                text = { Text(nomeMes(mes)) },
                                onClick = {
                                    mesSelecionado = mes
                                    expandidoMes = false
                                },
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = expandidoAno,
                    onExpandedChange = { expandidoAno = it },
                ) {
                    OutlinedTextField(
                        value = anoSelecionado.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.seletor_mes_label_ano)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoAno) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                    )
                    ExposedDropdownMenu(
                        expanded = expandidoAno,
                        onDismissRequest = { expandidoAno = false },
                    ) {
                        anos.forEach { ano ->
                            DropdownMenuItem(
                                text = { Text(ano.toString()) },
                                onClick = {
                                    anoSelecionado = ano
                                    expandidoAno = false
                                },
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmar(YearMonth.of(anoSelecionado, mesSelecionado))
            }) {
                Text(stringResource(R.string.acao_confirmar))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.acao_cancelar))
            }
        },
    )
}

@Preview(showBackground = true, name = "SeletorMesDialog · Light")
@Composable
private fun SeletorMesDialogPreview() {
    RumoTheme {
        SeletorMesDialog(
            mesAno = YearMonth.of(2026, 4),
            onConfirmar = {},
            onDismiss = {},
        )
    }
}






