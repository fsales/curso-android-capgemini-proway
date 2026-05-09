@file:OptIn(ExperimentalMaterial3Api::class)
package com.fsales.app.smartcontact.ui.feature.lista

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fsales.app.smartcontact.R
import com.fsales.app.smartcontact.ui.components.ContatoItemCard
import com.fsales.app.smartcontact.ui.components.EmptyState
import com.fsales.app.smartcontact.ui.components.SmartContactScaffold
import com.fsales.app.smartcontact.ui.theme.SmartContactTheme
import com.fsales.app.smartcontact.viewmodel.ListViewModel
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarUiState

// =============================================================================
// Screen — ponto de entrada; coleta ViewModel e roteia UiEvent
// =============================================================================
@Composable
fun ListScreen(
    onNavigateToAddEdit: (id: Long) -> Unit,
    viewModel: ListViewModel = viewModel(),
) {
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ListUiEvent.NavegaParaNovo            -> onNavigateToAddEdit(0L)
                is ListUiEvent.NavegaParaEdicao       -> onNavigateToAddEdit(event.id)
            }
        }
    }

    ListContent(onEvent = viewModel::onEvent)
}

// =============================================================================
// Content — renderiza estado puro; sem ViewModel
// =============================================================================
@Composable
fun ListContent(
    contatos: List<EditarAdicionarUiState> = emptyList(),
    onEvent: (ListEvent) -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    SmartContactScaffold(
        title             = stringResource(R.string.titulo_lista_contatos),
        snackbarHostState = snackbarHostState,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text  = { Text(stringResource(R.string.fab_contato)) },
                onClick = { onEvent(ListEvent.NavegaParaNovo) },
                icon  = { Icon(Icons.Default.Add, contentDescription = null) },
            )
        },
    ) { paddingValues ->
        if (contatos.isEmpty()) {
            EmptyState(
                title    = stringResource(R.string.titulo_estado_vazio),
                message  = stringResource(R.string.mensagem_estado_vazio),
                icon     = Icons.Default.PersonOutline,
                modifier = Modifier.fillMaxSize().padding(paddingValues),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                items(contatos) { contato ->
                    ContatoItemCard(
                        contato = contato,
                        onClick = { /* TODO: handle click, e.g., onEvent(ListEvent.NavegaParaEdicao(contato.id)) */ },
                    )
                }
            }
        }
    }
}

// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "List – Light")
@Preview(showBackground = true, name = "List – Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListContentPreview() {
    val contatos = listOf(
        EditarAdicionarUiState(
            nome = "Maria Silva",
            email = "maria@email.com",
            telefone = "(11) 99999-8888",
            dataNascimento = java.time.LocalDate.of(1990, 5, 8),
            cep = "01234-567",
            bairro = "Centro",
            logradouro = "Rua das Flores",
            numero = "123",
            estado = "SP",
            cidade = "São Paulo"
        ),
        EditarAdicionarUiState(
            nome = "João Souza",
            email = "joao@email.com",
            telefone = "(21) 98888-7777",
            dataNascimento = null,
            cep = "20000-000",
            bairro = "Copacabana",
            logradouro = "Av. Atlântica",
            numero = "456",
            estado = "RJ",
            cidade = "Rio de Janeiro"
        )
    )
    SmartContactTheme { ListContent(contatos = contatos) }
}

@Preview(showBackground = true, name = "Empty – Light")
@Preview(showBackground = true, name = "Empty – Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EmptyStatePreview() {
    SmartContactTheme { ListContent(contatos = emptyList()) }
}
