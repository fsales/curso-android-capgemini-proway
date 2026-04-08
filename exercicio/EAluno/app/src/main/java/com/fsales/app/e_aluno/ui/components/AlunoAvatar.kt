package com.fsales.app.e_aluno.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.fsales.app.e_aluno.ui.theme.AppTheme

private val AvatarSize = 48.dp

@Composable
fun AlunoAvatar(
    nome: String,
    fotoUrl: String?,
    modifier: Modifier = Modifier
) {
    if (fotoUrl != null) {
        AsyncImage(
            model = fotoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(AvatarSize)
                .clip(CircleShape)
        )
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(AvatarSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                text = nome.first().uppercase(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview(showBackground = true, name = "Avatar com inicial")
@Composable
private fun AlunoAvatarSemFotoPreview() {
    AppTheme {
        AlunoAvatar(nome = "João", fotoUrl = null)
    }
}

