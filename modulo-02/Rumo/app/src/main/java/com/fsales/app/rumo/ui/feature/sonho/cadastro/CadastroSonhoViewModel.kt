package com.fsales.app.rumo.ui.feature.sonho.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.core.domain.usecase.SalvarSonhoUseCase
import com.fsales.app.rumo.ui.util.toBigDecimalOuNulo
import com.fsales.app.rumo.ui.CadastroSonhoUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CadastroSonhoViewModel @Inject constructor(
    private val salvarSonhoUseCase: SalvarSonhoUseCase,
) : ViewModel() {

    companion object {
        const val ERRO_TITULO     = "titulo"
        const val ERRO_VALOR_META = "valorMeta"
    }

    private val _uiState = MutableStateFlow(CadastroSonhoUiState())
    val uiState: StateFlow<CadastroSonhoUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<CadastroSonhoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var jaSubmeteu = false

    fun onEvent(event: CadastroSonhoEvent) {
        when (event) {
            is CadastroSonhoEvent.AlterarTitulo     -> alterarTitulo(event.valor)
            is CadastroSonhoEvent.AlterarDescricao  -> _uiState.update { it.copy(descricao = event.valor) }
            is CadastroSonhoEvent.AlterarValorMeta  -> alterarValorMeta(event.valor)
            is CadastroSonhoEvent.AlterarPrioridade -> _uiState.update { it.copy(prioridade = event.prioridade) }
            is CadastroSonhoEvent.AlterarPrazo      -> _uiState.update { it.copy(prazoAlvo = event.prazo) }
            CadastroSonhoEvent.Salvar               -> salvar()
            CadastroSonhoEvent.Voltar               -> {
                resetar()
                _uiEvent.trySend(CadastroSonhoUiEvent.NavigateBack)
            }
        }
    }

    private fun alterarTitulo(valor: String) = _uiState.update { state ->
        state.copy(
            titulo = valor,
            erros  = if (jaSubmeteu && valor.isNotBlank()) state.erros - ERRO_TITULO else state.erros,
        )
    }

    private fun alterarValorMeta(valor: String) = _uiState.update { state ->
        val valido = (valor.toBigDecimalOuNulo() ?: BigDecimal.ZERO) > BigDecimal.ZERO
        state.copy(
            valorMetaTexto = valor,
            erros          = if (jaSubmeteu && valido) state.erros - ERRO_VALOR_META else state.erros,
        )
    }

    private fun salvar() {
        jaSubmeteu = true
        val state = _uiState.value

        val erros = buildMap<String, String> {
            if (state.titulo.isBlank()) {
                put(ERRO_TITULO, "O título é obrigatório.")
            }
            val meta = state.valorMetaTexto.toBigDecimalOuNulo()
            if (meta == null || meta <= BigDecimal.ZERO) {
                put(ERRO_VALOR_META, "Informe um valor meta maior que zero.")
            }
        }

        if (erros.isNotEmpty()) {
            _uiState.update { it.copy(erros = erros) }
            return
        }

        val sonho = Sonho(
            titulo     = state.titulo.trim(),
            descricao  = state.descricao.trim().ifBlank { null },
            valorMeta  = state.valorMetaTexto.toBigDecimalOuNulo() ?: BigDecimal.ZERO,
            valorAtual = BigDecimal.ZERO,
            prioridade = state.prioridade,
            prazoAlvo  = state.prazoAlvo,
        )

        viewModelScope.launch {
            _uiState.update { it.copy(salvando = true) }
            salvarSonhoUseCase(sonho)
                .onSuccess {
                    resetar()
                    _uiEvent.send(CadastroSonhoUiEvent.NavigateBack)
                }
                .onFailure { _uiEvent.send(CadastroSonhoUiEvent.ErroAoSalvar) }
            _uiState.update { it.copy(salvando = false) }
        }
    }

    private fun resetar() {
        jaSubmeteu = false
        _uiState.value = CadastroSonhoUiState()
    }
}