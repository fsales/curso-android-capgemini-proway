package com.fsales.app.rumo.ui.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.usecase.ObterSaldoMensalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val obterSaldoMensalUseCase: ObterSaldoMensalUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        carregarResumo(_uiState.value.mesAno)
    }

    fun onMesAnterior() {
        val novo = _uiState.value.mesAno.minusMonths(1)
        _uiState.value = _uiState.value.copy(mesAno = novo)
        carregarResumo(novo)
    }

    fun onMesProximo() {
        val novo = _uiState.value.mesAno.plusMonths(1)
        _uiState.value = _uiState.value.copy(mesAno = novo)
        carregarResumo(novo)
    }

    fun onSelecionarMesAno(mesAno: YearMonth) {
        _uiState.value = _uiState.value.copy(mesAno = mesAno)
        carregarResumo(mesAno)
    }

    private fun carregarResumo(mesAno: YearMonth) {
        viewModelScope.launch {
            obterSaldoMensalUseCase(mesAno.monthValue, mesAno.year)
                .onStart { _uiState.value = _uiState.value.copy(carregando = true, erro = null) }
                .catch { e -> _uiState.value = _uiState.value.copy(carregando = false, erro = e.message ?: "Erro") }
                .collect { resumo ->
                    _uiState.value = _uiState.value.copy(
                        carregando = false,
                        erro = null,
                        totalGanhos = resumo.totalGanhos,
                        totalGastos = resumo.totalGastos,
                        saldo = resumo.saldo,
                    )
                }
        }
    }
}
