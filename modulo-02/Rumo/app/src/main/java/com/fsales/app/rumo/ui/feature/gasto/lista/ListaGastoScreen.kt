package com.fsales.app.rumo.ui.feature.gasto.lista

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.fsales.app.rumo.core.domain.model.CategoriaGasto
import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.ui.ListaGastoUiEvent
import com.fsales.app.rumo.ui.components.RumoEmptyState
import com.fsales.app.rumo.ui.components.RumoErroState
import com.fsales.app.rumo.ui.components.RumoListaScaffold
import com.fsales.app.rumo.ui.components.SeletorMes
import com.fsales.app.rumo.ui.feature.gasto.components.GastoItem
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
fun ListaGastoScreen(
    modifier: Modifier = Modifier,
    viewModel: ListaGastoViewModel = hiltViewModel(),
    onNavigateToCadastro: () -> Unit = {},
    onCarregandoChange: (Boolean) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        launch {
            snapshotFlow { uiState.carregando }
                .collect { onCarregandoChange(it) }
        }
        viewModel.uiEvent.collect { event ->
            when (event) {
                ListaGastoUiEvent.NavigateToCadastro -> onNavigateToCadastro()
            }
        }
    }

    ListaGastoContent(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}

// =============================================================================
// Content — renderiza estado puro; sem ViewModel
// =============================================================================
@Composable
fun ListaGastoContent(
    modifier: Modifier = Modifier,
    uiState: ListaGastoUiState = ListaGastoUiState(),
    onEvent: (ListaGastoEvent) -> Unit = {},
) {
    RumoListaScaffold(
        modifier = modifier,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onEvent(ListaGastoEvent.IrParaCadastro) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                    )
                },
                text = { Text(text = stringResource(R.string.lista_gasto_adicionar)) },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            SeletorMes(
                mesAno = uiState.mesAno,
                onAnterior = { onEvent(ListaGastoEvent.MesAnterior) },
                onProximo = { onEvent(ListaGastoEvent.ProximoMes) },
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
            )

            when {
                uiState.erro != null -> {
                    RumoErroState(
                        mensagem = uiState.erro,
                        onRetry = { onEvent(ListaGastoEvent.TentarNovamente) },
                    )
                }

                uiState.gastos.isEmpty() && !uiState.carregando -> {
                    RumoEmptyState(
                        icone = Icons.Filled.SearchOff,
                        tituloRes = R.string.lista_gasto_vazia_titulo,
                        mensagemRes = R.string.lista_gasto_vazia_mensagem,
                    )
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.medium,
                        ),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(uiState.gastos, key = { it.id }) { gasto ->
                            GastoItem(gasto = gasto, onClick = {})
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
private val gastosFake = listOf(
    Gasto(
        id = 1L,
        descricao = "Aluguel",
        valor = BigDecimal("1500.00"),
        dataGasto = LocalDate.of(2026, 4, 5),
        mesReferencia = 4,
        anoReferencia = 2026,
        categoria = CategoriaGasto.MORADIA,
        essencial = true,
        recorrente = true,
    ),
    Gasto(
        id = 2L,
        descricao = "Supermercado",
        valor = BigDecimal("450.00"),
        dataGasto = LocalDate.of(2026, 4, 8),
        mesReferencia = 4,
        anoReferencia = 2026,
        categoria = CategoriaGasto.ALIMENTACAO,
        essencial = true,
    ),
    Gasto(
        id = 3L,
        descricao = "Netflix",
        valor = BigDecimal("55.90"),
        dataGasto = LocalDate.of(2026, 4, 10),
        mesReferencia = 4,
        anoReferencia = 2026,
        categoria = CategoriaGasto.LAZER,
        recorrente = true,
    ),
)

@Preview(showBackground = true, name = "ListaGasto · Preenchida · Light")
@Preview(showBackground = true, name = "ListaGasto · Preenchida · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListaGastoPreenchidaPreview() {
    RumoTheme {
        ListaGastoContent(
            uiState = ListaGastoUiState(gastos = gastosFake, mesAno = YearMonth.of(2026, 4)),
        )
    }
}

@Preview(showBackground = true, name = "ListaGasto · Vazia · Light")
@Preview(showBackground = true, name = "ListaGasto · Vazia · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListaGastoVaziaPreview() {
    RumoTheme {
        ListaGastoContent(
            uiState = ListaGastoUiState(gastos = emptyList(), mesAno = YearMonth.of(2026, 4)),
        )
    }
}

@Preview(showBackground = true, name = "ListaGasto · Shell · Light")
@Preview(showBackground = true, name = "ListaGasto · Shell · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListaGastoPreview() {
    RumoTheme {
        HomeScreenPreviewShell(initialTab = HomeEvent.IrParaGastos)
    }
}