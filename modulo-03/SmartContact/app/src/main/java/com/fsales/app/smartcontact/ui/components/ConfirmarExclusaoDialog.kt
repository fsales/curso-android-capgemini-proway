package com.fsales.app.smartcontact.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.fsales.app.smartcontact.R

@Composable
fun ConfirmarExclusaoDialog(
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit,
    titulo: String = stringResource(R.string.confirmar_exclusao_titulo),
    mensagem: String = stringResource(R.string.confirmar_exclusao_mensagem),
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = {
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        text = {
            Text(
                text = mensagem,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmar()
                    onCancelar()
                },
            ) {
                Text(stringResource(R.string.confirmar_exclusao_excluir))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text(stringResource(R.string.confirmar_exclusao_cancelar))
            }
        },
    )
}
