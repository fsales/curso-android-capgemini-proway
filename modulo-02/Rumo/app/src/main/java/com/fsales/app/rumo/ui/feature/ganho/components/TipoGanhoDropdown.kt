package com.fsales.app.rumo.ui.feature.ganho.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.R
import com.fsales.app.rumo.core.domain.model.TipoGanho
import com.fsales.app.rumo.ui.mapper.toIcon
import com.fsales.app.rumo.ui.theme.RumoTheme

// =============================================================================
// Dropdown de TipoGanho — ExposedDropdownMenuBox MD3
// Cada item exibe o ícone mapeado + descrição do domínio.
// =============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipoGanhoDropdown(
    tipoSelecionado: TipoGanho,
    onTipoSelecionado: (TipoGanho) -> Unit,
    modifier: Modifier = Modifier,
    erro: String? = null,
) {
    var expandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = it },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = tipoSelecionado.descricao,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.cadastro_ganho_campo_tipo)) },
            leadingIcon = {
                Icon(
                    imageVector = tipoSelecionado.toIcon(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            isError = erro != null,
            supportingText = erro?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false },
        ) {
            TipoGanho.entries.forEach { tipo ->
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = tipo.toIcon(),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    text = { Text(tipo.descricao) },
                    onClick = {
                        onTipoSelecionado(tipo)
                        expandido = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "TipoGanhoDropdown · Light")
@Preview(showBackground = true, name = "TipoGanhoDropdown · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TipoGanhoDropdownPreview() {
    RumoTheme {
        TipoGanhoDropdown(
            tipoSelecionado = TipoGanho.SALARIO,
            onTipoSelecionado = {},
        )
    }
}



