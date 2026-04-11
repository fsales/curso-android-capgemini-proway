package com.fsales.app.rumo.ui.feature.gasto.lista

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
import com.fsales.app.rumo.ui.ListaGastoUiEvent
import com.fsales.app.rumo.ui.feature.home.HomeEvent
import com.fsales.app.rumo.ui.feature.home.HomeScreen
import com.fsales.app.rumo.ui.theme.RumoTheme

@Composable
fun ListaGastoScreen(
    modifier: Modifier = Modifier,
    viewModel: ListaGastoViewModel = hiltViewModel(),
    onNavigateToCadastro: () -> Unit = {},
) {
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ListaGastoUiEvent.NavigateToCadastro -> onNavigateToCadastro()
            }
        }
    }
    ListaGastoContent(
        modifier = modifier,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun ListaGastoContent(
    modifier: Modifier = Modifier,
    onEvent: (ListaGastoEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Text(text = "Lista de Gastos")
    }
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListaGastoPreview() {
    RumoTheme {
        HomeScreen(initialTab = HomeEvent.IrParaGastos)
    }
}