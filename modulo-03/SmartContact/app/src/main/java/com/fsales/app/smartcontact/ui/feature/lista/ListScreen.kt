@file:OptIn(ExperimentalMaterial3Api::class)
package com.fsales.app.smartcontact.ui.feature.lista

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fsales.app.smartcontact.R
import com.fsales.app.smartcontact.ui.components.EmptyState
import com.fsales.app.smartcontact.ui.components.SmartContactScaffold
import com.fsales.app.smartcontact.ui.theme.SmartContactTheme

@Composable
fun ListScreen(
    onNavigateToAddEdit: (id: Long) -> Unit
) {

    ListContent()
}

@Composable
fun ListContent(

    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {

    // MD3: LargeTopAppBar colapsa à medida que o usuário rola a lista
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    SmartContactScaffold(
        title = stringResource(R.string.titulo_lista_contatos),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scrollBehavior = scrollBehavior,
        snackbarHostState = snackbarHostState,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(R.string.fab_contato)) },
                onClick = { /* TODO: Navegar para AddEditScreen */ },
                icon = { Icon(Icons.Default.Add, contentDescription = null) }
            )
        }
    ){ paddingValues ->
        EmptyState(
            title = stringResource(R.string.titulo_estado_vazio),
            message = stringResource(R.string.mensagem_estado_vazio),
            icon = Icons.Default.PersonOutline,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        )

    }
}

@Preview(showBackground = true, name = "List – Light")
@Preview(showBackground = true, name = "List – Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListContentPreview(

) {
    SmartContactTheme {
        ListContent()
    }
}

@Preview(showBackground = true, name = "Empty – Light")
@Preview(showBackground = true, name = "Empty – Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EmptyStatePreview() {
    SmartContactTheme {
        ListContent()
    }
}

