package com.fsales.app.rumo.core.domain.model

/**
 * Erros de domínio para a entidade [Ganho].
 *
 * Estende [Exception] para que possa ser usado diretamente como
 * `Throwable` em [Result.failure], permitindo `when (erro)` no ViewModel.
 */
sealed class GanhoErro(message: String) : Exception(message) {
    object DescricaoObrigatoria   : GanhoErro("A descrição do ganho é obrigatória.")
    object ValorInvalido          : GanhoErro("O valor do ganho deve ser maior que zero.")
    object DataForaDeCompetencia  : GanhoErro("O mês/ano de referência deve corresponder à data de recebimento.")
    object CompetenciaInvalida    : GanhoErro("Competência financeira inválida.")
}


