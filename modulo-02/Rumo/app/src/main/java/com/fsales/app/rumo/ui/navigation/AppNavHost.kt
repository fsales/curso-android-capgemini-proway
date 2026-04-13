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
import com.fsales.app.rumo.ui.feature.sonho.detalhe.DetalheSonhoScreen
import kotlinx.serialization.Serializable

@Serializable
data class HomeRoute(val abaInicial: AbaInicial = AbaInicial.GANHOS) : NavKey

@Serializable
data class CadastroGanhoRoute(val abaOrigem: AbaInicial = AbaInicial.GANHOS) : NavKey

@Serializable
data class CadastroGastoRoute(val abaOrigem: AbaInicial = AbaInicial.GASTOS) : NavKey

@Serializable
data class CadastroSonhoRoute(val abaOrigem: AbaInicial = AbaInicial.SONHOS) : NavKey

@Serializable
data class DetalheSonhoRoute(val sonhoId: Long) : NavKey

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
                    onNavigateToGanhoCadastro = { backStack.add(CadastroGanhoRoute(AbaInicial.GANHOS)) },
                    onNavigateToGastoCadastro = { backStack.add(CadastroGastoRoute(AbaInicial.GASTOS)) },
                    onNavigateToSonhoCadastro = { backStack.add(CadastroSonhoRoute(AbaInicial.SONHOS)) },
                    onNavigateToSonhoDetalhe  = { sonhoId -> backStack.add(DetalheSonhoRoute(sonhoId)) },
                )
            }
            entry<CadastroGanhoRoute> { route ->
                CadastroGanhoScreen(
                    navigateBack = {
                        backStack.removeLastOrNull()
                        val topo = backStack.lastOrNull()
                        if (topo !is HomeRoute || topo.abaInicial != route.abaOrigem) {
                            backStack.add(HomeRoute(route.abaOrigem))
                        }
                    },
                )
            }
            entry<CadastroGastoRoute> { route ->
                CadastroGastoScreen(
                    navigateBack = {
                        backStack.removeLastOrNull()
                        val topo = backStack.lastOrNull()
                        if (topo !is HomeRoute || topo.abaInicial != route.abaOrigem) {
                            backStack.add(HomeRoute(route.abaOrigem))
                        }
                    },
                )
            }
            entry<CadastroSonhoRoute> { route ->
                CadastroSonhoScreen(
                    navigateBack = {
                        backStack.removeLastOrNull()
                        val topo = backStack.lastOrNull()
                        if (topo !is HomeRoute || topo.abaInicial != route.abaOrigem) {
                            backStack.add(HomeRoute(route.abaOrigem))
                        }
                    },
                )
            }
            entry<DetalheSonhoRoute> { route ->
                DetalheSonhoScreen(
                    sonhoId      = route.sonhoId,
                    navigateBack = { backStack.removeLastOrNull() },
                )
            }
        }
    )
}

