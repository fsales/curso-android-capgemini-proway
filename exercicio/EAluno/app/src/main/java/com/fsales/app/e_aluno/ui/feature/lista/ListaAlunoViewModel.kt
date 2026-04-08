package com.fsales.app.e_aluno.ui.feature.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.e_aluno.domain.AlunoRepository
import com.fsales.app.e_aluno.domain.model.Aluno
import androidx.compose.runtime.Immutable
import com.fsales.app.e_aluno.ui.ListUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class ListaAlunoUiState(
    val ativos: List<Aluno> = emptyList(),
    val inativos: List<Aluno> = emptyList()
)

@HiltViewModel
class ListaAlunoViewModel @Inject constructor(
    private val repository: AlunoRepository
) : ViewModel() {

    val uiState = repository.getAll()
        .map { alunos ->
            ListaAlunoUiState(
                ativos = alunos.filter { it.ativo },
                inativos = alunos.filterNot { it.ativo }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ListaAlunoUiState()
        )

    private val _uiEvent = Channel<ListUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListaEvento) {
        when (event) {
            is ListaEvento.Detalhes -> detalhe(event.id)
        }
    }

    private fun detalhe(id: Long) {
        _uiEvent.trySend(ListUiEvent.NavigateToDetalhes(id))
    }
}