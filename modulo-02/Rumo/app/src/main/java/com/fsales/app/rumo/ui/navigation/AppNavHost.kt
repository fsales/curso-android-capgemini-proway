package com.fsales.app.rumo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.fsales.app.rumo.ui.feature.ganho.cadastro.CadastroGanhoScreen
import com.fsales.app.rumo.ui.feature.gasto.cadastro.CadastroGastoScreen
import com.fsales.app.rumo.ui.feature.home.HomeScreen
import com.fsales.app.rumo.ui.feature.sonho.cadastro.CadastroSonhoScreen
import kotlinx.serialization.Serializable

@Serializable
data class HomeRoute(val abaInicial: AbaInicial = AbaInicial.GANHOS) : NavKey

@Serializable
object CadastroGanhoRoute : NavKey

@Serializable
object CadastroGastoRoute : NavKey

@Serializable
object CadastroSonhoRoute : NavKey

@Composable
fun AppNavHost() {

    val backStack = rememberNavBackStack(HomeRoute())

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<HomeRoute> { route ->
                HomeScreen(
                    abaInicial                = route.abaInicial.toHomeEvent(),
                    onNavigateToGanhoCadastro = { backStack.add(CadastroGanhoRoute) },
                    onNavigateToGastoCadastro = { backStack.add(CadastroGastoRoute) },
                    onNavigateToSonhoCadastro = { backStack.add(CadastroSonhoRoute) },
                )
            }
            entry<CadastroGanhoRoute> {
                CadastroGanhoScreen(
                    navigateBack = { backStack.removeLastOrNull() },
                )
            }
            entry<CadastroGastoRoute> {
                CadastroGastoScreen(
                    navigateBack = {
                        backStack.removeLastOrNull()
                        backStack.add(HomeRoute(abaInicial = AbaInicial.GASTOS))
                    },
                )
            }
            entry<CadastroSonhoRoute> {
                CadastroSonhoScreen(
                    navigateBack = {
                        backStack.removeLastOrNull()
                        backStack.add(HomeRoute(abaInicial = AbaInicial.SONHOS))
                    },
                )
            }
        }
    )
}



