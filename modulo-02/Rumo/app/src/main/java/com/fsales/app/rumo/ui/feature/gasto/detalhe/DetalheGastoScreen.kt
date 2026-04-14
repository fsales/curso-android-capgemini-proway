package com.fsales.app.rumo.ui.feature.gasto.detalhe

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
import com.fsales.app.rumo.core.domain.model.CategoriaGasto
import com.fsales.app.rumo.core.domain.model.Gasto
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
fun DetalheGastoScreen(
    gastoId: Long,
    navigateBack: () -> Unit,
    onNavigateToCadastro: (gastoId: Long) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: DetalheGastoViewModel = hiltViewModel<DetalheGastoViewModel, DetalheGastoViewModel.Factory>(
        creationCallback = { factory -> factory.create(gastoId) }
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                DetalheGastoUiEvent.NavigateBack -> navigateBack()
                is DetalheGastoUiEvent.NavigateToCadastro -> onNavigateToCadastro(event.gastoId)
            }
        }
    }

    DetalheGastoContent(
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
fun DetalheGastoContent(
    modifier: Modifier = Modifier,
    uiState: DetalheGastoUiState = DetalheGastoUiState(),
    onEvent: (DetalheGastoEvent) -> Unit = {},
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
                            text = stringResource(R.string.detalhe_gasto_titulo),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(DetalheGastoEvent.Voltar) }) {
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
                    onRetry = { onEvent(DetalheGastoEvent.Voltar) },
                    modifier = Modifier.padding(paddingValues),
                )
            }

            uiState.gasto != null -> {
                DetalheGastoBody(
                    gasto = uiState.gasto,
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
            onDismissRequest = { onEvent(DetalheGastoEvent.CancelarDialog) },
            title = { Text(stringResource(R.string.detalhe_gasto_confirmar_excluir_titulo)) },
            text = { Text(stringResource(R.string.detalhe_gasto_confirmar_excluir_mensagem)) },
            confirmButton = {
                TextButton(onClick = { onEvent(DetalheGastoEvent.ConfirmarExclusao) }) {
                    Text(
                        text = stringResource(R.string.detalhe_gasto_excluir),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onEvent(DetalheGastoEvent.CancelarDialog) }) {
                    Text(stringResource(R.string.acao_cancelar))
                }
            },
        )
    }
}

@Composable
private fun DetalheGastoBody(
    gasto: Gasto,
    excluindo: Boolean,
    onEvent: (DetalheGastoEvent) -> Unit,
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
            text = gasto.valor.formatarBRL(),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error,
        )

        HorizontalDivider()

        // Campos
        DetalheCampo(label = stringResource(R.string.cadastro_gasto_campo_descricao), valor = gasto.descricao)
        DetalheCampo(label = stringResource(R.string.cadastro_gasto_campo_data), valor = gasto.dataGasto.formatarDataUI())
        DetalheCampo(label = stringResource(R.string.cadastro_gasto_campo_categoria), valor = gasto.categoria.descricao)

        gasto.observacao?.takeIf { it.isNotBlank() }?.let { observacao ->
            DetalheCampo(label = stringResource(R.string.cadastro_gasto_campo_observacao), valor = observacao)
        }

        // Badges
        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
            if (gasto.essencial) RumoInfoBadge(label = stringResource(R.string.gasto_essencial))
            if (gasto.recorrente) RumoInfoBadge(label = stringResource(R.string.gasto_recorrente))
        }

        HorizontalDivider()

        // Ações
        Button(
            onClick = { onEvent(DetalheGastoEvent.Editar) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Filled.Edit, contentDescription = null)
            Text(
                text = stringResource(R.string.detalhe_gasto_editar),
                modifier = Modifier.padding(start = MaterialTheme.spacing.small),
            )
        }

        OutlinedButton(
            onClick = { onEvent(DetalheGastoEvent.ExcluirEste) },
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
                text = stringResource(R.string.detalhe_gasto_excluir),
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
private val gastoFakeDetalhe = Gasto(
    id = 1L,
    descricao = "Aluguel",
    valor = BigDecimal("1500.00"),
    dataGasto = LocalDate.of(2026, 4, 5),
    mesReferencia = 4,
    anoReferencia = 2026,
    categoria = CategoriaGasto.MORADIA,
    essencial = true,
    recorrente = true,
    observacao = "Pagamento mensal",
)

@Preview(showBackground = true, name = "DetalheGasto · Light")
@Preview(showBackground = true, name = "DetalheGasto · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetalheGastoPreenchidoPreview() {
    RumoTheme {
        DetalheGastoContent(
            uiState = DetalheGastoUiState(gasto = gastoFakeDetalhe, carregando = false),
        )
    }
}

@Preview(showBackground = true, name = "DetalheGasto · Carregando · Light")
@Composable
private fun DetalheGastoCarregandoPreview() {
    RumoTheme {
        DetalheGastoContent(uiState = DetalheGastoUiState(carregando = true))
    }
}

@Preview(showBackground = true, name = "DetalheGasto · Dialog Excluir · Light")
@Composable
private fun DetalheGastoDialogPreview() {
    RumoTheme {
        DetalheGastoContent(
            uiState = DetalheGastoUiState(
                gasto = gastoFakeDetalhe,
                carregando = false,
                dialogConfirmacao = TipoConfirmacaoGasto.ExcluirEste,
            ),
        )
    }
}
