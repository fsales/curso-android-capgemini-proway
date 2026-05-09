package com.fsales.app.smartcontact.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fsales.app.smartcontact.model.Contato
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private fun maskTelefone(telefone: String): String {
    val digits = telefone.filter { it.isDigit() }
    return when {
        digits.length == 11 -> MaskVisualTransformation.CELULAR.filter(androidx.compose.ui.text.AnnotatedString(digits)).text.text
        digits.length == 10 -> MaskVisualTransformation.FIXO.filter(androidx.compose.ui.text.AnnotatedString(digits)).text.text
        else -> telefone
    }
}

private fun maskCep(cep: String): String {
    val digits = cep.filter { it.isDigit() }
    return if (digits.length == 8) MaskVisualTransformation.CEP.filter(androidx.compose.ui.text.AnnotatedString(digits)).text.text else cep
}

@Composable
fun ContatoItemCard(
    contato: Contato,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSwipeDelete: (() -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null,
) {
    val dismissState = rememberSwipeToDismissBoxState()

    // Se foi feito swipe para direita (excluir), chamar callback
    if (dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd && onSwipeDelete != null) {
        onSwipeDelete()
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
        modifier = modifier,
        enableDismissFromEndToStart = false,
    ) {
        ItemCard(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
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
                            text = maskTelefone(contato.telefone),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = " · ",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = maskCep(contato.endereco.cep),
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
                    if (contato.endereco.logradouro.isNotBlank() || contato.endereco.numero.isNotBlank() || contato.endereco.bairro.isNotBlank() || contato.endereco.cidade.isNotBlank() || contato.endereco.estado.isNotBlank()) {
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = listOfNotNull(
                                contato.endereco.logradouro.takeIf { it.isNotBlank() },
                                contato.endereco.numero.takeIf { it.isNotBlank() },
                                contato.endereco.bairro.takeIf { it.isNotBlank() },
                                contato.endereco.cidade.takeIf { it.isNotBlank() },
                                contato.endereco.estado.takeIf { it.isNotBlank() }
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
}

@Preview(showBackground = true, name = "ContatoItemCard · Completo")
@Composable
private fun ContatoItemCardPreview() {
    val contato = Contato(
        id = 1L,
        nome = "Maria Silva",
        email = "maria@email.com",
        telefone = "(11) 99999-8888",
        dataNascimento = LocalDate.of(1990, 5, 8),
        endereco = com.fsales.app.smartcontact.model.Endereco(
            cep = "01234-567",
            bairro = "Centro",
            logradouro = "Rua das Flores",
            numero = "123",
            estado = "SP",
            cidade = "São Paulo",
        ),
    )
    MaterialTheme {
        ContatoItemCard(contato = contato, onClick = {})
    }
}
