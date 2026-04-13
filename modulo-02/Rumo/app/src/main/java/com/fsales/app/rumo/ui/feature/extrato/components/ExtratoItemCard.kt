package com.fsales.app.rumo.ui.feature.extrato.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.ItemExtrato
import com.fsales.app.rumo.core.domain.model.TipoGanho
import com.fsales.app.rumo.ui.components.RumoInfoBadge
import com.fsales.app.rumo.ui.components.RumoItemCard
import com.fsales.app.rumo.ui.components.formatarBRL
import com.fsales.app.rumo.ui.mapper.formatarDataUI
import com.fsales.app.rumo.ui.mapper.toContentDescription
import com.fsales.app.rumo.ui.mapper.toIcon
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.iconSize
import com.fsales.app.rumo.ui.theme.spacing
import java.math.BigDecimal
import java.time.LocalDate

// =============================================================================
// ExtratoItemCard — card do extrato unificado.
// Semântica de cor: ganho = primary (verde-azul), gasto = error (vermelho).
// Reutiliza RumoItemCard e mappers de ícone já existentes.
// =============================================================================
@Composable
fun ExtratoItemCard(
    item: ItemExtrato,
    modifier: Modifier = Modifier,
) {
    when (item) {
        is ItemExtrato.GanhoItem -> GanhoExtratoCard(item.ganho, modifier)
        is ItemExtrato.GastoItem -> GastoExtratoCard(item.gasto, modifier)
    }
}

@Composable
private fun GanhoExtratoCard(ganho: Ganho, modifier: Modifier = Modifier) {
    val corGanho = MaterialTheme.colorScheme.primary
    RumoItemCard(
        onClick = {},
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = ganho.tipo.toIcon(),
                contentDescription = ganho.tipo.toContentDescription(),
                tint = corGanho,
                modifier = Modifier.size(MaterialTheme.iconSize.medium),
            )
        },
        titulo = {
            Text(
                text = ganho.descricao,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        subtitulo = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = ganho.tipo.descricao,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "·",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = ganho.dataRecebimento.formatarDataUI(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        trailingContent = {
            androidx.compose.foundation.layout.Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            ) {
                Text(
                    text = "+ ${ganho.valor.formatarBRL()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = corGanho,
                )
                if (ganho.recorrente) {
                    RumoInfoBadge(
                        label = stringResource(R.string.ganho_recorrente),
                        icone = Icons.Filled.Autorenew,
                    )
                }
            }
        },
    )
}

@Composable
private fun GastoExtratoCard(gasto: Gasto, modifier: Modifier = Modifier) {
    val corGasto = MaterialTheme.colorScheme.error
    RumoItemCard(
        onClick = {},
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = gasto.categoria.toIcon(),
                contentDescription = gasto.categoria.toContentDescription(),
                tint = corGasto,
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
        trailingContent = {
            androidx.compose.foundation.layout.Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            ) {
                Text(
                    text = "− ${gasto.valor.formatarBRL()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = corGasto,
                )
                if (gasto.essencial) {
                    RumoInfoBadge(
                        label = stringResource(R.string.gasto_essencial),
                        icone = Icons.Filled.Warning,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }
            }
        },
    )
}

// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "ExtratoItemCard · Ganho · Light")
@Preview(showBackground = true, name = "ExtratoItemCard · Ganho · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExtratoItemCardGanhoPreview() {
    RumoTheme {
        ExtratoItemCard(
            item = ItemExtrato.GanhoItem(
                Ganho(
                    id = 1L,
                    descricao = "Salário",
                    valor = BigDecimal("5000.00"),
                    dataRecebimento = LocalDate.of(2026, 4, 5),
                    mesReferencia = 4,
                    anoReferencia = 2026,
                    tipo = TipoGanho.SALARIO,
                    recorrente = true,
                )
            )
        )
    }
}

@Preview(showBackground = true, name = "ExtratoItemCard · Gasto · Light")
@Preview(showBackground = true, name = "ExtratoItemCard · Gasto · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExtratoItemCardGastoPreview() {
    RumoTheme {
        ExtratoItemCard(
            item = ItemExtrato.GastoItem(
                Gasto(
                    id = 1L,
                    descricao = "Supermercado",
                    valor = BigDecimal("450.00"),
                    dataGasto = LocalDate.of(2026, 4, 8),
                    mesReferencia = 4,
                    anoReferencia = 2026,
                    categoria = CategoriaGasto.ALIMENTACAO,
                    essencial = true,
                )
            )
        )
    }
}
