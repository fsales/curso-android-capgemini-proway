package com.fsales.app.rumo.ui.feature.sonho.cadastro
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fsales.app.rumo.R
import com.fsales.app.rumo.core.domain.model.PrioridadeSonho
import com.fsales.app.rumo.ui.CadastroSonhoUiEvent
import com.fsales.app.rumo.ui.components.RumoDatePickerField
import com.fsales.app.rumo.ui.theme.RumoTheme
import com.fsales.app.rumo.ui.theme.spacing
import java.time.LocalDate
// =============================================================================
// Screen — ponto de entrada; coleta ViewModel e roteia UiEvent
// =============================================================================
@Composable
fun CadastroSonhoScreen(
    viewModel: CadastroSonhoViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                CadastroSonhoUiEvent.NavigateBack -> navigateBack()
                CadastroSonhoUiEvent.ErroAoSalvar -> snackbarHostState.showSnackbar(
                    context.getString(R.string.cadastro_sonho_erro_salvar),
                )
            }
        }
    }
    CadastroSonhoContent(
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
fun CadastroSonhoContent(
    uiState: CadastroSonhoUiState = CadastroSonhoUiState(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onEvent: (CadastroSonhoEvent) -> Unit = {},
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.cadastro_sonho_titulo)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(CadastroSonhoEvent.Voltar) }) {
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
            // Titulo
            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = { onEvent(CadastroSonhoEvent.AlterarTitulo(it)) },
                label = { Text(stringResource(R.string.cadastro_sonho_campo_titulo)) },
                isError = uiState.erros.containsKey(CadastroSonhoViewModel.ERRO_TITULO),
                supportingText = uiState.erros[CadastroSonhoViewModel.ERRO_TITULO]?.let { msg ->
                    { Text(msg, color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            // Descricao (opcional)
            OutlinedTextField(
                value = uiState.descricao,
                onValueChange = { onEvent(CadastroSonhoEvent.AlterarDescricao(it)) },
                label = { Text(stringResource(R.string.cadastro_sonho_campo_descricao)) },
                minLines = 2,
                modifier = Modifier.fillMaxWidth(),
            )
            // Valor meta
            OutlinedTextField(
                value = uiState.valorMetaTexto,
                onValueChange = { onEvent(CadastroSonhoEvent.AlterarValorMeta(it)) },
                label = { Text(stringResource(R.string.cadastro_sonho_campo_valor_meta)) },
                isError = uiState.erros.containsKey(CadastroSonhoViewModel.ERRO_VALOR_META),
                supportingText = uiState.erros[CadastroSonhoViewModel.ERRO_VALOR_META]?.let { msg ->
                    { Text(msg, color = MaterialTheme.colorScheme.error) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                prefix = { Text("R$") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            // Prioridade
            PrioridadeDropdown(
                prioridadeSelecionada   = uiState.prioridade,
                onPrioridadeSelecionada = { onEvent(CadastroSonhoEvent.AlterarPrioridade(it)) },
                modifier = Modifier.fillMaxWidth(),
            )
            // Prazo alvo (opcional)
            RumoDatePickerField(
                data           = uiState.prazoAlvo ?: LocalDate.now(),
                onDateSelected = { onEvent(CadastroSonhoEvent.AlterarPrazo(it)) },
                label          = stringResource(R.string.cadastro_sonho_campo_prazo),
                modifier       = Modifier.fillMaxWidth(),
            )
            // Botao Salvar
            Button(
                onClick  = { onEvent(CadastroSonhoEvent.Salvar) },
                enabled  = !uiState.salvando,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (uiState.salvando) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color       = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text(stringResource(R.string.acao_salvar))
                }
            }
        }
    }
}
// =============================================================================
// Dropdown privado de PrioridadeSonho
// =============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrioridadeDropdown(
    prioridadeSelecionada: PrioridadeSonho,
    onPrioridadeSelecionada: (PrioridadeSonho) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expandido by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded         = expandido,
        onExpandedChange = { expandido = it },
        modifier         = modifier,
    ) {
        OutlinedTextField(
            value         = prioridadeSelecionada.descricao,
            onValueChange = {},
            readOnly      = true,
            label         = { Text(stringResource(R.string.cadastro_sonho_campo_prioridade)) },
            trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier      = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(
            expanded         = expandido,
            onDismissRequest = { expandido = false },
        ) {
            PrioridadeSonho.entries.forEach { prioridade ->
                DropdownMenuItem(
                    text    = { Text(prioridade.descricao) },
                    onClick = {
                        onPrioridadeSelecionada(prioridade)
                        expandido = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
// =============================================================================
// Previews
// =============================================================================
@Preview(showBackground = true, name = "CadastroSonho - Vazio - Light")
@Preview(showBackground = true, name = "CadastroSonho - Vazio - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CadastroSonhoVazioPreview() {
    RumoTheme { CadastroSonhoContent() }
}
@Preview(showBackground = true, name = "CadastroSonho - Com erros - Light")
@Composable
private fun CadastroSonhoComErrosPreview() {
    RumoTheme {
        CadastroSonhoContent(
            uiState = CadastroSonhoUiState(
                erros = mapOf(
                    CadastroSonhoViewModel.ERRO_TITULO     to "O titulo e obrigatorio.",
                    CadastroSonhoViewModel.ERRO_VALOR_META to "Informe um valor meta maior que zero.",
                ),
            ),
        )
    }
}