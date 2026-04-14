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

/**
 * Converte uma String de dígitos (centavos) em [BigDecimal] com escala 2.
 *
 * Usada em conjunto com [CurrencyVisualTransformation], onde o usuário digita
 * apenas algarismos e o campo armazena centavos. Exemplo: "150050" → 1500.50.
 *
 * Retorna `null` se a string for vazia, resultado for zero ou não for um número válido.
 */
fun String.centavosParaBigDecimal(): BigDecimal? {
    val digits = this.filter { it.isDigit() }
    if (digits.isEmpty()) return null
    val value = digits.toLongOrNull() ?: return null
    if (value == 0L) return null
    return BigDecimal(value).movePointLeft(2)
}

/**
 * Converte um [BigDecimal] em string de dígitos de centavos.
 * Exemplo: 1500.50 → "150050"
 * Usada para pré-preencher campos monetários no modo de edição.
 */
fun BigDecimal.toDigitosCentavos(): String =
    this.movePointRight(2).toLong().toString()







