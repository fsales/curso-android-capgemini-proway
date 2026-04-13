package com.fsales.app.rumo.ui.feature.sonho.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.core.domain.model.PrioridadeSonho
import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.core.domain.model.StatusSonho
import com.fsales.app.rumo.ui.components.RumoInfoBadge
import com.fsales.app.rumo.ui.components.RumoItemCard
import com.fsales.app.rumo.ui.components.RumoValorTexto
import com.fsales.app.rumo.ui.components.formatarBRL
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.spacing
import java.math.BigDecimal
import java.math.RoundingMode

// =============================================================================
// SonhoItem — card específico da feature Sonho, compõe RumoItemCard genérico.
// Exibe progresso do valor atual em relação ao valor meta.
// =============================================================================
@Composable
fun SonhoItem(
    sonho: Sonho,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val progresso = if (sonho.valorMeta > BigDecimal.ZERO) {
        (sonho.valorAtual.divide(sonho.valorMeta, 4, RoundingMode.HALF_UP)).toFloat()
            .coerceIn(0f, 1f)
    } else 0f

    RumoItemCard(
        onClick = onClick,
        modifier = modifier,
        leadingIcon = {
            RumoInfoBadge(
                label = sonho.prioridade.descricao,
                icone = Icons.Filled.Star,
                containerColor = when (sonho.prioridade) {
                    PrioridadeSonho.ALTA  -> MaterialTheme.colorScheme.errorContainer
                    PrioridadeSonho.MEDIA -> MaterialTheme.colorScheme.tertiaryContainer
                    PrioridadeSonho.BAIXA -> MaterialTheme.colorScheme.secondaryContainer
                },
                contentColor = when (sonho.prioridade) {
                    PrioridadeSonho.ALTA  -> MaterialTheme.colorScheme.onErrorContainer
                    PrioridadeSonho.MEDIA -> MaterialTheme.colorScheme.onTertiaryContainer
                    PrioridadeSonho.BAIXA -> MaterialTheme.colorScheme.onSecondaryContainer
                },
            )
        },
        titulo = {
            Text(
                text = sonho.titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        subtitulo = {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)) {
                RumoValorTexto(
                    valor = sonho.valorMeta,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "${sonho.valorAtual.formatarBRL()} / ${sonho.valorMeta.formatarBRL()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                LinearProgressIndicator(
                    progress = { progresso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.extraSmall),
                )
            }
        },
        trailingContent = {
            RumoInfoBadge(label = sonho.status.descricao)
        },
    )
}

// =============================================================================
// Previews
// =============================================================================
private val sonhoPreviewCarro = Sonho(
    id = 1L,
    titulo = "Carro novo",
    descricao = "Toyota Corolla 2027",
    valorMeta = BigDecimal("80000.00"),
    valorAtual = BigDecimal("25000.00"),
    prioridade = PrioridadeSonho.ALTA,
    status = StatusSonho.EM_ANDAMENTO,
)

private val sonhoPreviewViagem = Sonho(
    id = 2L,
    titulo = "Viagem para o Japão",
    valorMeta = BigDecimal("15000.00"),
    valorAtual = BigDecimal.ZERO,
    prioridade = PrioridadeSonho.MEDIA,
    status = StatusSonho.NAO_INICIADO,
)

@Preview(showBackground = true, name = "SonhoItem · Em andamento · Light")
@Preview(showBackground = true, name = "SonhoItem · Em andamento · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SonhoItemEmAndamentoPreview() {
    RumoTheme {
        SonhoItem(sonho = sonhoPreviewCarro, onClick = {})
    }
}

@Preview(showBackground = true, name = "SonhoItem · Não iniciado · Light")
@Preview(showBackground = true, name = "SonhoItem · Não iniciado · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SonhoItemNaoIniciadoPreview() {
    RumoTheme {
        SonhoItem(sonho = sonhoPreviewViagem, onClick = {})
    }
}

