package com.fsales.app.rumo.ui.feature.sonho.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fsales.app.rumo.R
import com.fsales.app.rumo.core.domain.model.PrioridadeSonho
import com.fsales.app.rumo.core.domain.model.ProjecaoSonho
import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.core.domain.model.StatusSonho
import com.fsales.app.rumo.ui.components.RumoInfoBadge
import com.fsales.app.rumo.ui.components.formatarBRL
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.iconSize
import com.fsales.app.rumo.ui.theme.spacing
import java.math.BigDecimal

// =============================================================================
// SonhoItem — card com dados de projeção financeira do sonho.
// =============================================================================
@Composable
fun SonhoItem(
    projecao: ProjecaoSonho,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sonho     = projecao.sonho
    val progresso = projecao.percentualConcluido
        .divide(BigDecimal(100))
        .toFloat()
        .coerceIn(0f, 1f)
    val concluido = projecao.valorRestante <= BigDecimal.ZERO

    val corProgresso = when {
        concluido -> MaterialTheme.colorScheme.tertiary
        else      -> MaterialTheme.colorScheme.primary
    }

    ElevatedCard(
        onClick  = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            // ------------------------------------------------------------------
            // Linha 1 — Título + badge de status
            // ------------------------------------------------------------------
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text       = sonho.titulo,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onSurface,
                    modifier   = Modifier.weight(1f),
                )
                Spacer(Modifier.width(MaterialTheme.spacing.small))
                RumoInfoBadge(label = sonho.status.descricao)
            }

            // ------------------------------------------------------------------
            // Linha 2 — Valores: atual / meta  +  percentual
            // ------------------------------------------------------------------
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text  = sonho.valorAtual.formatarBRL(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text  = "  /  ${sonho.valorMeta.formatarBRL()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text  = "${projecao.percentualConcluido.toPlainString()}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = corProgresso,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            // ------------------------------------------------------------------
            // Linha 3 — Barra de progresso
            // ------------------------------------------------------------------
            LinearProgressIndicator(
                progress = { progresso },
                color    = corProgresso,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.small),
            )

            // ------------------------------------------------------------------
            // Linha 4 — Prioridade + meses + indicador de prazo
            // ------------------------------------------------------------------
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Prioridade (lado esquerdo)
                RumoInfoBadge(
                    label          = sonho.prioridade.descricao,
                    icone          = Icons.Filled.Star,
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

                // Meses + prazo (lado direito)
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                ) {
                    // Meses necessários
                    val meses = projecao.mesesNecessarios
                    Text(
                        text  = if (meses != null)
                            pluralStringResource(R.plurals.sonho_meses_necessarios, meses, meses)
                        else
                            stringResource(R.string.sonho_sem_projecao),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    // Indicador de prazo (só se ainda há valor restante)
                    if (!concluido) {
                        val noPrazo = projecao.seraAlcancadoNoPrazo
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                        ) {
                            Icon(
                                imageVector        = if (noPrazo) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                                contentDescription = null,
                                tint               = if (noPrazo) MaterialTheme.colorScheme.primary
                                                     else MaterialTheme.colorScheme.error,
                                modifier           = Modifier.size(MaterialTheme.iconSize.extraSmall),
                            )
                            Text(
                                text  = stringResource(
                                    if (noPrazo) R.string.sonho_no_prazo else R.string.sonho_fora_do_prazo
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (noPrazo) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// Previews
// =============================================================================
private val sonhoPreviewCarro = Sonho(
    id         = 1L,
    titulo     = "Carro novo",
    descricao  = "Toyota Corolla 2027",
    valorMeta  = BigDecimal("80000.00"),
    valorAtual = BigDecimal("25000.00"),
    prioridade = PrioridadeSonho.ALTA,
    status     = StatusSonho.EM_ANDAMENTO,
)

private val sonhoPreviewViagem = Sonho(
    id         = 2L,
    titulo     = "Viagem para o Japão",
    valorMeta  = BigDecimal("15000.00"),
    valorAtual = BigDecimal.ZERO,
    prioridade = PrioridadeSonho.MEDIA,
    status     = StatusSonho.NAO_INICIADO,
)

private val sonhoPreviewApartamento = Sonho(
    id         = 3L,
    titulo     = "Apartamento próprio",
    valorMeta  = BigDecimal("300000.00"),
    valorAtual = BigDecimal("300000.00"),
    prioridade = PrioridadeSonho.ALTA,
    status     = StatusSonho.CONCLUIDO,
)

private val projecaoCarro = ProjecaoSonho(
    sonho                = sonhoPreviewCarro,
    valorRestante        = BigDecimal("55000.00"),
    percentualConcluido  = BigDecimal("31.25"),
    mesesNecessarios     = 18,
    seraAlcancadoNoPrazo = true,
)

private val projecaoViagem = ProjecaoSonho(
    sonho                = sonhoPreviewViagem,
    valorRestante        = BigDecimal("15000.00"),
    percentualConcluido  = BigDecimal("0.00"),
    mesesNecessarios     = null,
    seraAlcancadoNoPrazo = false,
)

private val projecaoApartamento = ProjecaoSonho(
    sonho                = sonhoPreviewApartamento,
    valorRestante        = BigDecimal.ZERO,
    percentualConcluido  = BigDecimal("100.00"),
    mesesNecessarios     = 0,
    seraAlcancadoNoPrazo = true,
)

@Preview(showBackground = true, name = "SonhoItem · Em andamento · Light")
@Preview(showBackground = true, name = "SonhoItem · Em andamento · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SonhoItemEmAndamentoPreview() {
    RumoTheme { SonhoItem(projecao = projecaoCarro, onClick = {}) }
}

@Preview(showBackground = true, name = "SonhoItem · Não iniciado · Light")
@Preview(showBackground = true, name = "SonhoItem · Não iniciado · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SonhoItemNaoIniciadoPreview() {
    RumoTheme { SonhoItem(projecao = projecaoViagem, onClick = {}) }
}

@Preview(showBackground = true, name = "SonhoItem · Concluído · Light")
@Preview(showBackground = true, name = "SonhoItem · Concluído · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SonhoItemConcluidoPreview() {
    RumoTheme { SonhoItem(projecao = projecaoApartamento, onClick = {}) }
}
