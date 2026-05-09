package com.fsales.app.smartcontact.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ContatoItemCard(
    contato: EditarAdicionarUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: (@Composable () -> Unit)? = null,
) {
    ItemCard(
        onClick = onClick,
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        titulo = {
            Text(
                text = contato.nome,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        subtitulo = {
            Column {
                Text(
                    text = contato.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = contato.telefone,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (contato.dataNascimento != null) {
                        Text(
                            text = " · ",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = contato.dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                // Endereço resumido
                if (contato.logradouro.isNotBlank() || contato.numero.isNotBlank() || contato.bairro.isNotBlank() || contato.cidade.isNotBlank() || contato.estado.isNotBlank()) {
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = listOfNotNull(
                            contato.logradouro.takeIf { it.isNotBlank() },
                            contato.numero.takeIf { it.isNotBlank() },
                            contato.bairro.takeIf { it.isNotBlank() },
                            contato.cidade.takeIf { it.isNotBlank() },
                            contato.estado.takeIf { it.isNotBlank() }
                        ).joinToString(", "),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        trailingContent = badge,
    )
}

@Preview(showBackground = true, name = "ContatoItemCard · Completo")
@Composable
private fun ContatoItemCardPreview() {
    val contato = EditarAdicionarUiState(
        nome = "Maria Silva",
        email = "maria@email.com",
        telefone = "(11) 99999-8888",
        dataNascimento = LocalDate.of(1990, 5, 8),
        cep = "01234-567",
        bairro = "Centro",
        logradouro = "Rua das Flores",
        numero = "123",
        estado = "SP",
        cidade = "São Paulo"
    )
    MaterialTheme {
        ContatoItemCard(contato = contato, onClick = {})
    }
}
