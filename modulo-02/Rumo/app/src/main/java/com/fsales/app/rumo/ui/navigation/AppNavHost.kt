package com.fsales.app.rumo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.fsales.app.rumo.ui.feature.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute : NavKey

@Composable
fun AppNavHost() {

    val backStack = rememberNavBackStack(HomeRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<HomeRoute> {
                HomeScreen()
            }
        }
    )
}