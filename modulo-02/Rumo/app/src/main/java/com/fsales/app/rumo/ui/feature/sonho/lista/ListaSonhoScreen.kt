package com.fsales.app.rumo.ui.feature.sonho.lista

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
import com.fsales.app.rumo.ui.ListaSonhoUiEvent
import com.fsales.app.rumo.ui.feature.home.HomeEvent
import com.fsales.app.rumo.ui.feature.home.HomeScreenPreviewShell
import com.fsales.app.rumo.ui.theme.RumoTheme

@Composable
fun ListaSonhoScreen(
    modifier: Modifier = Modifier,
    viewModel: ListaSonhoViewModel = hiltViewModel(),
    navigateBack: () -> Unit = {},
) {
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ListaSonhoUiEvent.NavigateBack -> navigateBack()
            }
        }
    }
    ListaSonhoContent(modifier = modifier)
}

@Composable
fun ListaSonhoContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Text(text = "Lista de Sonhos")
    }
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListaSonhoPreview() {
    RumoTheme {
        HomeScreenPreviewShell(initialTab = HomeEvent.IrParaSonhos)
    }
}