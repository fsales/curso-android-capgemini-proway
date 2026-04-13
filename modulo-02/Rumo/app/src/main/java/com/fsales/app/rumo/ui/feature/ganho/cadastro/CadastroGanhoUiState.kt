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
    // chave = campo (ERRO_*), valor = tipo de erro do domínio — a Screen resolve para string
    val erros: Map<String, GanhoErro> = emptyMap(),
    val salvando: Boolean = false,
)
