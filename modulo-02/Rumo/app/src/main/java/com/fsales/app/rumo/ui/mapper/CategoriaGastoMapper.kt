package com.fsales.app.rumo.ui.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector
import com.fsales.app.rumo.core.domain.model.CategoriaGasto

// =============================================================================
// Mapeia CategoriaGasto para apresentação na UI.
// Mantém lógica de apresentação fora do domínio.
// =============================================================================

fun CategoriaGasto.toIcon(): ImageVector = when (this) {
    CategoriaGasto.MORADIA         -> Icons.Filled.Home
    CategoriaGasto.ALIMENTACAO     -> Icons.Filled.Fastfood
    CategoriaGasto.TRANSPORTE      -> Icons.Filled.DirectionsBus
    CategoriaGasto.SAUDE           -> Icons.Filled.FitnessCenter
    CategoriaGasto.EDUCACAO        -> Icons.Filled.School
    CategoriaGasto.LAZER           -> Icons.Filled.SportsEsports
    CategoriaGasto.CONTAS          -> Icons.Filled.AccountBalance
    CategoriaGasto.OUTROS          -> Icons.Filled.MoreHoriz
    CategoriaGasto.SONHO_REALIZADO -> Icons.Filled.AutoAwesome
}

fun CategoriaGasto.toContentDescription(): String = descricao

