package com.fsales.app.rumo.ui.feature.sonho.detalhe

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fsales.app.rumo.R
import com.fsales.app.rumo.core.domain.model.PrioridadeSonho
import com.fsales.app.rumo.core.domain.model.ProjecaoSonho
import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.ui.components.RumoErroState
import com.fsales.app.rumo.ui.components.RumoInfoBadge
import com.fsales.app.rumo.ui.components.formatarBRL
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.spacing
import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

// =============================================================================
// Screen — ponto de entrada, consome ViewModel e roteia UiEvent
// =============================================================================
@Composable
fun DetalheSonhoScreen(
    sonhoId: Long,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetalheSonhoViewModel = hiltViewModel<DetalheSonhoViewModel, DetalheSonhoViewModel.Factory>(
        creationCallback = { factory -> factory.create(sonhoId) }
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                DetalheSonhoUiEvent.NavigateBack -> navigateBack()
            }
        }
    }

    DetalheSonhoContent(
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
fun DetalheSonhoContent(
    modifier: Modifier = Modifier,
    uiState: DetalheSonhoUiState = DetalheSonhoUiState(),
    onEvent: (DetalheSonhoEvent) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.detalhe_sonho_titulo)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(DetalheSonhoEvent.Voltar) }) {
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
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.erro != null -> {
                RumoErroState(
                    mensagem = uiState.erro,
                    onRetry  = { onEvent(DetalheSonhoEvent.Voltar) },
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                )
            }

            uiState.sonho != null -> {
                DetalheSonhoBody(
                    sonho    = uiState.sonho,
                    projecao = uiState.projecao,
                    onEvent  = onEvent,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                )
            }
        }

        if (uiState.exibirDialogoConclusao) {
            DialogoConclusao(
                onConfirmar = { onEvent(DetalheSonhoEvent.ConfirmarConclusao) },
                onCancelar  = { onEvent(DetalheSonhoEvent.CancelarConclusao) },
            )
        }
    }
}

