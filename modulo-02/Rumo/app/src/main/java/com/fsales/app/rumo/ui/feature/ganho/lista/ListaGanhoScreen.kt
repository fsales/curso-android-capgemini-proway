package com.fsales.app.rumo.ui.feature.ganho.lista

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fsales.app.rumo.R
import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.TipoGanho
import com.fsales.app.rumo.ui.ListaGanhoUiEvent
import com.fsales.app.rumo.ui.components.RumoEmptyState
import com.fsales.app.rumo.ui.components.RumoErroState
import com.fsales.app.rumo.ui.components.RumoListaScaffold
import com.fsales.app.rumo.ui.components.SeletorMes
import com.fsales.app.rumo.ui.feature.ganho.components.GanhoItem
import com.fsales.app.rumo.ui.feature.home.HomeEvent
import com.fsales.app.rumo.ui.feature.home.HomeScreenPreviewShell
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.spacing
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

// =============================================================================
// Screen — ponto de entrada da feature, consome ViewModel e roteia UiEvent
// =============================================================================
@Composable
fun ListaGanhoScreen(
    modifier: Modifier = Modifier,
    viewModel: ListaGanhoViewModel = hiltViewModel(),
    onNavigateToCadastro: () -> Unit = {},
    onCarregandoChange: (Boolean) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        // Coleta uiEvent (navegação) e observa carregando em paralelo no mesmo escopo
        launch {
            snapshotFlow { uiState.carregando }
                .collect { onCarregandoChange(it) }
        }
        viewModel.uiEvent.collect { event ->
            when (event) {
                ListaGanhoUiEvent.NavigateToCadastro -> onNavigateToCadastro()
            }
        }
    }

    ListaGanhoContent(
        uiState = uiState,
        modifier = modifier,
        onEvent = viewModel::onEvent,
    )
}

// =============================================================================
// Content — renderiza estado puro; sem ViewModel
// =============================================================================
@Composable
fun ListaGanhoContent(
    uiState: ListaGanhoUiState,
    modifier: Modifier = Modifier,
    onEvent: (ListaGanhoEvent) -> Unit,
) {
    RumoListaScaffold(
        modifier = modifier,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onEvent(ListaGanhoEvent.IrParaCadastro) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                    )
                },
                text = { Text(text = stringResource(R.string.lista_ganho_adicionar)) },
            )
        },
    ) { paddingValues ->
        when {
            uiState.erro != null -> {
                RumoErroState(
                    mensagem = uiState.erro,
                    onRetry = { onEvent(ListaGanhoEvent.TentarNovamente) },
                    modifier = Modifier.padding(paddingValues),
                )
            }

            uiState.ganhos.isEmpty() && !uiState.carregando -> {
                RumoEmptyState(
                    icone = Icons.Filled.SearchOff,
                    tituloRes = R.string.lista_ganho_vazia_titulo,
                    mensagemRes = R.string.lista_ganho_vazia_mensagem,
                    modifier = Modifier.padding(paddingValues),
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    contentPadding = PaddingValues(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.medium,
                    ),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                ) {
                    item {
                        SeletorMes(
                            mesAno = uiState.mesAno,
                            onAnterior = { onEvent(ListaGanhoEvent.MesAnterior) },
                            onProximo = { onEvent(ListaGanhoEvent.ProximoMes) },
                        )
                    }
                    items(uiState.ganhos, key = { it.id }) { ganho ->
                        GanhoItem(ganho = ganho, onClick = {})
                    }
                }
            }
        }
    }
}

// =============================================================================
// Previews
// =============================================================================
private val ganhosFake = listOf(
    Ganho(
        id = 1L,
        descricao = "Salário",
        valor = BigDecimal("5000.00"),
        dataRecebimento = LocalDate.of(2026, 4, 5),
        mesReferencia = 4,
        anoReferencia = 2026,
        tipo = TipoGanho.SALARIO,
        recorrente = true,
    ),
    Ganho(
        id = 2L,
        descricao = "Freelance UI Design",
        valor = BigDecimal("1200.00"),
        dataRecebimento = LocalDate.of(2026, 4, 10),
        mesReferencia = 4,
        anoReferencia = 2026,
        tipo = TipoGanho.RENDA_EXTRA,
        recorrente = false,
    ),
    Ganho(
        id = 3L,
        descricao = "Dividendos",
        valor = BigDecimal("320.50"),
        dataRecebimento = LocalDate.of(2026, 4, 12),
        mesReferencia = 4,
        anoReferencia = 2026,
        tipo = TipoGanho.INVESTIMENTO,
        recorrente = false,
    ),
)

@Preview(showBackground = true, name = "GanhoItem · Light")
@Preview(showBackground = true, name = "GanhoItem · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GanhoItemPreview() {
    RumoTheme {
        GanhoItem(ganho = ganhosFake.first(), onClick = {})
    }
}

@Preview(showBackground = true, name = "ListaGanho · Preenchida · Light")
@Preview(showBackground = true, name = "ListaGanho · Preenchida · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListaGanhoPreenchidaPreview() {
    RumoTheme {
        ListaGanhoContent(
            uiState = ListaGanhoUiState(ganhos = ganhosFake, mesAno = YearMonth.of(2026, 4)),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "ListaGanho · Vazia · Light")
@Preview(showBackground = true, name = "ListaGanho · Vazia · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListaGanhoVaziaPreview() {
    RumoTheme {
        ListaGanhoContent(
            uiState = ListaGanhoUiState(ganhos = emptyList(), mesAno = YearMonth.of(2026, 4)),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "ListaGanho · Erro · Light")
@Preview(showBackground = true, name = "ListaGanho · Erro · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListaGanhoErroPreview() {
    RumoTheme {
        ListaGanhoContent(
            uiState = ListaGanhoUiState(erro = "Não foi possível carregar os ganhos."),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "ListaGanho · Shell · Light")
@Preview(showBackground = true, name = "ListaGanho · Shell · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListaGanhoPreview() {
    RumoTheme {
        HomeScreenPreviewShell(initialTab = HomeEvent.IrParaGanhos)
    }
}

