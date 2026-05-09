package com.fsales.app.smartcontact.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.material3.Icon as M3Icon

/**
 * Overlay de loading centralizado que cobre toda a tela.
 *
 * Deve ser composto **sobre** o conteúdo principal (use `Box` com `zIndex` ou
 * coloque-o como último filho do `Box` pai).
 *
 * @param visivel Se `false`, o componente não é composto (saída do composition).
 * @param modifier Modifier aplicado ao container externo.
 */
@Composable
fun SmartContactLoading(
    visivel: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!visivel) return

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .zIndex(1f)
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.32f)),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            M3Icon(
                painter = painterResource(id = com.fsales.app.smartcontact.R.drawable.ic_app_logo),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SmartContactLoadingPreview() {
    MaterialTheme {
        SmartContactLoading(visivel = true)
    }
}
