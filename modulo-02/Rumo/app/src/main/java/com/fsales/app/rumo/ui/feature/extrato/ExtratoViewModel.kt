package com.fsales.app.rumo.ui.feature.extrato

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.model.ItemExtrato
import com.fsales.app.rumo.core.domain.usecase.ListarExtratoPorMesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ExtratoViewModel @Inject constructor(
    private val listarExtratoPorMesUseCase: ListarExtratoPorMesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExtratoUiState())
    val uiState: StateFlow<ExtratoUiState> = _uiState.asStateFlow()

    private var carregarJob: Job? = null

    init {
        carregarExtrato(_uiState.value.mesAno)
    }

    fun onEvent(event: ExtratoEvent) {
        when (event) {
            ExtratoEvent.MesAnterior          -> alterarMes(_uiState.value.mesAno.minusMonths(1))
            ExtratoEvent.ProximoMes           -> alterarMes(_uiState.value.mesAno.plusMonths(1))
            is ExtratoEvent.SelecionarMesAno  -> alterarMes(event.mesAno)
            ExtratoEvent.TentarNovamente      -> carregarExtrato(_uiState.value.mesAno)
        }
    }

    private fun alterarMes(novoMesAno: YearMonth) {
        _uiState.update { it.copy(mesAno = novoMesAno) }
        carregarExtrato(novoMesAno)
    }

    private fun carregarExtrato(mesAno: YearMonth) {
        carregarJob?.cancel()
        carregarJob = viewModelScope.launch {
            listarExtratoPorMesUseCase(mesAno.monthValue, mesAno.year)
                .onStart { _uiState.update { it.copy(carregando = true, erro = null) } }
                .catch { e ->
                    _uiState.update { it.copy(carregando = false, erro = e.message ?: "Erro desconhecido") }
                }
                .collect { itens ->
                    _uiState.update { calcularEstado(it, itens) }
                }
        }
    }

    private fun calcularEstado(estado: ExtratoUiState, itens: List<ItemExtrato>): ExtratoUiState {
        var totalGanhos = BigDecimal.ZERO
        var totalGastos = BigDecimal.ZERO
        itens.forEach { item ->
            when (item) {
                is ItemExtrato.GanhoItem -> totalGanhos += item.valor
                is ItemExtrato.GastoItem -> totalGastos += item.valor
            }
        }
        return estado.copy(
            itensPorData  = itens.groupBy { it.data },
            totalGanhos   = totalGanhos,
            totalGastos   = totalGastos,
            saldoPeriodo  = totalGanhos - totalGastos,
            carregando    = false,
            erro          = null,
        )
    }
}
