package com.fsales.app.rumo.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// =============================================================================
// Scaffold para telas de listagem com FAB opcional.
// contentWindowInsets = WindowInsets(0) evita conflito com o HomeScaffold pai.
// =============================================================================
@Composable
fun RumoListaScaffold(
    modifier: Modifier = Modifier,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = { floatingActionButton?.invoke() },
        floatingActionButtonPosition = FabPosition.End,
        contentWindowInsets = WindowInsets(0),
        content = content,
    )
}

