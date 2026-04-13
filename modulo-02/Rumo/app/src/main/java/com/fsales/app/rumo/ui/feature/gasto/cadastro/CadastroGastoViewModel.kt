package com.fsales.app.rumo.ui.feature.gasto.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.model.GastoErro
import com.fsales.app.rumo.core.domain.usecase.SalvarGastoUseCase
import com.fsales.app.rumo.core.domain.usecase.GastoErroException
import com.fsales.app.rumo.ui.util.toBigDecimalOuNulo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CadastroGastoViewModel @Inject constructor(
    private val salvarGastoUseCase: SalvarGastoUseCase,
) : ViewModel() {


    private val _uiState = MutableStateFlow(
        CadastroGastoUiState(dataGasto = LocalDate.now()),
    )
    val uiState: StateFlow<CadastroGastoUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<CadastroGastoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var jaSubmeteu = false

    fun onEvent(event: CadastroGastoEvent) {
        when (event) {
            is CadastroGastoEvent.AlterarDescricao  -> alterarDescricao(event.valor)
            is CadastroGastoEvent.AlterarValor       -> alterarValor(event.valor)
            is CadastroGastoEvent.AlterarData        -> _uiState.update { it.copy(dataGasto = event.data) }
            is CadastroGastoEvent.AlterarCategoria   -> _uiState.update { it.copy(categoria = event.categoria) }
            is CadastroGastoEvent.AlterarEssencial   -> _uiState.update { it.copy(essencial = event.essencial) }
            is CadastroGastoEvent.AlterarRecorrente  -> _uiState.update { it.copy(recorrente = event.recorrente) }
            is CadastroGastoEvent.AlterarObservacao  -> _uiState.update { it.copy(observacao = event.observacao) }
            CadastroGastoEvent.Salvar                -> salvar()
            CadastroGastoEvent.Voltar                -> {
                resetar()
                _uiEvent.trySend(CadastroGastoUiEvent.NavigateBack)
            }
        }
    }

    private fun alterarDescricao(valor: String) = _uiState.update { state ->
        state.copy(
            descricao = valor,
            erroDescricao = if (jaSubmeteu && valor.isNotBlank()) null else state.erroDescricao,
        )
    }

    private fun alterarValor(valor: String) = _uiState.update { state ->
        val valorValido = (valor.toBigDecimalOuNulo() ?: BigDecimal.ZERO) > BigDecimal.ZERO
        state.copy(
            valorTexto = valor,
            erroValor = if (jaSubmeteu && valorValido) null else state.erroValor,
        )
    }

    private fun salvar() {
        jaSubmeteu = true
        val state = _uiState.value

        val erroDescricao = if (state.descricao.isBlank()) GastoErro.DescricaoObrigatoria else null
        val v = state.valorTexto.toBigDecimalOuNulo()
        val erroValor = if (v == null || v <= BigDecimal.ZERO) GastoErro.ValorInvalido else null

        if (erroDescricao != null || erroValor != null) {
            _uiState.update { it.copy(erroDescricao = erroDescricao, erroValor = erroValor) }
            return
        }

        val gasto = Gasto(
            descricao      = state.descricao.trim(),
            valor          = state.valorTexto.toBigDecimalOuNulo() ?: BigDecimal.ZERO,
            dataGasto      = state.dataGasto,
            mesReferencia  = state.dataGasto.monthValue,
            anoReferencia  = state.dataGasto.year,
            categoria      = state.categoria,
            essencial      = state.essencial,
            recorrente     = state.recorrente,
            observacao     = state.observacao.trim().ifBlank { null },
        )

        viewModelScope.launch {
            _uiState.update { it.copy(salvando = true) }
            salvarGastoUseCase(gasto)
                .onSuccess {
                    resetar()
                    _uiEvent.send(CadastroGastoUiEvent.NavigateBack)
                }
                .onFailure { throwable ->
                    val erro = (throwable as? GastoErroException)?.erro
                    when (erro) {
                        GastoErro.DescricaoObrigatoria ->
                            _uiState.update { it.copy(erroDescricao = GastoErro.DescricaoObrigatoria) }
                        GastoErro.ValorInvalido ->
                            _uiState.update { it.copy(erroValor = GastoErro.ValorInvalido) }
                        else -> _uiEvent.send(CadastroGastoUiEvent.ErroAoSalvar)
                    }
                }
            _uiState.update { it.copy(salvando = false) }
        }
    }

    private fun resetar() {
        jaSubmeteu = false
        _uiState.value = CadastroGastoUiState(dataGasto = LocalDate.now())
    }
}