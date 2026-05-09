@file:OptIn(ExperimentalMaterial3Api::class)
package com.fsales.app.smartcontact.ui.feature.editaradicionar

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fsales.app.smartcontact.R
import com.fsales.app.smartcontact.ui.components.SmartContactScaffold
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarUiState
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.toStringRes
import com.fsales.app.smartcontact.ui.theme.spacing
import com.fsales.app.smartcontact.viewmodel.EditarAdicionarViewModel

@Composable
fun EditarAdicionarScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: EditarAdicionarViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    EditarAdicionarContent(
        uiState = uiState,
        onNomeChange = viewModel::onNomeChange,
        onEmailChange = viewModel::onEmailChange,
        onTelefoneChange = viewModel::onTelefoneChange,
        onCepChange = viewModel::onCepChange,
        onBairroChange = viewModel::onBairroChange,
        onLogradouroChange = viewModel::onLogradouroChange,
        onNumeroChange = viewModel::onNumeroChange,
        onEstadoChange = viewModel::onEstadoChange,
        onCidadeChange = viewModel::onCidadeChange,
        onSalvar = {
            if (viewModel.onSalvar()) onNavigateBack()
        },
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun EditarAdicionarContent(
    uiState: EditarAdicionarUiState = EditarAdicionarUiState(),
    onNomeChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onTelefoneChange: (String) -> Unit = {},
    onCepChange: (String) -> Unit = {},
    onBairroChange: (String) -> Unit = {},
    onLogradouroChange: (String) -> Unit = {},
    onNumeroChange: (String) -> Unit = {},
    onEstadoChange: (String) -> Unit = {},
    onCidadeChange: (String) -> Unit = {},
    onSalvar: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    SmartContactScaffold(
        title = stringResource(R.string.titulo_cadastro_edicao),
        snackbarHostState = snackbarHostState,
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationClick = onNavigateBack,
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.large
                ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        ) {
            // Nome
            OutlinedTextField(
                value = uiState.nome,
                onValueChange = onNomeChange,
                label = { Text(stringResource(R.string.form_nome)) },
                isError = uiState.errors.nome != null,
                supportingText = uiState.errors.nome?.let { erro ->
                    { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_nome)), color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            // E-mail
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = { Text(stringResource(R.string.form_email)) },
                isError = uiState.errors.email != null,
                supportingText = uiState.errors.email?.let { erro ->
                    { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_email)), color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            // Telefone
            OutlinedTextField(
                value = uiState.telefone,
                onValueChange = onTelefoneChange,
                label = { Text(stringResource(R.string.form_telefone)) },
                isError = uiState.errors.telefone != null,
                supportingText = uiState.errors.telefone?.let { erro ->
                    { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_telefone)), color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            // CEP
            OutlinedTextField(
                value = uiState.cep,
                onValueChange = onCepChange,
                label = { Text(stringResource(R.string.form_cep)) },
                isError = uiState.errors.cep != null,
                supportingText = uiState.errors.cep?.let { erro ->
                    { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_cep)), color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            // Logradouro
            OutlinedTextField(
                value = uiState.logradouro,
                onValueChange = onLogradouroChange,
                label = { Text(stringResource(R.string.form_logradouro)) },
                isError = uiState.errors.logradouro != null,
                supportingText = uiState.errors.logradouro?.let { erro ->
                    { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_logradouro)), color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            // Número
            OutlinedTextField(
                value = uiState.numero,
                onValueChange = onNumeroChange,
                label = { Text(stringResource(R.string.form_numero)) },
                isError = uiState.errors.numero != null,
                supportingText = uiState.errors.numero?.let { erro ->
                    { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_numero)), color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            // Bairro
            OutlinedTextField(
                value = uiState.bairro,
                onValueChange = onBairroChange,
                label = { Text(stringResource(R.string.form_bairro)) },
                isError = uiState.errors.bairro != null,
                supportingText = uiState.errors.bairro?.let { erro ->
                    { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_bairro)), color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            // Cidade
            OutlinedTextField(
                value = uiState.cidade,
                onValueChange = onCidadeChange,
                label = { Text(stringResource(R.string.form_cidade)) },
                isError = uiState.errors.cidade != null,
                supportingText = uiState.errors.cidade?.let { erro ->
                    { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_cidade)), color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            // Estado
            OutlinedTextField(
                value = uiState.estado,
                onValueChange = onEstadoChange,
                label = { Text(stringResource(R.string.form_estado)) },
                isError = uiState.errors.estado != null,
                supportingText = uiState.errors.estado?.let { erro ->
                    { Text(stringResource(erro.toStringRes(), stringResource(R.string.form_estado)), color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true, name = "New – Light")
@Preview(showBackground = true, name = "New – Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditarAdicionarPreview() {
    EditarAdicionarContent()
}