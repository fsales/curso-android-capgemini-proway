package com.fsales.app.rumo.ui.feature.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.TipoGanho
import com.fsales.app.rumo.ui.HomeUiEvent
import com.fsales.app.rumo.ui.components.RumoNavigationBar
import com.fsales.app.rumo.ui.components.RumoTopAppBar
import com.fsales.app.rumo.ui.feature.ganho.lista.ListaGanhoContent
import com.fsales.app.rumo.ui.feature.ganho.lista.ListaGanhoScreen
import com.fsales.app.rumo.ui.feature.ganho.lista.ListaGanhoUiState
import com.fsales.app.rumo.ui.feature.gasto.lista.ListaGastoContent
import com.fsales.app.rumo.ui.feature.gasto.lista.ListaGastoScreen
import com.fsales.app.rumo.ui.feature.sonho.lista.ListaSonhoContent
import com.fsales.app.rumo.ui.feature.sonho.lista.ListaSonhoScreen
import com.fsales.app.rumo.ui.feature.dashboard.DashboardContent
import com.fsales.app.rumo.ui.feature.dashboard.DashboardUiState
import com.fsales.app.rumo.ui.feature.extrato.ExtratoContent
import com.fsales.app.rumo.ui.feature.extrato.ExtratoUiState
import com.fsales.app.rumo.ui.theme.RumoTheme
import kotlinx.coroutines.delay
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

private const val FADE_DURATION_MS = 220
private const val SCALE_INITIAL = 0.92f
private const val SCALE_TARGET  = 1.08f
private const val LOADING_DURATION_MS = 400L

// =============================================================================
// Screen — ponto de entrada, consome ViewModel e roteia UiEvent
// =============================================================================
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    abaInicial: HomeEvent = HomeEvent.IrParaHome,
    onNavigateToGanhoCadastro: () -> Unit = {},
    onNavigateToGanhoDetalhe: (Long) -> Unit = {},
    onNavigateToGastoCadastro: () -> Unit = {},
    onNavigateToGastoDetalhe: (Long) -> Unit = {},
    onNavigateToSonhoCadastro: () -> Unit = {},
    onNavigateToSonhoDetalhe: (Long) -> Unit = {},
) {
    var uiState by remember { mutableStateOf(HomeUiState(abaAtiva = abaInicial)) }
    var carregandoAba by remember { mutableStateOf(false) }
    var carregandoLista by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            carregandoAba = true
            uiState = when (event) {
                HomeUiEvent.NavigateToHome       -> uiState.copy(abaAtiva = HomeEvent.IrParaHome)
                HomeUiEvent.NavigateToExtrato    -> uiState.copy(abaAtiva = HomeEvent.IrParaExtrato)
                HomeUiEvent.NavigateToListaGanho -> uiState.copy(abaAtiva = HomeEvent.IrParaGanhos)
                HomeUiEvent.NavigateToListaGasto -> uiState.copy(abaAtiva = HomeEvent.IrParaGastos)
                HomeUiEvent.NavigateToListaSonho -> uiState.copy(abaAtiva = HomeEvent.IrParaSonhos)
            }
            delay(LOADING_DURATION_MS)
            carregandoAba = false
        }
    }

    HomeContent(
        uiState = uiState,
        carregando = carregandoAba || carregandoLista,
        onEvent = viewModel::onEvent,
        onCarregandoChange = { carregandoLista = it },
        onNavigateToGanhoCadastro = onNavigateToGanhoCadastro,
        onNavigateToGanhoDetalhe  = onNavigateToGanhoDetalhe,
        onNavigateToGastoCadastro = onNavigateToGastoCadastro,
        onNavigateToGastoDetalhe  = onNavigateToGastoDetalhe,
        onNavigateToSonhoCadastro = onNavigateToSonhoCadastro,
        onNavigateToSonhoDetalhe  = onNavigateToSonhoDetalhe,
    )
}

