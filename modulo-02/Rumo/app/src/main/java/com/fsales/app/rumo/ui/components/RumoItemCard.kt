package com.fsales.app.rumo.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.spacing

// =============================================================================
// Card genérico reutilizado por Ganho, Gasto e Sonho
// Slots: leadingIcon, titulo, subtitulo, trailingContent
// =============================================================================
@Composable
fun RumoItemCard(
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
                .padding(MaterialTheme.spacing.medium)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            ) {
                titulo()
                subtitulo?.invoke()
            }

            if (trailingContent != null) {
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                trailingContent()
            }
        }
    }
}

// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "RumoItemCard · Sem badge · Light")
@Preview(showBackground = true, name = "RumoItemCard · Sem badge · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RumoItemCardSemBadgePreview() {
    RumoTheme {
        RumoItemCard(
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Savings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            titulo = {
                Text(text = "Dividendos", style = MaterialTheme.typography.titleMedium)
            },
            subtitulo = {
                Text(
                    text = "R$ 320,50",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
        )
    }
}

@Preview(showBackground = true, name = "RumoItemCard · Com badge · Light")
@Preview(showBackground = true, name = "RumoItemCard · Com badge · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RumoItemCardComBadgePreview() {
    RumoTheme {
        RumoItemCard(
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Savings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            titulo = {
                Text(text = "Salário", style = MaterialTheme.typography.titleMedium)
            },
            subtitulo = {
                Text(
                    text = "R$ 5.000,00",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            trailingContent = {
                RumoInfoBadge(
                    label = "Recorrente",
                    icone = Icons.Filled.Autorenew,
                )
            },
        )
    }
}




