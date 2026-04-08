package com.fsales.app.e_aluno.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.e_aluno.R
import com.fsales.app.e_aluno.domain.model.Aluno
import com.fsales.app.e_aluno.ui.mapper.toUiText
import com.fsales.app.e_aluno.ui.preview.PreviewData
import com.fsales.app.e_aluno.ui.theme.AppTheme
import com.fsales.app.e_aluno.ui.theme.spacing

private const val ContentWeight = 1f

@Composable
fun AlunoItem(
    aluno: Aluno,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onItemClick
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.spacing.small,
                    vertical = MaterialTheme.spacing.medium
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AlunoAvatar(
                nome = aluno.nome,
                fotoUrl = aluno.fotoUrl
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

            Column(modifier = Modifier.weight(ContentWeight)) {
                Text(
                    text = "${aluno.nome} ${aluno.sobrenome}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

                Text(
                    text = "${aluno.matricula} • ${aluno.curso} • ${aluno.semestre.toUiText()} • ${aluno.turno.toUiText()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textDecoration = if (aluno.ativo) TextDecoration.None else TextDecoration.LineThrough,
                    maxLines = 1
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.aluno_item_open_details, aluno.nome),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Previews para AlunoItem — um ativo e outro inativo, usando dados de PreviewData */
@Preview(showBackground = true, name = "Aluno Ativo – Light")
@Preview(showBackground = true, name = "Aluno Ativo – Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AlunoItemAtivoPreview() {
    AppTheme {
        AlunoItem(
            PreviewData.alunos.first { it.ativo },
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Aluno Inativo – Light")
@Preview(showBackground = true, name = "Aluno Inativo – Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AlunoItemInativoPreview() {
    AppTheme {
        AlunoItem(
            PreviewData.alunos.first { !it.ativo },
            onItemClick = {}
        )
    }
}
