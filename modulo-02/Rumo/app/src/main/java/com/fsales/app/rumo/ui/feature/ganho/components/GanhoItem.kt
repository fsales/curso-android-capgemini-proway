package com.fsales.app.rumo.ui.feature.ganho.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.R
import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.TipoGanho
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
// GanhoItem — card específico da feature Ganho, reutilizável em qualquer
// tela da feature (lista, detalhe, seleção, etc.).
// Compõe RumoItemCard genérico com os dados do domínio Ganho.
// =============================================================================
@Composable
fun GanhoItem(
    ganho: Ganho,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RumoItemCard(
        onClick = onClick,
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = ganho.tipo.toIcon(),
                contentDescription = ganho.tipo.toContentDescription(),
                tint = MaterialTheme.colorScheme.primary,
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
            RumoValorTexto(
                valor = ganho.valor,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
            )
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
        trailingContent = if (ganho.recorrente) {
            {
                RumoInfoBadge(
                    label = stringResource(R.string.ganho_recorrente),
                    icone = Icons.Filled.Autorenew,
                )
            }
        } else null,
    )
}

// =============================================================================
// Previews
// =============================================================================
private val ganhoPreviewSalario = Ganho(
    id = 1L,
    descricao = "Salário",
    valor = BigDecimal("5000.00"),
    dataRecebimento = LocalDate.of(2026, 4, 5),
    mesReferencia = 4,
    anoReferencia = 2026,
    tipo = TipoGanho.SALARIO,
    recorrente = true,
)

private val ganhoPreviewFreelance = Ganho(
    id = 2L,
    descricao = "Freelance UI Design",
    valor = BigDecimal("1200.00"),
    dataRecebimento = LocalDate.of(2026, 4, 10),
    mesReferencia = 4,
    anoReferencia = 2026,
    tipo = TipoGanho.RENDA_EXTRA,
    recorrente = false,
)

@Preview(showBackground = true, name = "GanhoItem · Recorrente · Light")
@Preview(showBackground = true, name = "GanhoItem · Recorrente · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GanhoItemRecorrentePreview() {
    RumoTheme {
        GanhoItem(ganho = ganhoPreviewSalario, onClick = {})
    }
}

@Preview(showBackground = true, name = "GanhoItem · Simples · Light")
@Preview(showBackground = true, name = "GanhoItem · Simples · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GanhoItemSimplesPreview() {
    RumoTheme {
        GanhoItem(ganho = ganhoPreviewFreelance, onClick = {})
    }
}



