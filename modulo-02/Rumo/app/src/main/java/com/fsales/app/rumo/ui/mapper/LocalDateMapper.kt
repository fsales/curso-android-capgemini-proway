package com.fsales.app.rumo.ui.mapper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// =============================================================================
// Formatadores de datas para exibição na UI — centralizado para reuso em
// Ganho, Gasto e Sonho.
// =============================================================================

/**
 * Formata LocalDate no padrão numérico curto do locale informado.
 * Ex (pt-BR): 05/04/2026 | Ex (en-US): 4/5/2026
 */
fun LocalDate.formatarParaLocale(locale: Locale): String =
    DateTimeFormatter.ofPattern("dd/MM/yyyy", locale).format(this)

/**
 * Versão @Composable que lê o locale do dispositivo via [LocalConfiguration].
 * Reage automaticamente a mudanças de idioma do sistema Android.
 * Ex: dispositivo em pt-BR → "05/04/2026"; em en-US → "05/04/2026" (formato fixo dd/MM/yyyy).
 */
@Composable
@ReadOnlyComposable
fun LocalDate.formatarDataUI(): String {
    val locale = LocalConfiguration.current.locales[0]
    return formatarParaLocale(locale)
}