// =============================================================================
// Content — renderiza estado puro; sem ViewModel nem LaunchedEffect.
// O slot [destinoConteudo] permite que o @Preview injete *Content sem Hilt.
// Por padrão (produção) usa os *Screen reais com hiltViewModel.
// =============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    carregando: Boolean = false,
    onEvent: (HomeEvent) -> Unit,
    onCarregandoChange: (Boolean) -> Unit = {},
    onNavigateToGanhoCadastro: () -> Unit = {},
    onNavigateToGanhoDetalhe: (Long) -> Unit = {},
    onNavigateToGastoCadastro: () -> Unit = {},
    onNavigateToGastoDetalhe: (Long) -> Unit = {},
    onNavigateToSonhoCadastro: () -> Unit = {},
    onNavigateToSonhoDetalhe: (Long) -> Unit = {},
    destinoConteudo: (@Composable (HomeEvent, Modifier) -> Unit)? = null,
) {
    Scaffold(
        topBar = { RumoTopAppBar(carregando = carregando, abaAtiva = uiState.abaAtiva) },
        bottomBar = {
            RumoNavigationBar(
                selectedEvent = uiState.abaAtiva,
                onItemSelected = onEvent,
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val contentModifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()

        if (destinoConteudo != null) {
            destinoConteudo(uiState.abaAtiva, contentModifier)
        } else {
            HomeDestino(
                abaAtiva = uiState.abaAtiva,
                modifier = contentModifier,
                onCarregandoChange = onCarregandoChange,
                onNavigateToGanhoCadastro = onNavigateToGanhoCadastro,
                onNavigateToGanhoDetalhe  = onNavigateToGanhoDetalhe,
                onNavigateToGastoCadastro = onNavigateToGastoCadastro,
                onNavigateToGastoDetalhe  = onNavigateToGastoDetalhe,
                onNavigateToSonhoCadastro = onNavigateToSonhoCadastro,
                onNavigateToSonhoDetalhe  = onNavigateToSonhoDetalhe,
            )
        }
    }
}

// =============================================================================
// Destino animado — privado, compõe as sub-telas com transição fade+scale
// =============================================================================
@Composable
private fun HomeDestino(
    abaAtiva: HomeEvent,
    modifier: Modifier,
    onCarregandoChange: (Boolean) -> Unit,
    onNavigateToGanhoCadastro: () -> Unit,
    onNavigateToGanhoDetalhe: (Long) -> Unit,
    onNavigateToGastoCadastro: () -> Unit,
    onNavigateToGastoDetalhe: (Long) -> Unit,
    onNavigateToSonhoCadastro: () -> Unit,
    onNavigateToSonhoDetalhe: (Long) -> Unit,
) {
    AnimatedContent(
        targetState = abaAtiva,
        transitionSpec = {
            (fadeIn(tween(FADE_DURATION_MS)) + scaleIn(tween(FADE_DURATION_MS), initialScale = SCALE_INITIAL)) togetherWith
            (fadeOut(tween(FADE_DURATION_MS)) + scaleOut(tween(FADE_DURATION_MS), targetScale = SCALE_TARGET))
        },
        label = "HomeDestinoFade",
        modifier = modifier,
    ) { aba ->
        when (aba) {
            HomeEvent.IrParaHome -> com.fsales.app.rumo.ui.feature.dashboard.DashboardScreen(
                onIrExtrato = {},
            )
            HomeEvent.IrParaExtrato -> com.fsales.app.rumo.ui.feature.extrato.ExtratoScreen()
            HomeEvent.IrParaGanhos -> ListaGanhoScreen(
                onNavigateToCadastro = onNavigateToGanhoCadastro,
                onNavigateToDetalhe  = onNavigateToGanhoDetalhe,
                onCarregandoChange = onCarregandoChange,
            )
            HomeEvent.IrParaGastos -> ListaGastoScreen(
                onNavigateToCadastro = onNavigateToGastoCadastro,
                onNavigateToDetalhe  = onNavigateToGastoDetalhe,
                onCarregandoChange = onCarregandoChange,
            )
            HomeEvent.IrParaSonhos -> ListaSonhoScreen(
                onNavigateToCadastro  = onNavigateToSonhoCadastro,
                onNavigateToDetalhe   = onNavigateToSonhoDetalhe,
            )
        }
    }
}

// =============================================================================
// Preview shell — compatibilidade com ListaGanhoScreen, ListaGastoScreen,
// ListaSonhoScreen que usam HomeScreenPreviewShell nos próprios @Preview.
// Usa o slot destinoConteudo com *Content — sem Hilt.
// =============================================================================
private val ganhosFakePreview = listOf(
    Ganho(
        id = 1L, descricao = "Salário", valor = BigDecimal("5000.00"),
        dataRecebimento = LocalDate.of(2026, 4, 5),
        mesReferencia = 4, anoReferencia = 2026,
        tipo = TipoGanho.SALARIO, recorrente = true,
    ),
    Ganho(
        id = 2L, descricao = "Freelance", valor = BigDecimal("1200.00"),
        dataRecebimento = LocalDate.of(2026, 4, 10),
        mesReferencia = 4, anoReferencia = 2026,
        tipo = TipoGanho.RENDA_EXTRA,
    ),
)

@Composable
fun HomeScreenPreviewShell(
    initialTab: HomeEvent = HomeEvent.IrParaGanhos,
) {
    var abaAtiva by remember { mutableStateOf(initialTab) }

    HomeContent(
        uiState = HomeUiState(abaAtiva = abaAtiva),
        onEvent = { abaAtiva = it },
        destinoConteudo = { aba, modifier ->
            AnimatedContent(
                targetState = aba,
                transitionSpec = {
                    (fadeIn(tween(FADE_DURATION_MS)) + scaleIn(tween(FADE_DURATION_MS), initialScale = SCALE_INITIAL)) togetherWith
                    (fadeOut(tween(FADE_DURATION_MS)) + scaleOut(tween(FADE_DURATION_MS), targetScale = SCALE_TARGET))
                },
                label = "HomeDestinoFadePreview",
                modifier = modifier,
            ) { destino ->
                when (destino) {
                    HomeEvent.IrParaHome -> DashboardContent(
                        uiState = DashboardUiState(
                            mesAno = YearMonth.of(2026, 4),
                            totalGanhos = java.math.BigDecimal("6200.00"),
                            totalGastos = java.math.BigDecimal("4200.00"),
                            saldo = java.math.BigDecimal("2000.00"),
                        ),
                        modifier = modifier,
                    )
                    HomeEvent.IrParaGanhos -> ListaGanhoContent(
                        uiState = ListaGanhoUiState(
                            ganhos = ganhosFakePreview,
                            mesAno = YearMonth.of(2026, 4),
                        ),
                        onEvent = {},
                    )
                    HomeEvent.IrParaExtrato -> ExtratoContent(
                        uiState = ExtratoUiState(mesAno = YearMonth.of(2026, 4)),
                        onEvent = {},
                    )
                    HomeEvent.IrParaGastos -> ListaGastoContent(onEvent = {})
                    HomeEvent.IrParaSonhos -> ListaSonhoContent()
                }
            }
        },
    )
}

// =============================================================================
// Previews — usam HomeContent diretamente com estado fake e destinoConteudo
// =============================================================================
@Preview(showBackground = true, name = "Home · Ganhos · Light")
@Preview(showBackground = true, name = "Home · Ganhos · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeGanhosPreview() {
    RumoTheme {
        HomeScreenPreviewShell(initialTab = HomeEvent.IrParaGanhos)
    }
}

@Preview(showBackground = true, name = "Home · Gastos · Light")
@Preview(showBackground = true, name = "Home · Gastos · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeGastosPreview() {
    RumoTheme {
        HomeScreenPreviewShell(initialTab = HomeEvent.IrParaGastos)
    }
}

@Preview(showBackground = true, name = "Home · Sonhos · Light")
@Preview(showBackground = true, name = "Home · Sonhos · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeSonhosPreview() {
    RumoTheme {
        HomeScreenPreviewShell(initialTab = HomeEvent.IrParaSonhos)
    }
}







