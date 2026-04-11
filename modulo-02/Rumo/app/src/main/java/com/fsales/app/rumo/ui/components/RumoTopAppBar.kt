package com.fsales.app.rumo.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.R
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.iconSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RumoTopAppBar(
    carregando: Boolean = false,
) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.topbar_titulo),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
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

@Preview(showBackground = true, name = "TopAppBar · Carregando · Light")
@Preview(showBackground = true, name = "TopAppBar · Carregando · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RumoTopAppBarCarregandoPreview() {
    RumoTheme {
        RumoTopAppBar(carregando = true)
    }
}

@Preview(showBackground = true, name = "TopAppBar · Light")
@Preview(showBackground = true, name = "TopAppBar · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RumoTopAppBarPreview() {
    RumoTheme {
        RumoTopAppBar(carregando = false)
    }
}
