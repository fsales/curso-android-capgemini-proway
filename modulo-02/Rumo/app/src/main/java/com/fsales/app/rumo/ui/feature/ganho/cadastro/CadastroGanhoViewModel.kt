package com.fsales.app.rumo.ui.feature.ganho.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.GanhoErro
import com.fsales.app.rumo.core.domain.usecase.SalvarGanhoUseCase
import com.fsales.app.rumo.core.domain.usecase.GanhoErroException
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
class CadastroGanhoViewModel @Inject constructor(
    private val salvarGanhoUseCase: SalvarGanhoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CadastroGanhoUiState(dataRecebimento = LocalDate.now()),
    )
    val uiState: StateFlow<CadastroGanhoUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<CadastroGanhoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var jaSubmeteu = false

    fun onEvent(event: CadastroGanhoEvent) {
        when (event) {
            is CadastroGanhoEvent.AlterarDescricao  -> alterarDescricao(event.valor)
            is CadastroGanhoEvent.AlterarValor       -> alterarValor(event.valor)
            is CadastroGanhoEvent.AlterarData        -> _uiState.update { it.copy(dataRecebimento = event.data) }
            is CadastroGanhoEvent.AlterarTipo        -> _uiState.update { it.copy(tipo = event.tipo) }
            is CadastroGanhoEvent.AlterarRecorrente  -> _uiState.update { it.copy(recorrente = event.recorrente) }
            is CadastroGanhoEvent.AlterarObservacao  -> _uiState.update { it.copy(observacao = event.observacao) }
            CadastroGanhoEvent.Salvar                -> salvar()
            CadastroGanhoEvent.Voltar                -> {
                resetar()
                _uiEvent.trySend(CadastroGanhoUiEvent.NavigateBack)
            }
        }
    }

    private fun alterarDescricao(valor: String) = _uiState.update { state ->
        state.copy(
            descricao    = valor,
            erroDescricao = if (jaSubmeteu && valor.isNotBlank()) null else state.erroDescricao,
        )
    }

    private fun alterarValor(valor: String) = _uiState.update { state ->
        val valorValido = (valor.toBigDecimalOuNulo() ?: BigDecimal.ZERO) > BigDecimal.ZERO
        state.copy(
            valorTexto = valor,
            erroValor  = if (jaSubmeteu && valorValido) null else state.erroValor,
        )
    }

    private fun salvar() {
        jaSubmeteu = true
        val state = _uiState.value

        val erroDescricao = if (state.descricao.isBlank()) GanhoErro.DescricaoObrigatoria else null
        val v             = state.valorTexto.toBigDecimalOuNulo()
        val erroValor     = if (v == null || v <= BigDecimal.ZERO) GanhoErro.ValorInvalido else null

        if (erroDescricao != null || erroValor != null) {
            _uiState.update { it.copy(erroDescricao = erroDescricao, erroValor = erroValor) }
            return
        }

        val ganho = Ganho(
            descricao       = state.descricao.trim(),
            valor           = state.valorTexto.toBigDecimalOuNulo() ?: BigDecimal.ZERO,
            dataRecebimento = state.dataRecebimento,
            mesReferencia   = state.dataRecebimento.monthValue,
            anoReferencia   = state.dataRecebimento.year,
            tipo            = state.tipo,
            recorrente      = state.recorrente,
            observacao      = state.observacao.trim().ifBlank { null },
        )

        viewModelScope.launch {
            _uiState.update { it.copy(salvando = true) }
            salvarGanhoUseCase(ganho)
                .onSuccess {
                    resetar()
                    _uiEvent.send(CadastroGanhoUiEvent.NavigateBack)
                }
                .onFailure { throwable ->
                    val erro = (throwable as? GanhoErroException)?.erro
                    when (erro) {
                        GanhoErro.DescricaoObrigatoria ->
                            _uiState.update { it.copy(erroDescricao = GanhoErro.DescricaoObrigatoria) }
                        GanhoErro.ValorInvalido ->
                            _uiState.update { it.copy(erroValor = GanhoErro.ValorInvalido) }
                        GanhoErro.DataForaDeCompetencia ->
                            _uiState.update { it.copy(erroData = GanhoErro.DataForaDeCompetencia) }
                        GanhoErro.CompetenciaInvalida ->
                            _uiState.update { it.copy(erroData = GanhoErro.CompetenciaInvalida) }
                        else -> _uiEvent.send(CadastroGanhoUiEvent.ErroAoSalvar)
                    }
                }
            _uiState.update { it.copy(salvando = false) }
        }
    }

    private fun resetar() {
        jaSubmeteu = false
        _uiState.value = CadastroGanhoUiState(dataRecebimento = LocalDate.now())
    }
}