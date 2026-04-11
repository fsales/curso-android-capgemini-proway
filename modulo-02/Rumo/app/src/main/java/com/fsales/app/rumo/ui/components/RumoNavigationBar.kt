package com.fsales.app.rumo.ui.components

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.rumo.R
import com.fsales.app.rumo.ui.feature.home.HomeEvent
import com.fsales.app.rumo.ui.theme.RumoTheme

data class RumoNavItem(
    @param:StringRes val labelRes: Int,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector,
    val evento: HomeEvent,
)

val rumoNavItems = listOf(
    RumoNavItem(
        labelRes     = R.string.nav_ganhos,
        iconFilled   = Icons.Filled.Savings,
        iconOutlined = Icons.Outlined.Savings,
        evento       = HomeEvent.IrParaGanhos,
    ),
    RumoNavItem(
        labelRes     = R.string.nav_gastos,
        iconFilled   = Icons.Filled.ShoppingCart,
        iconOutlined = Icons.Outlined.ShoppingCart,
        evento       = HomeEvent.IrParaGastos,
    ),
    RumoNavItem(
        labelRes     = R.string.nav_sonhos,
        iconFilled   = Icons.Filled.AutoAwesome,
        iconOutlined = Icons.Outlined.AutoAwesome,
        evento       = HomeEvent.IrParaSonhos,
    ),
)

@Composable
fun RumoNavigationBar(
    selectedEvent: HomeEvent?,
    onItemSelected: (HomeEvent) -> Unit,
    items: List<RumoNavItem> = rumoNavItems,
) {
    NavigationBar {
        items.forEach { item ->
            val selecionado = selectedEvent == item.evento
            val label = stringResource(id = item.labelRes)
            NavigationBarItem(
                selected = selecionado,
                onClick = { onItemSelected(item.evento) },
                icon = {
                    Icon(
                        imageVector = if (selecionado) item.iconFilled else item.iconOutlined,
                        contentDescription = label,
                    )
                },
                label = { Text(text = label) },
            )
        }
    }
}

@Preview(showBackground = true, name = "NavigationBar · Light")
@Preview(showBackground = true, name = "NavigationBar · Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RumoNavigationBarPreview() {
    RumoTheme {
        RumoNavigationBar(
            selectedEvent = HomeEvent.IrParaGanhos,
            onItemSelected = {},
        )
    }
}
