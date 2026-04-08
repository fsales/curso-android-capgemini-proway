package com.fsales.app.e_aluno.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fsales.app.e_aluno.R
import com.fsales.app.e_aluno.domain.model.Aluno
import com.fsales.app.e_aluno.ui.preview.PreviewData
import com.fsales.app.e_aluno.ui.theme.AppTheme
import com.fsales.app.e_aluno.ui.theme.spacing

@Composable
fun AlunoCard(
    aluno: Aluno,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            AlunoAvatar(
                nome = aluno.nome,
                fotoUrl = aluno.fotoUrl,
                modifier = Modifier.size(72.dp)
            )
            Text(
                text = "${aluno.nome} ${aluno.sobrenome}",
                style = MaterialTheme.typography.titleLarge
            )
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = if (aluno.ativo) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.errorContainer
                }
            ) {
                Text(
                    text = if (aluno.ativo) {
                        stringResource(R.string.detalhe_aluno_status_ativo)
                    } else {
                        stringResource(R.string.detalhe_aluno_status_inativo)
                    },
                    modifier = Modifier.padding(
                        horizontal = MaterialTheme.spacing.small,
                        vertical = MaterialTheme.spacing.extraSmall
                    ),
                    color = if (aluno.ativo) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    },
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlunoCardPreview() {
    AppTheme {
        AlunoCard(
            aluno = PreviewData.alunos.first(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

