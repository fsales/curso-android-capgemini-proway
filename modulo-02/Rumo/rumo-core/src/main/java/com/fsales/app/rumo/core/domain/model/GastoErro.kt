package com.fsales.app.rumo.core.domain.model

/**
 * Erros de domínio para a entidade [Gasto].
 * Identifica o tipo do erro — a mensagem para o usuário fica no strings.xml da UI.
 */
sealed class GastoErro {
    object DescricaoObrigatoria    : GastoErro()
    object ValorInvalido           : GastoErro()
    object DataForaDeCompetencia   : GastoErro()
    object DataVencimentoInvalida  : GastoErro()
}

