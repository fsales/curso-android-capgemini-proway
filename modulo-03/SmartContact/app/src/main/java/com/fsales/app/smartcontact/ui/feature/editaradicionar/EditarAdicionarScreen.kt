@file:OptIn(ExperimentalMaterial3Api::class)
package com.fsales.app.smartcontact.ui.feature.editaradicionar

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fsales.app.smartcontact.R
import com.fsales.app.smartcontact.ui.components.MaskVisualTransformation
import com.fsales.app.smartcontact.ui.components.SmartContactDatePickerField
import com.fsales.app.smartcontact.ui.components.SmartContactScaffold
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarUiState
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.toStringRes
import com.fsales.app.smartcontact.ui.theme.spacing
import com.fsales.app.smartcontact.viewmodel.EditarAdicionarViewModel

// =============================================================================
// Screen — ponto de entrada; coleta ViewModel e roteia UiEvent
// =============================================================================
@Composable
fun EditarAdicionarScreen(
    contatoId: Long? = null,
    navigateBack: () -> Unit = {},
    viewModel: EditarAdicionarViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val erroSalvarMsg = stringResource(R.string.cadastro_edicao_erro_salvar)

    LaunchedEffect(contatoId) {
        viewModel.carregarContato(contatoId)
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                EditarAdicionarUiEvent.NavigateBack -> navigateBack()
                EditarAdicionarUiEvent.ErroAoSalvar -> snackbarHostState.showSnackbar(erroSalvarMsg)
            }
        }
    }

    EditarAdicionarContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
    )
}

