package com.fsales.app.rumo.ui.feature.gasto.lista

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.fsales.app.rumo.ui.ListaGastoUiEvent
import com.fsales.app.rumo.ui.theme.RumoTheme

@Composable
fun ListaGastoScreen(
    viewModel: ListaGastoViewModel = hiltViewModel(),
    onNavigateToCadastro: () -> Unit
) {
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when(event){
                ListaGastoUiEvent.NavigateToCadastro -> onNavigateToCadastro()
            }
        }
    }
    ListaGastoContent(
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ListaGastoContent(
    onEvent: (ListaGastoEvent) -> Unit
) {
    RumoTheme {
        Scaffold() { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)     // respeita insets do Scaffold (top bar + nav bar + teclado)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // permite rolar quando o teclado está visível
            ) {
                Text(text = "Lista de Gastos")
            }
        }
    }
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListaGastoPreview() {
    ListaGastoContent(
        onEvent = { }
    )
}