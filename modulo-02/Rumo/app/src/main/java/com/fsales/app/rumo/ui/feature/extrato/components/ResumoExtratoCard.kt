package com.fsales.app.rumo.ui.feature.extrato.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.R
import com.fsales.app.rumo.ui.components.formatarBRL
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.spacing
import java.math.BigDecimal

// =============================================================================
// ResumoExtratoCard — exibe totais do período (entradas, saídas, saldo).
// Segue o visual do SaldoScreen: surfaceVariant container, 3 colunas.
// =============================================================================
@Composable
fun ResumoExtratoCard(
    totalGanhos: BigDecimal,
    totalGastos: BigDecimal,
    saldoPeriodo: BigDecimal,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
        ) {
            Text(
                text = stringResource(R.string.extrato_resumo_saldo),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            Text(
                text = saldoPeriodo.formatarBRL(),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = if (saldoPeriodo >= BigDecimal.ZERO)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ResumoColuna(
                    label = stringResource(R.string.extrato_resumo_entradas),
                    valor = totalGanhos,
                    valorColor = MaterialTheme.colorScheme.primary,
                    prefixo = "+",
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start,
                )
                ResumoColuna(
                    label = stringResource(R.string.extrato_resumo_saidas),
                    valor = totalGastos,
                    valorColor = MaterialTheme.colorScheme.error,
                    prefixo = "−",
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                )
            }
        }
    }
}

@Composable
private fun ResumoColuna(
    label: String,
    valor: BigDecimal,
    valorColor: Color,
    prefixo: String,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
        Text(
            text = "$prefixo ${valor.formatarBRL()}",
            style = style,
            color = valorColor,
        )
    }
}

// =============================================================================
// Preview
// =============================================================================
@Preview(showBackground = true, name = "ResumoExtratoCard · Saldo positivo · Light")
@Preview(showBackground = true, name = "ResumoExtratoCard · Saldo positivo · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ResumoExtratoCardPositivoPreview() {
    RumoTheme {
        ResumoExtratoCard(
            totalGanhos  = BigDecimal("6200.00"),
            totalGastos  = BigDecimal("3400.00"),
            saldoPeriodo = BigDecimal("2800.00"),
        )
    }
}

@Preview(showBackground = true, name = "ResumoExtratoCard · Saldo negativo · Light")
@Preview(showBackground = true, name = "ResumoExtratoCard · Saldo negativo · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ResumoExtratoCardNegativoPreview() {
    RumoTheme {
        ResumoExtratoCard(
            totalGanhos  = BigDecimal("3000.00"),
            totalGastos  = BigDecimal("4500.00"),
            saldoPeriodo = BigDecimal("-1500.00"),
        )
    }
}
