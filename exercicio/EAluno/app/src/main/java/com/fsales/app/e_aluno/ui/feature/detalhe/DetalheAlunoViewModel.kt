package com.fsales.app.e_aluno.ui.feature.detalhe


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.e_aluno.R
import com.fsales.app.e_aluno.domain.AlunoRepository
import com.fsales.app.e_aluno.domain.model.Aluno
import androidx.compose.runtime.Immutable
import com.fsales.app.e_aluno.ui.DetalhesUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class DetalheAlunoUiState(
    val isLoading: Boolean = false,
    val aluno: Aluno? = null,
    val isNotFound: Boolean = false
)

@HiltViewModel
class DetalheAlunoViewModel @Inject constructor(
    private val repository: AlunoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalheAlunoUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<DetalhesUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var loadedId: Long? = null

    fun load(id: Long) {
        if (loadedId == id) return
        loadedId = id

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isNotFound = false) }

            repository.getBy(id)
                .onSuccess { aluno ->
                    _uiState.value = DetalheAlunoUiState(
                        isLoading = false,
                        aluno = aluno,
                        isNotFound = aluno == null
                    )
                }
                .onFailure {
                    _uiState.value = DetalheAlunoUiState(isLoading = false)
                    _uiEvent.send(DetalhesUiEvent.NavigateBack)
                }
        }
    }
}