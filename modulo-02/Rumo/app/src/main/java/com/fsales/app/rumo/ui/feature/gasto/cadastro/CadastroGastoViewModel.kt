package com.fsales.app.rumo.ui.feature.gasto.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.usecase.SalvarGastoUseCase
import com.fsales.app.rumo.ui.CadastroGastoUiEvent
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

    companion object {
        const val ERRO_DESCRICAO = "descricao"
        const val ERRO_VALOR     = "valor"
    }

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
            CadastroGastoEvent.Voltar                -> _uiEvent.trySend(CadastroGastoUiEvent.NavigateBack)
        }
    }

    private fun alterarDescricao(valor: String) = _uiState.update { state ->
        state.copy(
            descricao = valor,
            erros = if (jaSubmeteu && valor.isNotBlank()) state.erros - ERRO_DESCRICAO else state.erros,
        )
    }

    private fun alterarValor(valor: String) = _uiState.update { state ->
        val valorValido = (valor.toBigDecimalOuNulo() ?: BigDecimal.ZERO) > BigDecimal.ZERO
        state.copy(
            valorTexto = valor,
            erros = if (jaSubmeteu && valorValido) state.erros - ERRO_VALOR else state.erros,
        )
    }

    private fun salvar() {
        jaSubmeteu = true
        val state = _uiState.value

        val erros = buildMap<String, String> {
            if (state.descricao.isBlank()) put(ERRO_DESCRICAO, "A descrição é obrigatória.")
            val v = state.valorTexto.toBigDecimalOuNulo()
            if (v == null || v <= BigDecimal.ZERO) put(ERRO_VALOR, "Informe um valor maior que zero.")
        }

        if (erros.isNotEmpty()) {
            _uiState.update { it.copy(erros = erros) }
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
                .onSuccess { _uiEvent.send(CadastroGastoUiEvent.NavigateBack) }
                .onFailure { _uiEvent.send(CadastroGastoUiEvent.ErroAoSalvar) }
            _uiState.update { it.copy(salvando = false) }
        }
    }
}