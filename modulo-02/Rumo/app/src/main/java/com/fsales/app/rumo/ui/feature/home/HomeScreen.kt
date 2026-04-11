package com.fsales.app.rumo.ui.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.ui.components.RumoNavigationBar
import com.fsales.app.rumo.ui.feature.ganho.lista.ListaGanhoContent
import com.fsales.app.rumo.ui.feature.ganho.lista.ListaGanhoScreen
import com.fsales.app.rumo.ui.feature.gasto.lista.ListaGastoContent
import com.fsales.app.rumo.ui.feature.gasto.lista.ListaGastoScreen
import com.fsales.app.rumo.ui.feature.sonho.lista.ListaSonhoContent
import com.fsales.app.rumo.ui.feature.sonho.lista.ListaSonhoScreen
import com.fsales.app.rumo.ui.theme.RumoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    initialTab: HomeEvent = HomeEvent.IrParaGanhos,
) {
    var selectedTab by remember { mutableStateOf(initialTab) }

    Scaffold(
        bottomBar = {
            RumoNavigationBar(
                selectedEvent = selectedTab,
                onItemSelected = { selectedTab = it },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        HomeDestino(
            selectedTab = selectedTab,
            paddingValues = paddingValues,
        )
    }
}

@Composable
private fun HomeDestino(
    selectedTab: HomeEvent,
    paddingValues: PaddingValues,
) {
    val modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()

    when (selectedTab) {
        HomeEvent.IrParaGanhos -> ListaGanhoScreen(modifier = modifier)
        HomeEvent.IrParaGastos -> ListaGastoScreen(modifier = modifier)
        HomeEvent.IrParaSonhos -> ListaSonhoScreen(modifier = modifier)
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
    var selectedTab by remember { mutableStateOf(initialTab) }

    Scaffold(
        bottomBar = {
            RumoNavigationBar(
                selectedEvent = selectedTab,
                onItemSelected = { selectedTab = it },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()

        when (selectedTab) {
            HomeEvent.IrParaGanhos -> ListaGanhoContent(modifier = modifier, onEvent = {})
            HomeEvent.IrParaGastos -> ListaGastoContent(modifier = modifier, onEvent = {})
            HomeEvent.IrParaSonhos -> ListaSonhoContent(modifier = modifier)
        }
    }
}

// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "Home · Ganhos · Light")
@Preview(showBackground = true, name = "Home · Ganhos · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeGanhosPreview() {
    RumoTheme {
        HomeScreenPreviewShell(initialTab = HomeEvent.IrParaGanhos)
    }
}

@Preview(showBackground = true, name = "Home · Gastos · Light")
@Preview(showBackground = true, name = "Home · Gastos · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeGastosPreview() {
    RumoTheme {
        HomeScreenPreviewShell(initialTab = HomeEvent.IrParaGastos)
    }
}

@Preview(showBackground = true, name = "Home · Sonhos · Light")
@Preview(showBackground = true, name = "Home · Sonhos · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeSonhosPreview() {
    RumoTheme {
        HomeScreenPreviewShell(initialTab = HomeEvent.IrParaSonhos)
    }
}