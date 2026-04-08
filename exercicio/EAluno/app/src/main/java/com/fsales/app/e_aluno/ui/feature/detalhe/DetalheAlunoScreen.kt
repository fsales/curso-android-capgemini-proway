package com.fsales.app.e_aluno.ui.feature.detalhe

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.CircularProgressIndicator
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
import com.fsales.app.e_aluno.ui.DetalhesUiEvent
import com.fsales.app.e_aluno.ui.components.AlunoCard
import com.fsales.app.e_aluno.ui.components.DetailField
import com.fsales.app.e_aluno.ui.mapper.toUiText
import com.fsales.app.e_aluno.ui.preview.PreviewData
import com.fsales.app.e_aluno.ui.theme.AppTheme
import com.fsales.app.e_aluno.ui.theme.spacing
import java.time.format.DateTimeFormatter

private const val BIRTH_DATE_PATTERN = "dd/MM/yyyy"
private val BIRTH_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(BIRTH_DATE_PATTERN)

@Composable
fun DetalheAlunoScreen(
    id: Long,
    navigateBack: () -> Unit,
    viewModel: DetalheAlunoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.load(id)
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            if (event is DetalhesUiEvent.NavigateBack) {
                navigateBack()
            }
        }
    }

    DetalheAlunoContent(
        uiState = uiState,
        onBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheAlunoContent(
    uiState: DetalheAlunoUiState = DetalheAlunoUiState(),
    onBack: () -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DetalheAlunoTopBar(
                scrollBehavior = scrollBehavior,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

        when {
            uiState.isLoading -> {
                DetalheAlunoLoading(modifier = contentModifier)
            }

            uiState.isNotFound || uiState.aluno == null -> {
                DetalheAlunoNotFound(modifier = contentModifier)
            }

            else -> {
                DetalheAlunoBody(
                    aluno = uiState.aluno,
                    modifier = contentModifier
                        .padding(MaterialTheme.spacing.large)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetalheAlunoTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.detalhe_aluno_navigate_back)
                )
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.School,
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text(stringResource(R.string.detalhe_aluno_screen_title))
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun DetalheAlunoLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DetalheAlunoNotFound(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.detalhe_aluno_not_found),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun DetalheAlunoBody(
    aluno: Aluno,
    modifier: Modifier = Modifier
) {
    val detailFields = listOf(
        DetailFieldData(R.string.detalhe_aluno_field_id, aluno.id.toString()),
        DetailFieldData(R.string.detalhe_aluno_field_nome, aluno.nome),
        DetailFieldData(R.string.detalhe_aluno_field_sobrenome, aluno.sobrenome),
        DetailFieldData(R.string.detalhe_aluno_field_email, aluno.email),
        DetailFieldData(R.string.detalhe_aluno_field_telefone, aluno.telefone),
        DetailFieldData(R.string.detalhe_aluno_field_matricula, aluno.matricula),
        DetailFieldData(R.string.detalhe_aluno_field_curso, aluno.curso),
        DetailFieldData(R.string.detalhe_aluno_field_turno, aluno.turno.toUiText()),
        DetailFieldData(R.string.detalhe_aluno_field_semestre, aluno.semestre.toUiText()),
        DetailFieldData(
            R.string.detalhe_aluno_field_data_nascimento,
            aluno.dataNascimento.format(BIRTH_DATE_FORMATTER)
        )
    )

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        item {
            AlunoCard(
                aluno = aluno,
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(
            items = detailFields,
            key = DetailFieldData::labelRes
        ) { field ->
            DetailField(
                labelRes = field.labelRes,
                value = field.value
            )
        }
    }
}

private data class DetailFieldData(
    val labelRes: Int,
    val value: String
)

/** preview */
@Preview(showBackground = true, name = "Detalhe Aluno - Light")
@Preview(showBackground = true, name = "Detalhe Aluno - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DetalheAlunoScreenPreview() {
    AppTheme {
        DetalheAlunoContent(
            uiState = DetalheAlunoUiState(aluno = PreviewData.alunos.first())
        )
    }
}
