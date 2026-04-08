package com.fsales.app.e_aluno.ui.feature.lista

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fsales.app.e_aluno.R
import com.fsales.app.e_aluno.domain.model.Aluno
import com.fsales.app.e_aluno.ui.ListUiEvent
import com.fsales.app.e_aluno.ui.UiEvent
import com.fsales.app.e_aluno.ui.components.AlunoItem
import com.fsales.app.e_aluno.ui.components.ListaSectionHeader
import com.fsales.app.e_aluno.ui.preview.PreviewData
import com.fsales.app.e_aluno.ui.theme.AppTheme
import com.fsales.app.e_aluno.ui.theme.spacing

@Composable
fun ListaAlunoScreen(
    viewModel: ListaAlunoViewModel = hiltViewModel(),
    onNavigateToDetalhes: (id: Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ListUiEvent.NavigateToDetalhes -> onNavigateToDetalhes(event.id)
                is UiEvent.ShowSnackbar -> TODO()
            }
        }
    }

    ListaAlunoContent(
        ativos = uiState.ativos,
        inativos = uiState.inativos,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListaAlunoContent(
    ativos: List<Aluno>,
    inativos: List<Aluno>,
    onEvent: (ListaEvento) -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val isEmpty = ativos.isEmpty() && inativos.isEmpty()
    val screenState = rememberListaAlunoScreenState()
    val scope = rememberCoroutineScope()
    val showScrollToTop by remember(screenState.listState) {
        derivedStateOf {
            screenState.listState.firstVisibleItemIndex > 0 ||
                screenState.listState.firstVisibleItemScrollOffset > 0
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ListaAlunoTopBar(
                scrollBehavior = scrollBehavior,
                showScrollToTop = showScrollToTop,
                onScrollToTop = { scope.launch { screenState.scrollToTop() } }
            )
        }
    ) { paddingValues ->
        if (isEmpty) {
            ListaAlunoEmpty(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
            return@Scaffold
        }

        LazyColumn(
            state = screenState.listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(all = MaterialTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            alunoSection(
                headerTitleRes = R.string.lista_aluno_categoria_ativos,
                alunos = ativos,
                expanded = screenState.ativosExpanded,
                onToggleExpanded = screenState::toggleAtivos,
                onEvent = onEvent
            )
            alunoSection(
                headerTitleRes = R.string.lista_aluno_categoria_inativos,
                alunos = inativos,
                expanded = screenState.inativosExpanded,
                onToggleExpanded = screenState::toggleInativos,
                onEvent = onEvent
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.alunoSection(
    @StringRes headerTitleRes: Int,
    alunos: List<Aluno>,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    onEvent: (ListaEvento) -> Unit
) {
    if (alunos.isEmpty()) return

    val headerKey = "header_$headerTitleRes"

    stickyHeader(key = headerKey, contentType = "header") {
        ListaSectionHeader(
            titleRes = headerTitleRes,
            count = alunos.size,
            expanded = expanded,
            onToggle = onToggleExpanded,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = MaterialTheme.spacing.large)
        )
    }

    if (!expanded) return

    items(alunos, key = { it.id }, contentType = { "aluno" }) { aluno ->
        AlunoItem(
            aluno = aluno,
            modifier = Modifier.animateItem(),
            onItemClick = { onEvent(ListaEvento.Detalhes(aluno.id)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListaAlunoTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    showScrollToTop: Boolean,
    onScrollToTop: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.School,
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text(stringResource(R.string.lista_aluno_screen_title))
            }
        },
        actions = {
            AnimatedVisibility(
                visible = showScrollToTop,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = onScrollToTop) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.lista_aluno_voltar_topo)
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}


@Composable
fun ListaAlunoEmpty(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            Text(
                text = stringResource(R.string.lista_aluno_empty),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

/** Previews */
@Preview(showBackground = true)
@Composable
private fun ListaAlunoPreview() {
    AppTheme {
        ListaAlunoContent(
            ativos = PreviewData.alunos.filter { it.ativo },
            inativos = PreviewData.alunos.filterNot { it.ativo }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaAlunoEmptyPreview() {
    AppTheme {
        ListaAlunoContent(
            ativos = emptyList(),
            inativos = emptyList()
        )
    }
}