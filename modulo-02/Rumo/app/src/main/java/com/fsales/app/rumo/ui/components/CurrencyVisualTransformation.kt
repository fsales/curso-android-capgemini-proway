package com.fsales.app.rumo.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * VisualTransformation que exibe String de dígitos (centavos) no formato BRL.
 *
 * Armazenamento: "150050"  → dígitos puros representando centavos
 * Exibição:      "1.500,50"
 *
 * Uso:
 *   OutlinedTextField(
 *       keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
 *       visualTransformation = remember { CurrencyVisualTransformation() },
 *   )
 *
 * Converter para BigDecimal no ViewModel via String.centavosParaBigDecimal().
 */
class CurrencyVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }
        val formatted = formatarComoMoeda(digits)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // clamp to real digit count so empty-input padding never causes over-indexing
                val clampedOffset = offset.coerceIn(0, digits.length)
                var digitsCount = 0
                for (i in formatted.indices) {
                    if (digitsCount == clampedOffset) return i
                    if (formatted[i].isDigit()) digitsCount++
                }
                return formatted.length
            }

            override fun transformedToOriginal(offset: Int): Int =
                // coerce to [0, digits.length] so padding zeros added by formatarComoMoeda
                // (when input is empty) are never counted as real original characters
                formatted.take(offset).count { it.isDigit() }.coerceIn(0, digits.length)
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }

    private fun formatarComoMoeda(digitos: String): String {
        val preenchido = digitos.padStart(3, '0')
        val parteInteira = preenchido.dropLast(2).trimStart('0').ifEmpty { "0" }
        val parteCentavos = preenchido.takeLast(2)
        val parteInteiraFormatada = parteInteira.reversed().chunked(3)
            .joinToString(".").reversed()
        return "$parteInteiraFormatada,$parteCentavos"
    }
}
