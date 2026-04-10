package com.fsales.app.rumo.ui.feature.gasto.cadastro

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
import com.fsales.app.rumo.ui.CadastroGastoUiEvent
import com.fsales.app.rumo.ui.theme.RumoTheme

@Composable
fun CadastroGastoScreen(
    viewModel: CadastroGastoViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                CadastroGastoUiEvent.NavigateBack -> navigateBack()
            }
        }
    }
    CadastroGastoContent()
}

@Composable
fun CadastroGastoContent() {
    RumoTheme {
        Scaffold() { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)     // respeita insets do Scaffold (top bar + nav bar + teclado)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // permite rolar quando o teclado está visível
            ) {
                Text(text = "Cadastro de Gasto")
            }
        }
    }
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CadastroGastoPreview() {
    CadastroGastoContent()
}