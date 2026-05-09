package com.fsales.app.smartcontact.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Card genérico reutilizável para itens de listagem.
 * Slots: leadingIcon, titulo, subtitulo, trailingContent
 */
@Composable
fun ItemCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    titulo: @Composable () -> Unit,
    subtitulo: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                titulo()
                subtitulo?.invoke()
            }

            if (trailingContent != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingContent()
            }
        }
    }
}

// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "ItemCard · Sem badge · Light")
@Composable
private fun ItemCardSemBadgePreview() {
    MaterialTheme {
        ItemCard(
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            titulo = {
                Text(text = "Nome do Item", style = MaterialTheme.typography.titleMedium)
            },
            subtitulo = {
                Text(
                    text = "Descrição ou detalhe",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
        )
    }
}

@Preview(showBackground = true, name = "ItemCard · Com badge · Light")
@Composable
private fun ItemCardComBadgePreview() {
    MaterialTheme {
        ItemCard(
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            titulo = {
                Text(text = "Outro Item", style = MaterialTheme.typography.titleMedium)
            },
            subtitulo = {
                Text(
                    text = "Outro detalhe",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            trailingContent = {
                Text(
                    text = "Badge",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            },
        )
    }
}

