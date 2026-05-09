package com.fsales.app.smartcontact.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * [VisualTransformation] genérica baseada em máscara de caracteres.
 *
 * Cada `#` na [mask] representa um dígito do texto de entrada.
 * Os demais caracteres são inseridos como separadores fixos.
 *
 * Exemplos de masks:
 *  - CEP:      "#####-###"
 *  - Telefone: "(##) #####-####"  ou  "(##) ####-####"
 */
class MaskVisualTransformation(private val mask: String) : VisualTransformation {

    private val slots = mask.count { it == '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.take(slots)
        val masked = buildString {
            var digitIndex = 0
            for (ch in mask) {
                if (digitIndex >= digits.length) break
                if (ch == '#') {
                    append(digits[digitIndex++])
                } else {
                    append(ch)
                }
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformedOffset = 0
                var consumed = 0
                for (ch in mask) {
                    if (consumed >= offset) break
                    if (ch == '#') consumed++
                    transformedOffset++
                }
                return transformedOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                var originalOffset = 0
                var i = 0
                for (ch in mask) {
                    if (i >= offset) break
                    if (ch == '#') originalOffset++
                    i++
                }
                return originalOffset
            }
        }

        return TransformedText(AnnotatedString(masked), offsetMapping)
    }

    companion object {
        /** Máscara para CEP brasileiro: 00000-000 */
        val CEP = MaskVisualTransformation("#####-###")

        /** Máscara para telefone celular: (00) 00000-0000 */
        val CELULAR = MaskVisualTransformation("(##) #####-####")

        /** Máscara para telefone fixo: (00) 0000-0000 */
        val FIXO = MaskVisualTransformation("(##) ####-####")
    }
}

