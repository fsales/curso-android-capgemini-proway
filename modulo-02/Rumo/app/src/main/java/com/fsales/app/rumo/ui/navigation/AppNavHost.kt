package com.fsales.app.rumo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.fsales.app.rumo.ui.feature.ganho.cadastro.CadastroGanhoScreen
import com.fsales.app.rumo.ui.feature.ganho.detalhe.DetalheGanhoScreen
import com.fsales.app.rumo.ui.feature.gasto.cadastro.CadastroGastoScreen
import com.fsales.app.rumo.ui.feature.gasto.detalhe.DetalheGastoScreen
import com.fsales.app.rumo.ui.feature.home.HomeScreen
import com.fsales.app.rumo.ui.feature.sonho.cadastro.CadastroSonhoScreen
import com.fsales.app.rumo.ui.feature.sonho.detalhe.DetalheSonhoScreen
import kotlinx.serialization.Serializable

@Serializable
data class HomeRoute(val abaInicial: AbaInicial = AbaInicial.HOME) : NavKey

@Serializable
data class CadastroGanhoRoute(val abaOrigem: AbaInicial = AbaInicial.GANHOS, val ganhoId: Long? = null) : NavKey

@Serializable
data class DetalheGanhoRoute(val ganhoId: Long) : NavKey

@Serializable
data class CadastroGastoRoute(val abaOrigem: AbaInicial = AbaInicial.GASTOS, val gastoId: Long? = null) : NavKey

@Serializable
data class DetalheGastoRoute(val gastoId: Long) : NavKey

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
                    onNavigateToGanhoDetalhe  = { id -> backStack.add(DetalheGanhoRoute(id)) },
                    onNavigateToGastoCadastro = { backStack.add(CadastroGastoRoute(AbaInicial.GASTOS)) },
                    onNavigateToGastoDetalhe  = { id -> backStack.add(DetalheGastoRoute(id)) },
                    onNavigateToSonhoCadastro = { backStack.add(CadastroSonhoRoute(AbaInicial.SONHOS)) },
                    onNavigateToSonhoDetalhe  = { sonhoId -> backStack.add(DetalheSonhoRoute(sonhoId)) },
                )
            }
            entry<CadastroGanhoRoute> { route ->
                CadastroGanhoScreen(
                    ganhoId = route.ganhoId,
                    navigateBack = {
                        backStack.removeLastOrNull()
                        val topo = backStack.lastOrNull()
                        if (topo !is HomeRoute || topo.abaInicial != route.abaOrigem) {
                            backStack.add(HomeRoute(route.abaOrigem))
                        }
                    },
                )
            }
            entry<DetalheGanhoRoute> { route ->
                DetalheGanhoScreen(
                    ganhoId = route.ganhoId,
                    navigateBack = {
                        backStack.removeLastOrNull()
                        val topo = backStack.lastOrNull()
                        if (topo !is HomeRoute || topo.abaInicial != AbaInicial.GANHOS) {
                            backStack.add(HomeRoute(AbaInicial.GANHOS))
                        }
                    },
                    onNavigateToCadastro = { id ->
                        backStack.add(CadastroGanhoRoute(abaOrigem = AbaInicial.GANHOS, ganhoId = id))
                    },
                )
            }
            entry<CadastroGastoRoute> { route ->
                CadastroGastoScreen(
                    gastoId = route.gastoId,
                    navigateBack = {
                        backStack.removeLastOrNull()
                        val topo = backStack.lastOrNull()
                        if (topo !is HomeRoute || topo.abaInicial != route.abaOrigem) {
                            backStack.add(HomeRoute(route.abaOrigem))
                        }
                    },
                )
            }
            entry<DetalheGastoRoute> { route ->
                DetalheGastoScreen(
                    gastoId = route.gastoId,
                    navigateBack = {
                        backStack.removeLastOrNull()
                        val topo = backStack.lastOrNull()
                        if (topo !is HomeRoute || topo.abaInicial != AbaInicial.GASTOS) {
                            backStack.add(HomeRoute(AbaInicial.GASTOS))
                        }
                    },
                    onNavigateToCadastro = { id ->
                        backStack.add(CadastroGastoRoute(abaOrigem = AbaInicial.GASTOS, gastoId = id))
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
                    navigateBack = {
                        backStack.removeLastOrNull()
                        val topo = backStack.lastOrNull()
                        if (topo !is HomeRoute || topo.abaInicial != AbaInicial.SONHOS) {
                            backStack.add(HomeRoute(AbaInicial.SONHOS))
                        }
                    },
                )
            }
        }
    )
}

