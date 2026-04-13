package com.fsales.app.rumo.ui.feature.saldo

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.fsales.app.rumo.R
import com.fsales.app.rumo.ui.components.SeletorMes
import com.fsales.app.rumo.ui.theme.RumoTheme
import java.text.NumberFormat
import java.time.YearMonth
import java.util.Locale

@Composable
fun SaldoScreen(
    viewModel: SaldoViewModel = hiltViewModel(),
    onIrExtrato: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    SaldoContent(
        uiState = state,
        onMesAnterior = viewModel::onMesAnterior,
        onMesProximo = viewModel::onMesProximo,
        onIrExtrato = onIrExtrato,
    )
}

@Composable
fun SaldoContent(
    uiState: SaldoUiState,
    modifier: Modifier = Modifier,
    onMesAnterior: () -> Unit = {},
    onMesProximo: () -> Unit = {},
    onIrExtrato: () -> Unit = {},
) {
    Column(modifier = modifier.padding(16.dp)) {
        SeletorMes(
            mesAno = uiState.mesAno,
            onAnterior = onMesAnterior,
            onProximo = onMesProximo,
        )

        Spacer(modifier = Modifier.height(12.dp))

        val locale = Locale.getDefault()
        val fmt = NumberFormat.getCurrencyInstance(locale)

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(R.string.saldo_label), style = MaterialTheme.typography.titleSmall)

                    if (uiState.saldo != null && uiState.saldo < java.math.BigDecimal.ZERO) {
                        BadgedBox(badge = { Badge { Text(stringResource(R.string.saldo_negativo_badge)) } }) {
                            Icon(painter = painterResource(id = R.drawable.ic_rumo_saldo), contentDescription = null)
                        }
                    } else {
                        Icon(painter = painterResource(id = R.drawable.ic_rumo_saldo), contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = uiState.saldo?.let { fmt.format(it) } ?: stringResource(R.string.saldo_carregando),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (uiState.saldo != null && uiState.saldo < java.math.BigDecimal.ZERO) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(text = stringResource(R.string.saldo_ganhos_label), style = MaterialTheme.typography.bodySmall)
                        Text(text = uiState.totalGanhos?.let { fmt.format(it) } ?: "—", style = MaterialTheme.typography.bodyLarge)
                    }
                    Column {
                        Text(text = stringResource(R.string.saldo_gastos_label), style = MaterialTheme.typography.bodySmall)
                        Text(text = uiState.totalGastos?.let { fmt.format(it) } ?: "—", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Saldo · Light")
@Preview(showBackground = true, name = "Saldo · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SaldoPreview() {
    RumoTheme {
        Surface {
            SaldoContent(
                uiState = SaldoUiState(
                    mesAno = YearMonth.of(2026, 4),
                    totalGanhos = java.math.BigDecimal("6200.00"),
                    totalGastos = java.math.BigDecimal("4200.00"),
                    saldo = java.math.BigDecimal("2000.00"),
                ),
            )
        }
    }
}


