package com.fsales.app.e_aluno.ui.feature.lista

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class ListaAlunoScreenState internal constructor(
    val listState: LazyListState,
) {
    var ativosExpanded by mutableStateOf(true)
        private set

    var inativosExpanded by mutableStateOf(true)
        private set

    private enum class Section { ATIVOS, INATIVOS }

    fun toggleAtivos() = toggleSection(Section.ATIVOS)

    fun toggleInativos() = toggleSection(Section.INATIVOS)

    private fun toggleSection(section: Section) {
        when (section) {
            Section.ATIVOS -> {
                if (!ativosExpanded) {
                    ativosExpanded = true
                } else {
                    if (!inativosExpanded) inativosExpanded = true
                    ativosExpanded = false
                }
            }
            Section.INATIVOS -> {
                if (!inativosExpanded) {
                    inativosExpanded = true
                } else {
                    if (!ativosExpanded) ativosExpanded = true
                    inativosExpanded = false
                }
            }
        }
    }

    suspend fun scrollToTop(animated: Boolean = true) {
        if (animated) listState.animateScrollToItem(0) else listState.scrollToItem(0)
    }
}

@Composable
fun rememberListaAlunoScreenState(): ListaAlunoScreenState {
    val listState = rememberLazyListState()
    return remember(listState) {
        ListaAlunoScreenState(listState = listState)
    }
}
