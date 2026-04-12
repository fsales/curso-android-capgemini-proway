package com.fsales.app.rumo.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.R
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.iconSize
import com.fsales.app.rumo.ui.theme.spacing

// =============================================================================
// Estado de erro com botão de retry — reutilizado em Ganho, Gasto e Sonho
// =============================================================================
@Composable
fun RumoErroState(
    mensagem: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(MaterialTheme.iconSize.extraLarge),
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Text(
            text = mensagem,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        OutlinedButton(onClick = onRetry) {
            Text(text = stringResource(R.string.erro_tentar_novamente))
        }
    }
}

@Preview(showBackground = true, name = "RumoErroState · Light")
@Preview(showBackground = true, name = "RumoErroState · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RumoErroStatePreview() {
    RumoTheme {
        RumoErroState(
            mensagem = "Não foi possível carregar os dados.",
            onRetry = {},
        )
    }
}




