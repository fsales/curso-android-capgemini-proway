@file:OptIn(ExperimentalMaterial3Api::class)
package com.fsales.app.smartcontact.ui.feature.lista

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fsales.app.smartcontact.R
import com.fsales.app.smartcontact.model.Contato
import com.fsales.app.smartcontact.ui.components.ConfirmarExclusaoDialog
import com.fsales.app.smartcontact.ui.components.ContatoItemCard
import com.fsales.app.smartcontact.ui.components.EmptyState
import com.fsales.app.smartcontact.ui.components.SmartContactLoading
import com.fsales.app.smartcontact.ui.components.SmartContactScaffold
import com.fsales.app.smartcontact.ui.theme.SmartContactTheme
import com.fsales.app.smartcontact.viewmodel.ListViewModel

// =============================================================================
// Screen — ponto de entrada; coleta ViewModel e roteia UiEvent
// =============================================================================
@Composable
fun ListScreen(
    onNavigateToAddEdit: (id: Long) -> Unit,
    viewModel: ListViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val excluirSucessoMsg = stringResource(R.string.lista_excluir_sucesso)
    val excluirErroMsg = stringResource(R.string.lista_excluir_erro)

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ListUiEvent.NavegaParaNovo -> onNavigateToAddEdit(0L)
                is ListUiEvent.NavegaParaEdicao -> onNavigateToAddEdit(event.id)
                ListUiEvent.ExcluirSucesso -> snackbarHostState.showSnackbar(excluirSucessoMsg)
                ListUiEvent.ErroAoExcluir -> snackbarHostState.showSnackbar(excluirErroMsg)
            }
        }
    }

    ListContent(
        contatos = uiState.contatos,
        carregando = uiState.carregando,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
    )
}

// =============================================================================
// Content — renderiza estado puro; sem ViewModel
// =============================================================================
@Composable
fun ListContent(
    contatos: List<Contato> = emptyList(),
    carregando: Boolean = false,
    onEvent: (ListEvent) -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val (idParaExcluir, setIdParaExcluir) = remember { mutableLongStateOf(0L) }
    val contatoParaExcluir = contatos.firstOrNull { it.id == idParaExcluir }

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
        Box(modifier = Modifier.padding(paddingValues)) {
            if (contatos.isEmpty() && !carregando) {
                EmptyState(
                    title    = stringResource(R.string.titulo_estado_vazio),
                    message  = stringResource(R.string.mensagem_estado_vazio),
                    icon     = Icons.Default.PersonOutline,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = contatos,
                        key = { it.id },
                    ) { contato ->
                        ContatoItemCard(
                            contato = contato,
                            onClick = { onEvent(ListEvent.NavegaParaEdicao(contato.id)) },
                            onSwipeDelete = { setIdParaExcluir(contato.id) },
                        )
                    }
                }
            }

            SmartContactLoading(visivel = carregando)
        }
    }

    contatoParaExcluir?.let { contato ->
        ConfirmarExclusaoDialog(
            titulo = stringResource(R.string.confirmar_exclusao_titulo_nome, contato.nome),
            onConfirmar = {
                onEvent(ListEvent.Excluir(contato.id))
                setIdParaExcluir(0L)
            },
            onCancelar = {
                setIdParaExcluir(0L)
            },
        )
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
        Contato(
            id = 1L,
            nome = "Maria Silva",
            email = "maria@email.com",
            telefone = "(11) 99999-8888",
            dataNascimento = java.time.LocalDate.of(1990, 5, 8),
            endereco = com.fsales.app.smartcontact.model.Endereco(
                cep = "01234-567",
                bairro = "Centro",
                logradouro = "Rua das Flores",
                numero = "123",
                estado = "SP",
                cidade = "São Paulo",
            ),
        ),
        Contato(
            id = 2L,
            nome = "Joao Souza",
            email = "joao@email.com",
            telefone = "(21) 98888-7777",
            dataNascimento = null,
            endereco = com.fsales.app.smartcontact.model.Endereco(
                cep = "20000-000",
                bairro = "Copacabana",
                logradouro = "Av. Atlantica",
                numero = "456",
                estado = "RJ",
                cidade = "Rio de Janeiro",
            ),
        ),
    )
    SmartContactTheme { ListContent(contatos = contatos) }
}

@Preview(showBackground = true, name = "Empty – Light")
@Preview(showBackground = true, name = "Empty – Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EmptyStatePreview() {
    SmartContactTheme { ListContent(contatos = emptyList()) }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun LoadingPreview() {
    SmartContactTheme { ListContent(contatos = emptyList(), carregando = true) }
}

