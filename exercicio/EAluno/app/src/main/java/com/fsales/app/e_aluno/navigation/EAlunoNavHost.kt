package com.fsales.app.e_aluno.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.fsales.app.e_aluno.ui.feature.detalhe.DetalheAlunoScreen
import com.fsales.app.e_aluno.ui.feature.lista.ListaAlunoScreen
import kotlinx.serialization.Serializable

@Serializable
object ListaRoute : NavKey

@Serializable
data class DetalheRoute(val id: Long) : NavKey


@Composable
fun EAlunoNavHost() {

    val backStack = rememberNavBackStack(ListaRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<ListaRoute> {
                ListaAlunoScreen(
                    onNavigateToDetalhes = { id ->
                        backStack.add(DetalheRoute(id = id))
                    }
                )
            }
            entry<DetalheRoute> { key ->
                DetalheAlunoScreen(
                    id = key.id,
                    navigateBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}