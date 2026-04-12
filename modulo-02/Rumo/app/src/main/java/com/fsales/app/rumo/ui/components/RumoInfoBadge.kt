package com.fsales.app.rumo.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.iconSize
import com.fsales.app.rumo.ui.theme.spacing

// =============================================================================
// Badge informativo sem interação — reutilizável em qualquer tela do app.
// containerColor/contentColor parametrizáveis para diferentes semânticas:
//   informativo → secondaryContainer (default)
//   alerta      → errorContainer
//   destaque    → tertiaryContainer
// =============================================================================
@Composable
fun RumoInfoBadge(
    label: String,
    modifier: Modifier = Modifier,
    icone: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.smallMedium,
                vertical = MaterialTheme.spacing.extraSmall,
            ),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icone != null) {
                Icon(
                    imageVector = icone,
                    contentDescription = null,
                    modifier = Modifier.size(MaterialTheme.iconSize.extraSmall),
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "RumoInfoBadge · Com ícone · Light")
@Preview(showBackground = true, name = "RumoInfoBadge · Com ícone · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RumoInfoBadgeComIconePreview() {
    RumoTheme {
        RumoInfoBadge(label = "Recorrente", icone = Icons.Filled.Autorenew)
    }
}

@Preview(showBackground = true, name = "RumoInfoBadge · Só texto · Light")
@Preview(showBackground = true, name = "RumoInfoBadge · Só texto · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RumoInfoBadgeSoTextoPreview() {
    RumoTheme {
        RumoInfoBadge(label = "Essencial")
    }
}

@Preview(showBackground = true, name = "RumoInfoBadge · Alerta · Light")
@Preview(showBackground = true, name = "RumoInfoBadge · Alerta · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RumoInfoBadgeAlertaPreview() {
    RumoTheme {
        RumoInfoBadge(
            label = "Essencial",
            icone = Icons.Filled.Warning,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
        )
    }
}