// =============================================================================
// Content — renderiza estado puro; sem ViewModel
// =============================================================================
@Composable
fun EditarAdicionarContent(
    uiState: EditarAdicionarUiState = EditarAdicionarUiState(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onEvent: (EditarAdicionarEvent) -> Unit = {},
) {
    SmartContactScaffold(
        title             = stringResource(R.string.titulo_cadastro_edicao),
        snackbarHostState = snackbarHostState,
        navigationIcon    = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationClick = { onEvent(EditarAdicionarEvent.Voltar) },
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical   = MaterialTheme.spacing.large,
                    ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            ) {
                // Nome
                OutlinedTextField(
                    value          = uiState.nome,
                    onValueChange  = { onEvent(EditarAdicionarEvent.AlterarNome(it)) },
                    label          = { Text(stringResource(R.string.form_nome)) },
                    isError        = uiState.errors.nome != null,
                    supportingText = uiState.errors.nome?.let { erro ->
                        { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_nome)), color = MaterialTheme.colorScheme.error) }
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType   = KeyboardType.Text,
                        imeAction      = ImeAction.Next,
                    ),
                    singleLine = true,
                    modifier   = Modifier.fillMaxWidth(),
                )
                // E-mail
                OutlinedTextField(
                    value          = uiState.email,
                    onValueChange  = { onEvent(EditarAdicionarEvent.AlterarEmail(it)) },
                    label          = { Text(stringResource(R.string.form_email)) },
                    isError        = uiState.errors.email != null,
                    supportingText = uiState.errors.email?.let { erro ->
                        { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_email)), color = MaterialTheme.colorScheme.error) }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction    = ImeAction.Next,
                    ),
                    singleLine = true,
                    modifier   = Modifier.fillMaxWidth(),
                )
                // Telefone — aceita somente dígitos, máscara dinâmica celular/fixo
                val telefoneMask = if (uiState.telefone.length <= 10)
                    MaskVisualTransformation.FIXO else MaskVisualTransformation.CELULAR
                OutlinedTextField(
                    value          = uiState.telefone,
                    onValueChange  = { if (it.length <= 11) onEvent(EditarAdicionarEvent.AlterarTelefone(it.filter(Char::isDigit))) },
                    label          = { Text(stringResource(R.string.form_telefone)) },
                    isError        = uiState.errors.telefone != null,
                    supportingText = uiState.errors.telefone?.let { erro ->
                        { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_telefone)), color = MaterialTheme.colorScheme.error) }
                    },
                    visualTransformation = telefoneMask,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction    = ImeAction.Next,
                    ),
                    singleLine = true,
                    modifier   = Modifier.fillMaxWidth(),
                )
                // Data de Nascimento
                SmartContactDatePickerField(
                    data           = uiState.dataNascimento,
                    onDateSelected = { onEvent(EditarAdicionarEvent.AlterarDataNascimento(it)) },
                    label          = stringResource(R.string.form_data_nascimento),
                    erro           = uiState.errors.dataNascimento?.let {
                        stringResource(it.toStringRes(), stringResource(R.string.form_data_nascimento))
                    },
                    modifier       = Modifier.fillMaxWidth(),
                )
                // CEP — aceita somente dígitos, máscara 00000-000
                // Exibe CircularProgressIndicator enquanto carregandoCep == true
                OutlinedTextField(
                    value          = uiState.cep,
                    onValueChange  = { if (it.length <= 8) onEvent(EditarAdicionarEvent.AlterarCep(it.filter(Char::isDigit))) },
                    label          = { Text(stringResource(R.string.form_cep)) },
                    isError        = uiState.errors.cep != null,
                    supportingText = uiState.errors.cep?.let { erro ->
                        { Text(stringResource(erro.toStringRes()), color = MaterialTheme.colorScheme.error) }
                    },
                    trailingIcon   = if (uiState.carregandoCep) {
                        { CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp) }
                    } else null,
                    visualTransformation = MaskVisualTransformation.CEP,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction    = ImeAction.Next,
                    ),
                    singleLine = true,
                    modifier   = Modifier.fillMaxWidth(),
                )
                // Logradouro — editável, pode ser sobrescrito pelo ViaCEP
                OutlinedTextField(
                    value          = uiState.logradouro,
                    onValueChange  = { onEvent(EditarAdicionarEvent.AlterarLogradouro(it)) },
                    label          = { Text(stringResource(R.string.form_logradouro)) },
                    isError        = uiState.errors.logradouro != null,
                    supportingText = uiState.errors.logradouro?.let { erro ->
                        { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_logradouro)), color = MaterialTheme.colorScheme.error) }
                    },
                    enabled = !uiState.carregandoCep,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType   = KeyboardType.Text,
                        imeAction      = ImeAction.Next,
                    ),
                    singleLine = true,
                    modifier   = Modifier.fillMaxWidth(),
                )
                // Número
                OutlinedTextField(
                    value          = uiState.numero,
                    onValueChange  = { onEvent(EditarAdicionarEvent.AlterarNumero(it)) },
                    label          = { Text(stringResource(R.string.form_numero)) },
                    isError        = uiState.errors.numero != null,
                    supportingText = uiState.errors.numero?.let { erro ->
                        { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_numero)), color = MaterialTheme.colorScheme.error) }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction    = ImeAction.Next,
                    ),
                    singleLine = true,
                    modifier   = Modifier.fillMaxWidth(),
                )
                // Bairro — editável, pode ser sobrescrito pelo ViaCEP
                OutlinedTextField(
                    value          = uiState.bairro,
                    onValueChange  = { onEvent(EditarAdicionarEvent.AlterarBairro(it)) },
                    label          = { Text(stringResource(R.string.form_bairro)) },
                    isError        = uiState.errors.bairro != null,
                    supportingText = uiState.errors.bairro?.let { erro ->
                        { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_bairro)), color = MaterialTheme.colorScheme.error) }
                    },
                    enabled = !uiState.carregandoCep,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType   = KeyboardType.Text,
                        imeAction      = ImeAction.Next,
                    ),
                    singleLine = true,
                    modifier   = Modifier.fillMaxWidth(),
                )
                // Cidade — somente leitura; preenchida pelo ViaCEP
                OutlinedTextField(
                    value          = uiState.cidade,
                    onValueChange  = {},
                    label          = { Text(stringResource(R.string.form_cidade)) },
                    isError        = uiState.errors.cidade != null,
                    supportingText = uiState.errors.cidade?.let { erro ->
                        { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_cidade)), color = MaterialTheme.colorScheme.error) }
                    },
                    enabled = false,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType   = KeyboardType.Text,
                        imeAction      = ImeAction.Next,
                    ),
                    singleLine = true,
                    modifier   = Modifier.fillMaxWidth(),
                )
                // Estado — somente leitura; preenchido pelo ViaCEP
                OutlinedTextField(
                    value          = uiState.estado,
                    onValueChange  = {},
                    label          = { Text(stringResource(R.string.form_estado)) },
                    isError        = uiState.errors.estado != null,
                    supportingText = uiState.errors.estado?.let { erro ->
                        { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_estado)), color = MaterialTheme.colorScheme.error) }
                    },
                    enabled = false,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        keyboardType   = KeyboardType.Text,
                        imeAction      = ImeAction.Done,
                    ),
                    singleLine = true,
                    modifier   = Modifier.fillMaxWidth(),
                )
                // Botão Salvar
                Button(
                    onClick  = { onEvent(EditarAdicionarEvent.Salvar) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.acao_salvar))
                }
            }
        }
    }
}

// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "New – Light")
@Preview(showBackground = true, name = "New – Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EditarAdicionarPreview() {
    EditarAdicionarContent()
}