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
import com.fsales.app.rumo.ui.HomeUiEvent
import com.fsales.app.rumo.ui.components.RumoNavigationBar
import com.fsales.app.rumo.ui.components.RumoTopAppBar
import com.fsales.app.rumo.ui.feature.ganho.lista.ListaGanhoContent
import com.fsales.app.rumo.ui.feature.ganho.lista.ListaGanhoScreen
import com.fsales.app.rumo.ui.feature.gasto.lista.ListaGastoContent
import com.fsales.app.rumo.ui.feature.gasto.lista.ListaGastoScreen
import com.fsales.app.rumo.ui.feature.sonho.lista.ListaSonhoContent
import com.fsales.app.rumo.ui.feature.sonho.lista.ListaSonhoScreen
import com.fsales.app.rumo.ui.theme.RumoTheme
import kotlinx.coroutines.delay

private const val FADE_DURATION_MS = 220
private const val SCALE_INITIAL = 0.92f
private const val SCALE_TARGET  = 1.08f
private const val LOADING_DURATION_MS = 400L

// =============================================================================
// Tela real — usa hiltViewModel + LaunchedEffect para coletar HomeUiEvent
// =============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    var abaAtiva by remember { mutableStateOf<HomeEvent>(HomeEvent.IrParaGanhos) }
    var carregando by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            carregando = true
            when (event) {
                HomeUiEvent.NavigateToListaGanho -> abaAtiva = HomeEvent.IrParaGanhos
                HomeUiEvent.NavigateToListaGasto -> abaAtiva = HomeEvent.IrParaGastos
                HomeUiEvent.NavigateToListaSonho -> abaAtiva = HomeEvent.IrParaSonhos
            }
            delay(LOADING_DURATION_MS)
            carregando = false
        }
    }

    HomeScaffold(
        abaAtiva = abaAtiva,
        carregando = carregando,
        onAbaAtiva = viewModel::onEvent,
        conteudo = { modifier ->
            HomeDestino(abaAtiva = abaAtiva, modifier = modifier)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScaffold(
    abaAtiva: HomeEvent,
    carregando: Boolean = false,
    onAbaAtiva: (HomeEvent) -> Unit,
    conteudo: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        topBar = { RumoTopAppBar(carregando = carregando) },
        bottomBar = {
            RumoNavigationBar(
                selectedEvent = abaAtiva,
                onItemSelected = onAbaAtiva,
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        conteudo(
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        )
    }
}

@Composable
private fun HomeDestino(
    abaAtiva: HomeEvent,
    modifier: Modifier,
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
            HomeEvent.IrParaGanhos -> ListaGanhoScreen()
            HomeEvent.IrParaGastos -> ListaGastoScreen()
            HomeEvent.IrParaSonhos -> ListaSonhoScreen()
        }
    }
}

// =============================================================================
// Shell sem ViewModel — exclusivo para @Preview
// Usa *Content diretamente para evitar dependência do Hilt no preview renderer
// =============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenPreviewShell(
    initialTab: HomeEvent = HomeEvent.IrParaGanhos,
) {
    var abaAtiva by remember { mutableStateOf(initialTab) }

    HomeScaffold(
        abaAtiva = abaAtiva,
        onAbaAtiva = { abaAtiva = it },
        conteudo = { modifier ->
            AnimatedContent(
                targetState = abaAtiva,
                transitionSpec = {
                    (fadeIn(tween(FADE_DURATION_MS)) + scaleIn(tween(FADE_DURATION_MS), initialScale = SCALE_INITIAL)) togetherWith
                    (fadeOut(tween(FADE_DURATION_MS)) + scaleOut(tween(FADE_DURATION_MS), targetScale = SCALE_TARGET))
                },
                label = "HomeDestinoFadePreview",
                modifier = modifier,
            ) { aba ->
                when (aba) {
                    HomeEvent.IrParaGanhos -> ListaGanhoContent(onEvent = {})
                    HomeEvent.IrParaGastos -> ListaGastoContent(onEvent = {})
                    HomeEvent.IrParaSonhos -> ListaSonhoContent()
                }
            }
        },
    )
}

// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "Home · Ganhos · Light")
@Preview(showBackground = true, name = "Home · Ganhos · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeGanhosPreview() {
    RumoTheme { HomeScreenPreviewShell(initialTab = HomeEvent.IrParaGanhos) }
}

@Preview(showBackground = true, name = "Home · Gastos · Light")
@Preview(showBackground = true, name = "Home · Gastos · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeGastosPreview() {
    RumoTheme { HomeScreenPreviewShell(initialTab = HomeEvent.IrParaGastos) }
}

@Preview(showBackground = true, name = "Home · Sonhos · Light")
@Preview(showBackground = true, name = "Home · Sonhos · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeSonhosPreview() {
    RumoTheme { HomeScreenPreviewShell(initialTab = HomeEvent.IrParaSonhos) }
}