package com.fsales.app.rumo.ui.feature.ganho.cadastro

import com.fsales.app.rumo.core.domain.model.GanhoErro
import com.fsales.app.rumo.core.domain.model.TipoGanho
import java.time.LocalDate

data class CadastroGanhoUiState(
    val descricao: String = "",
    val valorTexto: String = "",
    val dataRecebimento: LocalDate,          // inicializado pelo ViewModel — sem LocalDate.now() aqui
    val tipo: TipoGanho = TipoGanho.SALARIO,
    val recorrente: Boolean = false,
    val observacao: String = "",
    val erroDescricao: GanhoErro? = null,
    val erroValor: GanhoErro? = null,
    val erroData: GanhoErro? = null,
    val salvando: Boolean = false,
    val modoEdicao: ModoEdicaoGanho = ModoEdicaoGanho.NOVO,
    val ganhoIdEdicao: Long? = null,
)
