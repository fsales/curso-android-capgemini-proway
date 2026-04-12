package com.fsales.app.rumo.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.R
import com.fsales.app.rumo.ui.theme.RumoTheme
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

// =============================================================================
// Seletor de mês/ano — reutilizado em Ganho, Gasto e Sonho
// =============================================================================

private val formatadorMesAno: DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag("pt-BR"))

fun YearMonth.formatarMesAno(): String =
    this.format(formatadorMesAno)
        .replaceFirstChar { it.uppercaseChar() }

@Composable
fun SeletorMes(
    mesAno: YearMonth,
    onAnterior: () -> Unit,
    onProximo: () -> Unit,
    modifier: Modifier = Modifier,
) {
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

        Text(
            text = mesAno.formatarMesAno(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        IconButton(onClick = onProximo) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.seletor_mes_proximo),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
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






