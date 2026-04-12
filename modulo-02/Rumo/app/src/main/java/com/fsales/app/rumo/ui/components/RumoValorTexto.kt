package com.fsales.app.rumo.ui.components

import android.content.res.Configuration
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.ui.theme.RumoTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

// =============================================================================
// Formata BigDecimal em moeda BRL — ex: R$ 1.234,56
// Reutilizado em cards de Ganho, Gasto e Sonho
// =============================================================================

private val formatadorBRL: NumberFormat
    get() = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

fun BigDecimal.formatarBRL(): String = formatadorBRL.format(this)

@Composable
fun RumoValorTexto(
    valor: BigDecimal,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = LocalContentColor.current,
) {
    Text(
        text = valor.formatarBRL(),
        style = style,
        color = color,
        modifier = modifier,
    )
}

@Preview(showBackground = true, name = "RumoValorTexto · Light")
@Preview(showBackground = true, name = "RumoValorTexto · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RumoValorTextoPreview() {
    RumoTheme {
        RumoValorTexto(valor = BigDecimal("1234.56"))
    }
}


