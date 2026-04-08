package com.fsales.app.e_aluno.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Tokens de espaçamento baseados no grid de 4dp do Material Design 3.
 * Acesse via MaterialTheme.spacing.large
 */
data class Spacing(
    val extraSmall: Dp = 4.dp,   // separador mínimo
    val small: Dp = 8.dp,        // entre itens de lista
    val medium: Dp = 12.dp,      // padding vertical de cards
    val large: Dp = 16.dp,       // margem padrão de tela (MD3 screen margin)
    val extraLarge: Dp = 24.dp   // separação entre seções
)

val LocalSpacing = compositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current