// =============================================================================
// Corpo principal com dados do sonho + projeção
// =============================================================================
@Composable
private fun DetalheSonhoBody(
    sonho: Sonho,
    projecao: ProjecaoSonho?,
    onEvent: (DetalheSonhoEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        // ------------------------------------------------------------------
        // Título + badge concluído
        // ------------------------------------------------------------------
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            if (sonho.concluido) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = MaterialTheme.spacing.extraSmall),
                )
            }
            Text(
                text = sonho.titulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
        }

        if (sonho.concluido) {
            RumoInfoBadge(
                label = stringResource(R.string.sonho_realizado),
                icone = Icons.Filled.CheckCircle,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }

        // ------------------------------------------------------------------
        // Descrição (opcional)
        // ------------------------------------------------------------------
        if (!sonho.descricao.isNullOrBlank()) {
            Text(
                text = sonho.descricao.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        HorizontalDivider()

        // ------------------------------------------------------------------
        // Linha: Meta
        // ------------------------------------------------------------------
        DetalheRow(
            rotulo = stringResource(R.string.sonho_meta, ""),
            valor  = sonho.valorMeta.formatarBRL(),
        )

        // ------------------------------------------------------------------
        // Linha: Prioridade
        // ------------------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.cadastro_sonho_campo_prioridade),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            RumoInfoBadge(
                label = sonho.prioridade.descricao,
                icone = Icons.Filled.Star,
                containerColor = when (sonho.prioridade) {
                    PrioridadeSonho.ALTA  -> MaterialTheme.colorScheme.errorContainer
                    PrioridadeSonho.MEDIA -> MaterialTheme.colorScheme.tertiaryContainer
                    PrioridadeSonho.BAIXA -> MaterialTheme.colorScheme.secondaryContainer
                },
                contentColor = when (sonho.prioridade) {
                    PrioridadeSonho.ALTA  -> MaterialTheme.colorScheme.onErrorContainer
                    PrioridadeSonho.MEDIA -> MaterialTheme.colorScheme.onTertiaryContainer
                    PrioridadeSonho.BAIXA -> MaterialTheme.colorScheme.onSecondaryContainer
                },
            )
        }

        // ------------------------------------------------------------------
        // Linha: Prazo alvo
        // ------------------------------------------------------------------
        sonho.prazoAlvo?.let { prazo ->
            DetalheRow(
                rotulo = stringResource(R.string.cadastro_sonho_campo_prazo),
                valor  = prazo.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
            )
        }
        // Linha: Data de criação
        DetalheRow(
            rotulo = stringResource(R.string.cadastro_sonho_campo_data_criacao),
            valor  = sonho.dataCriacao.atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
        )

        HorizontalDivider()

        // ------------------------------------------------------------------
        // Projeção financeira (apenas para sonhos ativos)
        // ------------------------------------------------------------------
        if (!sonho.concluido && projecao != null) {
            Text(
                text = stringResource(R.string.detalhe_sonho_titulo_projecao),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            DetalheRow(
                rotulo = stringResource(R.string.sonho_saldo_mensal, ""),
                valor  = projecao.saldoMensal.formatarBRL(),
            )

            val mesesText = projecao.mesesNecessarios
                ?.let { pluralStringResource(R.plurals.sonho_meses_necessarios, it, it) }
                ?: stringResource(R.string.sonho_sem_projecao)
            DetalheRow(
                rotulo = stringResource(R.string.detalhe_sonho_prazo_estimado),
                valor  = mesesText,
            )

            projecao.seraAlcancadoNoPrazo?.let { noPrazo ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text  = stringResource(R.string.detalhe_sonho_indicador_prazo),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    val tint = if (noPrazo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                    ) {
                        Icon(
                            imageVector = if (noPrazo) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                            contentDescription = null,
                            tint = tint,
                            modifier = Modifier.padding(end = MaterialTheme.spacing.extraSmall),
                        )
                        Text(
                            text  = stringResource(if (noPrazo) R.string.sonho_no_prazo else R.string.sonho_fora_do_prazo),
                            style = MaterialTheme.typography.bodyMedium,
                            color = tint,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }

            HorizontalDivider()
        }

        // ------------------------------------------------------------------
        // Botão "Marcar como realizado" — só para sonhos ativos e com projeção executada
        // ------------------------------------------------------------------
        val podeMarcarComoRealizado = !sonho.concluido && projecao?.mesesNecessarios != null
        if (podeMarcarComoRealizado) {
            Button(
                onClick  = { onEvent(DetalheSonhoEvent.Concluir) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.padding(end = MaterialTheme.spacing.small),
                )
                Text(stringResource(R.string.detalhe_sonho_marcar_realizado))
            }
        }
    }
}

// =============================================================================
// Row genérica de rótulo + valor
// =============================================================================
@Composable
private fun DetalheRow(
    rotulo: String,
    valor: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text  = rotulo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text       = valor,
            style      = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}

// =============================================================================
// Diálogo de confirmação de conclusão
// =============================================================================
@Composable
private fun DialogoConclusao(
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        icon = {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        title = { Text(stringResource(R.string.sonho_dialogo_conclusao_titulo)) },
        text  = { Text(stringResource(R.string.sonho_dialogo_conclusao_mensagem)) },
        confirmButton = {
            Button(onClick = onConfirmar) {
                Text(stringResource(R.string.sonho_dialogo_confirmar))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text(stringResource(R.string.acao_cancelar))
            }
        },
    )
}

// =============================================================================
// Previews
// =============================================================================
private val sonhoPreviewAtivo = Sonho(
    id        = 1L,
    titulo    = "Carro novo",
    descricao = "Toyota Corolla 2027",
    valorMeta = BigDecimal("80000.00"),
    prioridade = PrioridadeSonho.ALTA,
)

private val projecaoPreview = ProjecaoSonho(
    sonho                = sonhoPreviewAtivo,
    saldoMensal          = BigDecimal("3055.55"),
    mesesNecessarios     = 27,
    seraAlcancadoNoPrazo = true,
)

private val sonhoPreviewConcluido = Sonho(
    id        = 2L,
    titulo    = "Notebook novo",
    valorMeta = BigDecimal("5000.00"),
    prioridade = PrioridadeSonho.MEDIA,
    concluido = true,
)

@Preview(showBackground = true, name = "DetalheSonho · Ativo · Light")
@Preview(showBackground = true, name = "DetalheSonho · Ativo · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetalheSonhoAtivoPreview() {
    RumoTheme {
        DetalheSonhoContent(
            uiState = DetalheSonhoUiState(
                sonho    = sonhoPreviewAtivo,
                projecao = projecaoPreview,
                carregando = false,
            ),
        )
    }
}

@Preview(showBackground = true, name = "DetalheSonho · Concluído · Light")
@Preview(showBackground = true, name = "DetalheSonho · Concluído · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetalheSonhoConcluidoPreview() {
    RumoTheme {
        DetalheSonhoContent(
            uiState = DetalheSonhoUiState(
                sonho    = sonhoPreviewConcluido,
                projecao = null,
                carregando = false,
            ),
        )
    }
}

@Preview(showBackground = true, name = "DetalheSonho · Diálogo · Light")
@Composable
private fun DetalheSonhoDialogoPreview() {
    RumoTheme {
        DetalheSonhoContent(
            uiState = DetalheSonhoUiState(
                sonho    = sonhoPreviewAtivo,
                projecao = projecaoPreview,
                carregando = false,
                exibirDialogoConclusao = true,
            ),
        )
    }
}
