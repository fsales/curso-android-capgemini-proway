package com.fsales.app.rumo.ui.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Savings
import androidx.compose.ui.graphics.vector.ImageVector
import com.fsales.app.rumo.core.domain.model.TipoGanho

// =============================================================================
// Mapeia TipoGanho para apresentação na UI.
// Mantém lógica de apresentação fora do domínio.
// =============================================================================

fun TipoGanho.toIcon(): ImageVector = when (this) {
    TipoGanho.SALARIO      -> Icons.Filled.AccountBalance
    TipoGanho.RENDA_EXTRA  -> Icons.Filled.Payments
    TipoGanho.INVESTIMENTO -> Icons.AutoMirrored.Filled.TrendingUp
    TipoGanho.PRESENTE     -> Icons.Filled.CardGiftcard
    TipoGanho.OUTRO        -> Icons.Filled.Savings
}

/**
 * Descrição acessível do tipo de ganho para contentDescription de ícones.
 * Delega ao [TipoGanho.descricao] que já é a fonte de verdade do domínio.
 * Se o app precisar de i18n no futuro, converter para @StringRes aqui.
 */
fun TipoGanho.toContentDescription(): String = descricao

