package com.fsales.app.rumo.ui.util

import java.math.BigDecimal

/**
 * Converte uma String digitada pelo usuário em um campo monetário para [BigDecimal].
 *
 * Campos de formulário Android com `KeyboardType.Decimal` enviam o separador decimal
 * do locale do dispositivo — vírgula em pt-BR/de-DE, ponto em en-US.
 * Durante a digitação **não** há separador de milhar, portanto a substituição
 * de ',' → '.' é segura e suficiente para todos os locales.
 *
 * Exemplos aceitos:
 *   "0,01"  → 0.01   (pt-BR, de-DE)
 *   "0.01"  → 0.01   (en-US)
 *   "1500"  → 1500   (qualquer locale)
 *   "1500,5"→ 1500.5 (pt-BR)
 *
 * Retorna `null` se a string for vazia ou não representar um número válido.
 */
fun String.toBigDecimalOuNulo(): BigDecimal? =
    this.trim()
        .replace(',', '.')
        .toBigDecimalOrNull()







