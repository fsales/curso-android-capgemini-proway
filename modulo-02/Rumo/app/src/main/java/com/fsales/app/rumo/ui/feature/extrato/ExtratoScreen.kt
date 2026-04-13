package com.fsales.app.rumo.ui.feature.extrato

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fsales.app.rumo.R
import com.fsales.app.rumo.core.domain.model.CategoriaGasto
import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.ItemExtrato
import com.fsales.app.rumo.core.domain.model.TipoGanho
import com.fsales.app.rumo.ui.components.RumoEmptyState
import com.fsales.app.rumo.ui.components.RumoErroState
import com.fsales.app.rumo.ui.components.RumoListaScaffold
import com.fsales.app.rumo.ui.components.SeletorMes
import com.fsales.app.rumo.ui.feature.extrato.components.ExtratoItemCard
import com.fsales.app.rumo.ui.feature.extrato.components.ResumoExtratoCard
import com.fsales.app.rumo.ui.mapper.formatarDataUI
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.spacing
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

// =============================================================================
// Screen — consome ViewModel e delega para ExtratoContent
// =============================================================================
@Composable
fun ExtratoScreen(
    modifier: Modifier = Modifier,
    viewModel: ExtratoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ExtratoContent(
        uiState = uiState,
        modifier = modifier,
        onEvent = viewModel::onEvent,
    )
}

// =============================================================================
// Content — renderiza estado puro; sem ViewModel
// =============================================================================
@Composable
fun ExtratoContent(
    uiState: ExtratoUiState,
    modifier: Modifier = Modifier,
    onEvent: (ExtratoEvent) -> Unit = {},
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val mostrarScrollToTop by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 0 }
    }

    RumoListaScaffold(
        modifier = modifier,
        floatingActionButton = {
            AnimatedVisibility(
                visible = mostrarScrollToTop,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.extrato_scroll_topo),
                    )
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.medium),
        ) {
            Spacer(Modifier.height(MaterialTheme.spacing.small))

            SeletorMes(
                mesAno = uiState.mesAno,
                onAnterior = { onEvent(ExtratoEvent.MesAnterior) },
                onProximo = { onEvent(ExtratoEvent.ProximoMes) },
            )

            Spacer(Modifier.height(MaterialTheme.spacing.small))

            ResumoExtratoCard(
                totalGanhos  = uiState.totalGanhos,
                totalGastos  = uiState.totalGastos,
                saldoPeriodo = uiState.saldoPeriodo,
            )

            Spacer(Modifier.height(MaterialTheme.spacing.small))

            when {
                uiState.erro != null -> {
                    RumoErroState(
                        mensagem = uiState.erro,
                        onRetry = { onEvent(ExtratoEvent.TentarNovamente) },
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                !uiState.carregando && uiState.itensPorData.isEmpty() -> {
                    RumoEmptyState(
                        icone = Icons.AutoMirrored.Filled.ReceiptLong,
                        tituloRes = R.string.extrato_lista_vazia_titulo,
                        mensagemRes = R.string.extrato_lista_vazia_mensagem,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                else -> {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = MaterialTheme.spacing.extraLarge),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    ) {
                        uiState.itensPorData.forEach { (data, itens) ->
                            stickyHeader(key = "header_$data") {
                                ExtratoDataHeader(data = data)
                            }
                            items(
                                count = itens.size,
                                key = { index ->
                                    val item = itens[index]
                                    if (item is ItemExtrato.GanhoItem) "g_${item.id}" else "e_${item.id}"
                                },
                            ) { index ->
                                ExtratoItemCard(item = itens[index])
                            }
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// Separador de data — sticky header da LazyColumn
// =============================================================================
@Composable
private fun ExtratoDataHeader(data: LocalDate) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Text(
            text = data.formatarDataUI(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.extraSmall,
                vertical = MaterialTheme.spacing.extraSmall,
            ),
        )
    }
}

// =============================================================================
// Preview
// =============================================================================
private val ganhoFakeExtrato = Ganho(
    id = 1L, descricao = "Salário", valor = BigDecimal("5000.00"),
    dataRecebimento = LocalDate.of(2026, 4, 5),
    mesReferencia = 4, anoReferencia = 2026, tipo = TipoGanho.SALARIO, recorrente = true,
)
private val gastoFakeExtrato = Gasto(
    id = 1L, descricao = "Supermercado", valor = BigDecimal("450.00"),
    dataGasto = LocalDate.of(2026, 4, 5),
    mesReferencia = 4, anoReferencia = 2026, categoria = CategoriaGasto.ALIMENTACAO, essencial = true,
)
private val itensFakeExtrato = mapOf(
    LocalDate.of(2026, 4, 5) to listOf(
        ItemExtrato.GanhoItem(ganhoFakeExtrato),
        ItemExtrato.GastoItem(gastoFakeExtrato),
    )
)
private val uiStatePreenchida = ExtratoUiState(
    itensPorData = itensFakeExtrato,
    totalGanhos  = BigDecimal("5000.00"),
    totalGastos  = BigDecimal("450.00"),
    saldoPeriodo = BigDecimal("4550.00"),
    mesAno       = YearMonth.of(2026, 4),
)

@Preview(showBackground = true, name = "Extrato · Preenchida · Light")
@Preview(showBackground = true, name = "Extrato · Preenchida · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExtratoPreenchidaPreview() {
    RumoTheme {
        ExtratoContent(uiState = uiStatePreenchida)
    }
}

@Preview(showBackground = true, name = "Extrato · Vazia · Light")
@Preview(showBackground = true, name = "Extrato · Vazia · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExtratoVaziaPreview() {
    RumoTheme {
        ExtratoContent(uiState = ExtratoUiState(mesAno = YearMonth.of(2026, 4)))
    }
}

@Preview(showBackground = true, name = "Extrato · Erro · Light")
@Preview(showBackground = true, name = "Extrato · Erro · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExtratoErroPreview() {
    RumoTheme {
        ExtratoContent(
            uiState = ExtratoUiState(
                erro = "Não foi possível carregar o extrato.",
                mesAno = YearMonth.of(2026, 4),
            )
        )
    }
}
