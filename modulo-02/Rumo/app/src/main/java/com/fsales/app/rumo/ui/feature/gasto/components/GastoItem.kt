package com.fsales.app.rumo.ui.feature.gasto.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.R
import com.fsales.app.rumo.core.domain.model.CategoriaGasto
import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.ui.components.RumoInfoBadge
import com.fsales.app.rumo.ui.components.RumoItemCard
import com.fsales.app.rumo.ui.components.RumoValorTexto
import com.fsales.app.rumo.ui.mapper.formatarDataUI
import com.fsales.app.rumo.ui.mapper.toContentDescription
import com.fsales.app.rumo.ui.mapper.toIcon
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.iconSize
import com.fsales.app.rumo.ui.theme.spacing
import java.math.BigDecimal
import java.time.LocalDate

// =============================================================================
// GastoItem — card específico da feature Gasto, compõe RumoItemCard genérico.
// =============================================================================
@Composable
fun GastoItem(
    gasto: Gasto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RumoItemCard(
        onClick = onClick,
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = gasto.categoria.toIcon(),
                contentDescription = gasto.categoria.toContentDescription(),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(MaterialTheme.iconSize.medium),
            )
        },
        titulo = {
            Text(
                text = gasto.descricao,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        subtitulo = {
            RumoValorTexto(
                valor = gasto.valor,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = gasto.categoria.descricao,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "·",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = gasto.dataGasto.formatarDataUI(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        trailingContent = if (gasto.essencial || gasto.recorrente) {
            {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                ) {
                    if (gasto.essencial) {
                        RumoInfoBadge(
                            label = stringResource(R.string.gasto_essencial),
                            icone = Icons.Filled.Warning,
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        )
                    }
                    if (gasto.recorrente) {
                        RumoInfoBadge(
                            label = stringResource(R.string.gasto_recorrente),
                            icone = Icons.Filled.Autorenew,
                        )
                    }
                }
            }
        } else null,
    )
}

// =============================================================================
// Previews
// =============================================================================
private val gastoPreviewAluguel = Gasto(
    id = 1L,
    descricao = "Aluguel",
    valor = BigDecimal("1500.00"),
    dataGasto = LocalDate.of(2026, 4, 5),
    mesReferencia = 4,
    anoReferencia = 2026,
    categoria = CategoriaGasto.MORADIA,
    essencial = true,
    recorrente = true,
)

private val gastoPreviewRestaurante = Gasto(
    id = 2L,
    descricao = "Restaurante",
    valor = BigDecimal("85.50"),
    dataGasto = LocalDate.of(2026, 4, 10),
    mesReferencia = 4,
    anoReferencia = 2026,
    categoria = CategoriaGasto.ALIMENTACAO,
    essencial = false,
    recorrente = false,
)

@Preview(showBackground = true, name = "GastoItem · Essencial · Light")
@Preview(showBackground = true, name = "GastoItem · Essencial · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GastoItemEssencialPreview() {
    RumoTheme {
        GastoItem(gasto = gastoPreviewAluguel, onClick = {})
    }
}

@Preview(showBackground = true, name = "GastoItem · Simples · Light")
@Preview(showBackground = true, name = "GastoItem · Simples · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GastoItemSimplesPreview() {
    RumoTheme {
        GastoItem(gasto = gastoPreviewRestaurante, onClick = {})
    }
}

