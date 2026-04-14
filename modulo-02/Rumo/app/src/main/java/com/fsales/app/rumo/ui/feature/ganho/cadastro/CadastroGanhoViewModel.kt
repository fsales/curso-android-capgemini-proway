package com.fsales.app.rumo.ui.feature.ganho.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.GanhoErro
import com.fsales.app.rumo.core.domain.usecase.AtualizarGanhoUseCase
import com.fsales.app.rumo.core.domain.usecase.ObterGanhoPorIdUseCase
import com.fsales.app.rumo.core.domain.usecase.SalvarGanhoUseCase
import com.fsales.app.rumo.core.domain.usecase.GanhoErroException
import com.fsales.app.rumo.ui.util.centavosParaBigDecimal
import com.fsales.app.rumo.ui.util.toDigitosCentavos
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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

@HiltViewModel(assistedFactory = CadastroGanhoViewModel.Factory::class)
class CadastroGanhoViewModel @AssistedInject constructor(
    @Assisted val ganhoId: Long?,
    private val salvarGanhoUseCase: SalvarGanhoUseCase,
    private val atualizarGanhoUseCase: AtualizarGanhoUseCase,
    private val obterGanhoPorIdUseCase: ObterGanhoPorIdUseCase,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(ganhoId: Long?): CadastroGanhoViewModel
    }

    private val _uiState = MutableStateFlow(
        CadastroGanhoUiState(dataRecebimento = LocalDate.now()),
    )
    val uiState: StateFlow<CadastroGanhoUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<CadastroGanhoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var jaSubmeteu = false

    internal fun carregarParaEdicao(id: Long) {
        viewModelScope.launch {
            runCatching { obterGanhoPorIdUseCase(id) }
                .onSuccess { ganho ->
                    if (ganho != null) {
                        _uiState.update {
                            it.copy(
                                descricao       = ganho.descricao,
                                valorTexto      = ganho.valor.toDigitosCentavos(),
                                dataRecebimento = ganho.dataRecebimento,
                                tipo            = ganho.tipo,
                                recorrente      = ganho.recorrente,
                                observacao      = ganho.observacao ?: "",
                                modoEdicao      = ModoEdicaoGanho.INDIVIDUAL,
                                ganhoIdEdicao   = id,
                            )
                        }
                    }
                }
        }
    }

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
        val digits = valor.filter { it.isDigit() }
        val valorValido = (digits.centavosParaBigDecimal() ?: BigDecimal.ZERO) > BigDecimal.ZERO
        state.copy(
            valorTexto = digits,
            erroValor  = if (jaSubmeteu && valorValido) null else state.erroValor,
        )
    }

    private fun salvar() {
        jaSubmeteu = true
        val state = _uiState.value

        val erroDescricao = if (state.descricao.isBlank()) GanhoErro.DescricaoObrigatoria else null
        val v             = state.valorTexto.centavosParaBigDecimal()
        val erroValor     = if (v == null || v <= BigDecimal.ZERO) GanhoErro.ValorInvalido else null

        if (erroDescricao != null || erroValor != null) {
            _uiState.update { it.copy(erroDescricao = erroDescricao, erroValor = erroValor) }
            return
        }

        val ganho = Ganho(
            id              = state.ganhoIdEdicao ?: 0L,
            descricao       = state.descricao.trim(),
            valor           = state.valorTexto.centavosParaBigDecimal() ?: BigDecimal.ZERO,
            dataRecebimento = state.dataRecebimento,
            mesReferencia   = state.dataRecebimento.monthValue,
            anoReferencia   = state.dataRecebimento.year,
            tipo            = state.tipo,
            recorrente      = state.recorrente,
            observacao      = state.observacao.trim().ifBlank { null },
        )

        viewModelScope.launch {
            _uiState.update { it.copy(salvando = true) }
            val resultado = if (state.ganhoIdEdicao != null) atualizarGanhoUseCase(ganho) else salvarGanhoUseCase(ganho)
            resultado
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

    internal fun resetar() {
        jaSubmeteu = false
        _uiState.value = CadastroGanhoUiState(dataRecebimento = LocalDate.now())
    }
}
