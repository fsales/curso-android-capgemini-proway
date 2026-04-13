package com.fsales.app.rumo.ui.feature.gasto.cadastro

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fsales.app.rumo.R
import com.fsales.app.rumo.core.domain.model.CategoriaGasto
import com.fsales.app.rumo.ui.CadastroGastoUiEvent
import com.fsales.app.rumo.ui.components.RumoDatePickerField
import com.fsales.app.rumo.ui.feature.gasto.components.CategoriaGastoDropdown
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.spacing
import java.time.LocalDate

// =============================================================================
// Screen — ponto de entrada; coleta ViewModel e roteia UiEvent
// =============================================================================
@Composable
fun CadastroGastoScreen(
    viewModel: CadastroGastoViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                CadastroGastoUiEvent.NavigateBack -> navigateBack()
                CadastroGastoUiEvent.ErroAoSalvar -> snackbarHostState.showSnackbar(
                    context.getString(R.string.cadastro_gasto_erro_salvar)
                )
            }
        }
    }

    CadastroGastoContent(
        uiState           = uiState,
        snackbarHostState = snackbarHostState,
        onEvent           = viewModel::onEvent,
    )
}

// =============================================================================
// Content — renderiza estado puro; sem ViewModel
// =============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroGastoContent(
    uiState: CadastroGastoUiState = CadastroGastoUiState(dataGasto = LocalDate.now()),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onEvent: (CadastroGastoEvent) -> Unit = {},
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.cadastro_gasto_titulo)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(CadastroGastoEvent.Voltar) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.acao_voltar),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = MaterialTheme.spacing.medium, vertical = MaterialTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        ) {
            // Descrição
            OutlinedTextField(
                value = uiState.descricao,
                onValueChange = { onEvent(CadastroGastoEvent.AlterarDescricao(it)) },
                label = { Text(stringResource(R.string.cadastro_gasto_campo_descricao)) },
                isError = uiState.erros.containsKey(CadastroGastoViewModel.ERRO_DESCRICAO),
                supportingText = uiState.erros[CadastroGastoViewModel.ERRO_DESCRICAO]?.let { msg ->
                    { Text(msg, color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            // Valor
            OutlinedTextField(
                value = uiState.valorTexto,
                onValueChange = { onEvent(CadastroGastoEvent.AlterarValor(it)) },
                label = { Text(stringResource(R.string.cadastro_gasto_campo_valor)) },
                isError = uiState.erros.containsKey(CadastroGastoViewModel.ERRO_VALOR),
                supportingText = uiState.erros[CadastroGastoViewModel.ERRO_VALOR]?.let { msg ->
                    { Text(msg, color = MaterialTheme.colorScheme.error) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                prefix = { Text("R$") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            // Data do gasto
            RumoDatePickerField(
                data = uiState.dataGasto,
                onDateSelected = { onEvent(CadastroGastoEvent.AlterarData(it)) },
                label = stringResource(R.string.cadastro_gasto_campo_data),
                modifier = Modifier.fillMaxWidth(),
            )

            // Categoria
            CategoriaGastoDropdown(
                categoriaSelecionada = uiState.categoria,
                onCategoriaSelecionada = { onEvent(CadastroGastoEvent.AlterarCategoria(it)) },
                modifier = Modifier.fillMaxWidth(),
            )

            // Essencial
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.cadastro_gasto_campo_essencial),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Switch(
                    checked = uiState.essencial,
                    onCheckedChange = { onEvent(CadastroGastoEvent.AlterarEssencial(it)) },
                )
            }

            // Recorrente
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.cadastro_gasto_campo_recorrente),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Switch(
                    checked = uiState.recorrente,
                    onCheckedChange = { onEvent(CadastroGastoEvent.AlterarRecorrente(it)) },
                )
            }

            // Observação (opcional)
            OutlinedTextField(
                value = uiState.observacao,
                onValueChange = { onEvent(CadastroGastoEvent.AlterarObservacao(it)) },
                label = { Text(stringResource(R.string.cadastro_gasto_campo_observacao)) },
                minLines = 2,
                modifier = Modifier.fillMaxWidth(),
            )

            // Botão Salvar
            Button(
                onClick = { onEvent(CadastroGastoEvent.Salvar) },
                enabled = !uiState.salvando,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (uiState.salvando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text(stringResource(R.string.acao_salvar))
                }
            }
        }
    }
}

// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "CadastroGasto · Vazio · Light")
@Preview(showBackground = true, name = "CadastroGasto · Vazio · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CadastroGastoVazioPreview() {
    RumoTheme { CadastroGastoContent() }
}

@Preview(showBackground = true, name = "CadastroGasto · Com erros · Light")
@Composable
private fun CadastroGastoComErrosPreview() {
    RumoTheme {
        CadastroGastoContent(
            uiState = CadastroGastoUiState(
                dataGasto = LocalDate.now(),
                erros = mapOf(
                    CadastroGastoViewModel.ERRO_DESCRICAO to "A descrição é obrigatória.",
                    CadastroGastoViewModel.ERRO_VALOR to "Informe um valor maior que zero.",
                ),
            ),
        )
    }
}

@Preview(showBackground = true, name = "CadastroGasto · Salvando · Light")
@Composable
private fun CadastroGastoSalvandoPreview() {
    RumoTheme {
        CadastroGastoContent(
            uiState = CadastroGastoUiState(
                descricao = "Aluguel",
                valorTexto = "1500.00",
                categoria = CategoriaGasto.MORADIA,
                essencial = true,
                recorrente = true,
                dataGasto = LocalDate.of(2026, 4, 5),
                salvando = true,
            ),
        )
    }
}