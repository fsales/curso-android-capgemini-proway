package com.fsales.app.rumo.ui.feature.ganho.detalhe

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fsales.app.rumo.R
import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.TipoGanho
import com.fsales.app.rumo.ui.components.RumoErroState
import com.fsales.app.rumo.ui.components.RumoInfoBadge
import com.fsales.app.rumo.ui.components.formatarBRL
import com.fsales.app.rumo.ui.mapper.formatarDataUI
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.spacing
import java.math.BigDecimal
import java.time.LocalDate

// =============================================================================
// Screen — ponto de entrada, consome ViewModel e roteia UiEvent
// =============================================================================
@Composable
fun DetalheGanhoScreen(
    ganhoId: Long,
    navigateBack: () -> Unit,
    onNavigateToCadastro: (ganhoId: Long) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: DetalheGanhoViewModel = hiltViewModel<DetalheGanhoViewModel, DetalheGanhoViewModel.Factory>(
        creationCallback = { factory -> factory.create(ganhoId) }
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                DetalheGanhoUiEvent.NavigateBack -> navigateBack()
                is DetalheGanhoUiEvent.NavigateToCadastro -> onNavigateToCadastro(event.ganhoId)
            }
        }
    }

    DetalheGanhoContent(
        modifier = modifier,
        uiState  = uiState,
        onEvent  = viewModel::onEvent,
    )
}

// =============================================================================
// Content — renderiza estado puro; sem ViewModel
// =============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheGanhoContent(
    modifier: Modifier = Modifier,
    uiState: DetalheGanhoUiState = DetalheGanhoUiState(),
    onEvent: (DetalheGanhoEvent) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.labelSmall,
                        )
                        Text(
                            text = stringResource(R.string.detalhe_ganho_titulo),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(DetalheGanhoEvent.Voltar) }) {
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
        when {
            uiState.carregando -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator() }
            }

            uiState.erro != null -> {
                RumoErroState(
                    mensagem = uiState.erro,
                    onRetry = { onEvent(DetalheGanhoEvent.Voltar) },
                    modifier = Modifier.padding(paddingValues),
                )
            }

            uiState.ganho != null -> {
                DetalheGanhoBody(
                    ganho = uiState.ganho,
                    excluindo = uiState.excluindo,
                    onEvent = onEvent,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                )
            }
        }
    }

    // AlertDialog de confirmação
    if (uiState.dialogConfirmacao != null) {
        AlertDialog(
            onDismissRequest = { onEvent(DetalheGanhoEvent.CancelarDialog) },
            title = { Text(stringResource(R.string.detalhe_ganho_confirmar_excluir_titulo)) },
            text = { Text(stringResource(R.string.detalhe_ganho_confirmar_excluir_mensagem)) },
            confirmButton = {
                TextButton(onClick = { onEvent(DetalheGanhoEvent.ConfirmarExclusao) }) {
                    Text(
                        text = stringResource(R.string.detalhe_ganho_excluir),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onEvent(DetalheGanhoEvent.CancelarDialog) }) {
                    Text(stringResource(R.string.acao_cancelar))
                }
            },
        )
    }
}

@Composable
private fun DetalheGanhoBody(
    ganho: Ganho,
    excluindo: Boolean,
    onEvent: (DetalheGanhoEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = MaterialTheme.spacing.medium, vertical = MaterialTheme.spacing.large),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        // Valor em destaque
        Text(
            text = ganho.valor.formatarBRL(),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        HorizontalDivider()

        // Campos
        DetalheCampo(label = stringResource(R.string.cadastro_ganho_campo_descricao), valor = ganho.descricao)
        DetalheCampo(label = stringResource(R.string.cadastro_ganho_campo_data), valor = ganho.dataRecebimento.formatarDataUI())
        DetalheCampo(label = stringResource(R.string.cadastro_ganho_campo_tipo), valor = ganho.tipo.descricao)

        ganho.observacao?.takeIf { it.isNotBlank() }?.let { observacao ->
            DetalheCampo(label = stringResource(R.string.cadastro_ganho_campo_observacao), valor = observacao)
        }

        // Badges
        if (ganho.recorrente) {
            Row { RumoInfoBadge(label = stringResource(R.string.ganho_recorrente)) }
        }

        HorizontalDivider()

        // Ações
        Button(
            onClick = { onEvent(DetalheGanhoEvent.Editar) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Filled.Edit, contentDescription = null)
            Text(
                text = stringResource(R.string.detalhe_ganho_editar),
                modifier = Modifier.padding(start = MaterialTheme.spacing.small),
            )
        }

        OutlinedButton(
            onClick = { onEvent(DetalheGanhoEvent.ExcluirEste) },
            enabled = !excluindo,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error,
            ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (excluindo) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(end = MaterialTheme.spacing.small),
                    strokeWidth = androidx.compose.ui.unit.Dp(2f),
                )
            } else {
                Icon(Icons.Filled.Delete, contentDescription = null)
            }
            Text(
                text = stringResource(R.string.detalhe_ganho_excluir),
                modifier = Modifier.padding(start = MaterialTheme.spacing.small),
            )
        }
    }
}

@Composable
private fun DetalheCampo(
    label: String,
    valor: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

// =============================================================================
// Previews
// =============================================================================
private val ganhoFakeDetalhe = Ganho(
    id = 1L,
    descricao = "Salário",
    valor = BigDecimal("5000.00"),
    dataRecebimento = LocalDate.of(2026, 4, 5),
    mesReferencia = 4,
    anoReferencia = 2026,
    tipo = TipoGanho.SALARIO,
    recorrente = true,
)

@Preview(showBackground = true, name = "DetalheGanho · Light")
@Preview(showBackground = true, name = "DetalheGanho · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetalheGanhoPreenchidoPreview() {
    RumoTheme {
        DetalheGanhoContent(
            uiState = DetalheGanhoUiState(ganho = ganhoFakeDetalhe, carregando = false),
        )
    }
}

@Preview(showBackground = true, name = "DetalheGanho · Dialog Excluir · Light")
@Composable
private fun DetalheGanhoDialogPreview() {
    RumoTheme {
        DetalheGanhoContent(
            uiState = DetalheGanhoUiState(
                ganho = ganhoFakeDetalhe,
                carregando = false,
                dialogConfirmacao = TipoConfirmacaoGanho.ExcluirEste,
            ),
        )
    }
}
