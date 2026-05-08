package com.fsales.app.smartcontact.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.fsales.app.smartcontact.ui.feature.lista.ListScreen
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
object ListRoute : NavKey

@Serializable
data class EditarAdicionarRoute(
    val id: Long?,
    // UUID gerado na hora da navegação garante que cada entrada seja única.
    // Sem isso, AddEditRoute(null) == AddEditRoute(null) (data class equality)
    // e o Nav3 reutilizaria o mesmo ViewModelStoreOwner — trazendo os dados da sessão anterior.
    val entryId: String
) : NavKey


@Composable
fun SmartContactNavHost() {

    val backStack = rememberNavBackStack(ListRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<ListRoute> {
                ListScreen(
                    onNavigateToAddEdit = { id ->
                        backStack.add(EditarAdicionarRoute(id = null, entryId = UUID.randomUUID().toString()))
                    }
                )
            }
        }
    )
}