package com.fsales.app.rumo.ui.feature.ganho.cadastro

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fsales.app.rumo.R
import com.fsales.app.rumo.core.domain.model.GanhoErro
import com.fsales.app.rumo.core.domain.model.TipoGanho
import com.fsales.app.rumo.ui.components.CurrencyVisualTransformation
import com.fsales.app.rumo.ui.components.RumoDatePickerField
import com.fsales.app.rumo.ui.feature.ganho.components.TipoGanhoDropdown
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.spacing
import java.time.LocalDate

// =============================================================================
// Mapeamento de GanhoErro → @StringRes (privado ao arquivo)
// =============================================================================
@androidx.annotation.StringRes
private fun GanhoErro.toStringRes(): Int = when (this) {
    GanhoErro.DescricaoObrigatoria   -> R.string.cadastro_ganho_erro_descricao_obrigatoria
    GanhoErro.ValorInvalido          -> R.string.cadastro_ganho_erro_valor_invalido
    GanhoErro.DataForaDeCompetencia,
    GanhoErro.CompetenciaInvalida    -> R.string.cadastro_ganho_erro_data_invalida
}

// =============================================================================
// Screen — ponto de entrada; coleta ViewModel e roteia UiEvent
// =============================================================================
@Composable
fun CadastroGanhoScreen(
    ganhoId: Long? = null,
    navigateBack: () -> Unit,
    viewModel: CadastroGanhoViewModel = hiltViewModel<CadastroGanhoViewModel, CadastroGanhoViewModel.Factory>(
        creationCallback = { factory -> factory.create(ganhoId) }
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val erroSalvarMsg = stringResource(R.string.cadastro_ganho_erro_salvar)

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                CadastroGanhoUiEvent.NavigateBack -> navigateBack()
                CadastroGanhoUiEvent.ErroAoSalvar -> snackbarHostState.showSnackbar(erroSalvarMsg)
            }
        }
    }

    LaunchedEffect(ganhoId) {
        if (ganhoId != null) viewModel.carregarParaEdicao(ganhoId)
        else viewModel.resetar()
    }

    CadastroGanhoContent(
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
fun CadastroGanhoContent(
    uiState: CadastroGanhoUiState = CadastroGanhoUiState(dataRecebimento = LocalDate.now()),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onEvent: (CadastroGanhoEvent) -> Unit = {},
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.labelSmall,
                        )
                        Text(
                            text = stringResource(R.string.cadastro_ganho_titulo),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(CadastroGanhoEvent.Voltar) }) {
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
                onValueChange = { onEvent(CadastroGanhoEvent.AlterarDescricao(it)) },
                label = { Text(stringResource(R.string.cadastro_ganho_campo_descricao)) },
                isError = uiState.erroDescricao != null,
                supportingText = uiState.erroDescricao?.let { erro ->
                    { Text(stringResource(erro.toStringRes()), color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            // Valor
            val currencyTransformation = remember { CurrencyVisualTransformation() }
            OutlinedTextField(
                value = uiState.valorTexto,
                onValueChange = { onEvent(CadastroGanhoEvent.AlterarValor(it)) },
                label = { Text(stringResource(R.string.cadastro_ganho_campo_valor)) },
                isError = uiState.erroValor != null,
                supportingText = uiState.erroValor?.let { erro ->
                    { Text(stringResource(erro.toStringRes()), color = MaterialTheme.colorScheme.error) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = currencyTransformation,
                prefix = { Text("R$") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            // Data de recebimento
            RumoDatePickerField(
                data = uiState.dataRecebimento,
                onDateSelected = { onEvent(CadastroGanhoEvent.AlterarData(it)) },
                label = stringResource(R.string.cadastro_ganho_campo_data),
                erro = uiState.erroData?.let { stringResource(it.toStringRes()) },
                modifier = Modifier.fillMaxWidth(),
            )

            // Tipo
            TipoGanhoDropdown(
                tipoSelecionado = uiState.tipo,
                onTipoSelecionado = { onEvent(CadastroGanhoEvent.AlterarTipo(it)) },
                modifier = Modifier.fillMaxWidth(),
            )

            // Recorrente
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.cadastro_ganho_campo_recorrente),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Switch(
                    checked = uiState.recorrente,
                    onCheckedChange = { onEvent(CadastroGanhoEvent.AlterarRecorrente(it)) },
                )
            }

            // Observação (opcional)
            OutlinedTextField(
                value = uiState.observacao,
                onValueChange = { onEvent(CadastroGanhoEvent.AlterarObservacao(it)) },
                label = { Text(stringResource(R.string.cadastro_ganho_campo_observacao)) },
                minLines = 2,
                modifier = Modifier.fillMaxWidth(),
            )

            // Botão Salvar
            Button(
                onClick = { onEvent(CadastroGanhoEvent.Salvar) },
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
@Preview(showBackground = true, name = "CadastroGanho · Vazio · Light")
@Preview(showBackground = true, name = "CadastroGanho · Vazio · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CadastroGanhoVazioPreview() {
    RumoTheme { CadastroGanhoContent() }
}

@Preview(showBackground = true, name = "CadastroGanho · Com erros · Light")
@Composable
private fun CadastroGanhoComErrosPreview() {
    RumoTheme {
        CadastroGanhoContent(
            uiState = CadastroGanhoUiState(
                dataRecebimento = LocalDate.now(),
                erroDescricao = GanhoErro.DescricaoObrigatoria,
                erroValor = GanhoErro.ValorInvalido,
            ),
        )
    }
}

@Preview(showBackground = true, name = "CadastroGanho · Salvando · Light")
@Composable
private fun CadastroGanhoSalvandoPreview() {
    RumoTheme {
        CadastroGanhoContent(
            uiState = CadastroGanhoUiState(
                descricao = "Salário",
                valorTexto = "5000.00",
                tipo = TipoGanho.SALARIO,
                recorrente = true,
                dataRecebimento = LocalDate.of(2026, 4, 5),
                salvando = true,
            ),
        )
    }
}