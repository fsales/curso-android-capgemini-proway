package com.fsales.app.rumo.ui.feature.sonho.lista

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fsales.app.rumo.R
import com.fsales.app.rumo.core.domain.model.PrioridadeSonho
import com.fsales.app.rumo.core.domain.model.ProjecaoSonho
import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.ui.ListaSonhoUiEvent
import com.fsales.app.rumo.ui.components.RumoEmptyState
import com.fsales.app.rumo.ui.components.RumoErroState
import com.fsales.app.rumo.ui.components.RumoListaScaffold
import com.fsales.app.rumo.ui.feature.home.HomeEvent
import com.fsales.app.rumo.ui.feature.home.HomeScreenPreviewShell
import com.fsales.app.rumo.ui.feature.sonho.components.SonhoItem
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.spacing
import java.math.BigDecimal

// =============================================================================
// Screen — ponto de entrada da feature, consome ViewModel e roteia UiEvent
// =============================================================================
@Composable
fun ListaSonhoScreen(
    modifier: Modifier = Modifier,
    viewModel: ListaSonhoViewModel = hiltViewModel(),
    onNavigateToCadastro: () -> Unit = {},
    onNavigateToDetalhe: (Long) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ListaSonhoUiEvent.NavigateToCadastro         -> onNavigateToCadastro()
                is ListaSonhoUiEvent.NavigateToDetalhe -> onNavigateToDetalhe(event.sonhoId)
            }
        }
    }

    ListaSonhoContent(
        modifier = modifier,
        uiState  = uiState,
        onEvent  = viewModel::onEvent,
    )
}

// =============================================================================
// Content — renderiza estado puro; sem ViewModel
// =============================================================================
@Composable
fun ListaSonhoContent(
    modifier: Modifier = Modifier,
    uiState: ListaSonhoUiState = ListaSonhoUiState(),
    onEvent: (ListaSonhoEvent) -> Unit = {},
) {
    RumoListaScaffold(
        modifier = modifier,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onEvent(ListaSonhoEvent.IrParaCadastro) },
                icon    = { Icon(imageVector = Icons.Filled.Add, contentDescription = null) },
                text    = { Text(text = stringResource(R.string.lista_sonho_adicionar)) },
            )
        },
    ) { paddingValues ->
        when {
            uiState.erro != null -> {
                RumoErroState(
                    mensagem = uiState.erro,
                    onRetry  = { onEvent(ListaSonhoEvent.TentarNovamente) },
                    modifier = Modifier.padding(paddingValues),
                )
            }

            uiState.projecoes.isEmpty() && !uiState.carregando -> {
                RumoEmptyState(
                    icone       = Icons.Filled.AutoAwesome,
                    tituloRes   = R.string.lista_sonho_vazia_titulo,
                    mensagemRes = R.string.lista_sonho_vazia_mensagem,
                    modifier    = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                )
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start  = MaterialTheme.spacing.medium,
                        end    = MaterialTheme.spacing.medium,
                        top    = paddingValues.calculateTopPadding() + MaterialTheme.spacing.medium,
                        bottom = paddingValues.calculateBottomPadding() + MaterialTheme.spacing.medium,
                    ),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(uiState.projecoes, key = { it.sonho.id }) { projecao ->
                        SonhoItem(
                            projecao = projecao,
                            onClick  = { onEvent(ListaSonhoEvent.AbrirSonho(projecao.sonho.id)) },
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// Previews
// =============================================================================
private fun fakeSonho(
    id: Long,
    titulo: String,
    valorMeta: BigDecimal,
    prioridade: PrioridadeSonho,
    concluido: Boolean = false,
) = Sonho(id = id, titulo = titulo, valorMeta = valorMeta, prioridade = prioridade, concluido = concluido)

private val projecoesFake = listOf(
    ProjecaoSonho(
        sonho                = fakeSonho(1L, "Carro novo", BigDecimal("80000"), PrioridadeSonho.ALTA),
        saldoMensal          = BigDecimal("3055.55"),
        mesesNecessarios     = 27,
        seraAlcancadoNoPrazo = true,
    ),
    ProjecaoSonho(
        sonho                = fakeSonho(2L, "Viagem para o Japão", BigDecimal("15000"), PrioridadeSonho.MEDIA),
        saldoMensal          = BigDecimal.ZERO,
        mesesNecessarios     = null,
        seraAlcancadoNoPrazo = null,
    ),
    ProjecaoSonho(
        sonho                = fakeSonho(3L, "Apartamento próprio", BigDecimal("300000"), PrioridadeSonho.ALTA, concluido = true),
        saldoMensal          = BigDecimal.ZERO,
        mesesNecessarios     = null,
        seraAlcancadoNoPrazo = null,
    ),
)

@Preview(showBackground = true, name = "ListaSonho · Preenchida · Light")
@Preview(showBackground = true, name = "ListaSonho · Preenchida · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListaSonhoPreenchidaPreview() {
    RumoTheme { ListaSonhoContent(uiState = ListaSonhoUiState(projecoes = projecoesFake)) }
}

@Preview(showBackground = true, name = "ListaSonho · Vazia · Light")
@Preview(showBackground = true, name = "ListaSonho · Vazia · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListaSonhoVaziaPreview() {
    RumoTheme { ListaSonhoContent(uiState = ListaSonhoUiState(projecoes = emptyList())) }
}

@Preview(showBackground = true, name = "ListaSonho · Shell · Light")
@Preview(showBackground = true, name = "ListaSonho · Shell · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListaSonhoPreview() {
    RumoTheme { HomeScreenPreviewShell(initialTab = HomeEvent.IrParaSonhos) }
}