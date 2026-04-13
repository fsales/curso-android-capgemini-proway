package com.fsales.app.rumo.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.R
import com.fsales.app.rumo.ui.feature.home.HomeEvent
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.iconSize

// Mapeia a aba ativa para o @StringRes do título de seção
private fun HomeEvent.tituloSecaoRes(): Int = when (this) {
    HomeEvent.IrParaGanhos -> R.string.nav_ganhos
    HomeEvent.IrParaGastos -> R.string.nav_gastos
    HomeEvent.IrParaSonhos -> R.string.nav_sonhos
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RumoTopAppBar(
    carregando: Boolean = false,
    abaAtiva: HomeEvent = HomeEvent.IrParaGanhos,
) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.topbar_titulo),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    AnimatedContent(
                        targetState = abaAtiva,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "TopBarSecao",
                    ) { aba ->
                        Text(
                            text = stringResource(aba.tituloSecaoRes()),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f),
                        )
                    }
                }
            },
            navigationIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_rumo_logo),
                    contentDescription = stringResource(R.string.topbar_logo_descricao),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(MaterialTheme.iconSize.large),
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        )

        AnimatedVisibility(
            visible = carregando,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer,
            )
        }
    }
}

@Preview(showBackground = true, name = "TopAppBar · Ganhos · Light")
@Preview(showBackground = true, name = "TopAppBar · Ganhos · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RumoTopAppBarGanhosPreview() {
    RumoTheme {
        RumoTopAppBar(abaAtiva = HomeEvent.IrParaGanhos)
    }
}

@Preview(showBackground = true, name = "TopAppBar · Gastos · Light")
@Composable
private fun RumoTopAppBarGastosPreview() {
    RumoTheme {
        RumoTopAppBar(abaAtiva = HomeEvent.IrParaGastos)
    }
}

@Preview(showBackground = true, name = "TopAppBar · Sonhos · Light")
@Composable
private fun RumoTopAppBarSonhosPreview() {
    RumoTheme {
        RumoTopAppBar(abaAtiva = HomeEvent.IrParaSonhos)
    }
}

@Preview(showBackground = true, name = "TopAppBar · Carregando · Light")
@Composable
private fun RumoTopAppBarCarregandoPreview() {
    RumoTheme {
        RumoTopAppBar(carregando = true, abaAtiva = HomeEvent.IrParaGastos)
    }
}
