package com.fsales.app.rumo.ui.feature.ganho.lista

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.fsales.app.rumo.ui.ListaGanhoUiEvent
import com.fsales.app.rumo.ui.feature.home.HomeEvent
import com.fsales.app.rumo.ui.feature.home.HomeScreen
import com.fsales.app.rumo.ui.theme.RumoTheme

@Composable
fun ListaGanhoScreen(
    modifier: Modifier = Modifier,
    viewModel: ListaGanhoViewModel = hiltViewModel(),
    onNavigateToCadastro: () -> Unit = {},
) {
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ListaGanhoUiEvent.NavigateToCadastro -> onNavigateToCadastro()
            }
        }
    }
    ListaGanhoContent(
        modifier = modifier,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun ListaGanhoContent(
    modifier: Modifier = Modifier,
    onEvent: (ListaGanhoEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Text(text = "Lista de Ganhos")
    }
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListaGanhoPreview() {
    RumoTheme {
        HomeScreen(initialTab = HomeEvent.IrParaGanhos)
    }
}