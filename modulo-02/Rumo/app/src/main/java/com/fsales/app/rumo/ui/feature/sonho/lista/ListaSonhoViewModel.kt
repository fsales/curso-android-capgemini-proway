package com.fsales.app.rumo.ui.feature.sonho.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.model.ProjecaoSonho
import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.core.domain.usecase.ListarSonhosUseCase
import com.fsales.app.rumo.core.domain.usecase.ObterProjecaoSonhosUseCase
import com.fsales.app.rumo.ui.ListaSonhoUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ListaSonhoViewModel @Inject constructor(
    private val listarSonhosUseCase: ListarSonhosUseCase,
    private val obterProjecaoSonhosUseCase: ObterProjecaoSonhosUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListaSonhoUiState())
    val uiState: StateFlow<ListaSonhoUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ListaSonhoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        carregarProjecoes()
    }

    fun onEvent(event: ListaSonhoEvent) {
        when (event) {
            ListaSonhoEvent.IrParaCadastro  -> _uiEvent.trySend(ListaSonhoUiEvent.NavigateToCadastro)
            ListaSonhoEvent.TentarNovamente -> carregarProjecoes()
            is ListaSonhoEvent.AbrirSonho   -> _uiEvent.trySend(ListaSonhoUiEvent.NavigateToDetalhe(event.id))
        }
    }

    private fun carregarProjecoes() {
        val hoje = LocalDate.now()
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            kotlinx.coroutines.flow.combine(
                listarSonhosUseCase(),
                obterProjecaoSonhosUseCase(hoje.monthValue, hoje.year)
            ) { sonhos, projecoes ->
                // Mapeia cada sonho para sua projeção, se existir, ou cria uma "vazia"
                sonhos.map { sonho ->
                    projecoes.find { it.sonho.id == sonho.id } ?: ProjecaoSonho(
                        sonho = sonho,
                        saldoMensal = java.math.BigDecimal.ZERO,
                        mesesNecessarios = null,
                        seraAlcancadoNoPrazo = null
                    )
                }
            }
                .catch { e ->
                    _uiState.update { it.copy(carregando = false, erro = e.message ?: "Erro desconhecido") }
                }
                .collect { todasProjecoes ->
                    _uiState.update { it.copy(carregando = false, erro = null, projecoes = todasProjecoes) }
                }
        }
    }
}