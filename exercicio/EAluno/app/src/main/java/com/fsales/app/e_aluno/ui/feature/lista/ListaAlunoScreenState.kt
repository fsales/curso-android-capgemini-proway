package com.fsales.app.e_aluno.ui.feature.lista

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Stable
class ListaAlunoScreenState internal constructor(
    val listState: LazyListState,
    val ativosExpanded: Boolean,
    val inativosExpanded: Boolean,
    private val onAtivosExpandedChange: (Boolean) -> Unit,
    private val onInativosExpandedChange: (Boolean) -> Unit
) {
    private enum class Section { ATIVOS, INATIVOS }

    fun toggleAtivos() = toggleSection(Section.ATIVOS)

    fun toggleInativos() = toggleSection(Section.INATIVOS)

    private fun toggleSection(section: Section) {
        val (currentExpanded, setCurrentExpanded, otherExpanded, setOtherExpanded) =
            when (section) {
                Section.ATIVOS -> Quad(
                    ativosExpanded,
                    onAtivosExpandedChange,
                    inativosExpanded,
                    onInativosExpandedChange
                )

                Section.INATIVOS -> Quad(
                    inativosExpanded,
                    onInativosExpandedChange,
                    ativosExpanded,
                    onAtivosExpandedChange
                )
            }

        if (!currentExpanded) {
            setCurrentExpanded(true)
            return
        }

        if (!otherExpanded) setOtherExpanded(true)
        setCurrentExpanded(false)
    }

    suspend fun scrollToTop(animated: Boolean = true) {
        if (animated) listState.animateScrollToItem(0) else listState.scrollToItem(0)
    }

    private data class Quad(
        val currentExpanded: Boolean,
        val setCurrentExpanded: (Boolean) -> Unit,
        val otherExpanded: Boolean,
        val setOtherExpanded: (Boolean) -> Unit
    )
}

@Composable
fun rememberListaAlunoScreenState(): ListaAlunoScreenState {
    val listState = rememberLazyListState()
    var ativosExpanded by rememberSaveable { mutableStateOf(true) }
    var inativosExpanded by rememberSaveable { mutableStateOf(true) }

    return remember(listState, ativosExpanded, inativosExpanded) {
        ListaAlunoScreenState(
            listState = listState,
            ativosExpanded = ativosExpanded,
            inativosExpanded = inativosExpanded,
            onAtivosExpandedChange = { ativosExpanded = it },
            onInativosExpandedChange = { inativosExpanded = it }
        )
    }
}
