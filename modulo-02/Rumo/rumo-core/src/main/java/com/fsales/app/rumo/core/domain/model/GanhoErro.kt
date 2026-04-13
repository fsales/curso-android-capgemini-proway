package com.fsales.app.rumo.core.domain.model

/**
 * Erros de domínio para a entidade [Ganho].
 * Identifica o tipo do erro — a mensagem para o usuário fica no strings.xml da UI.
 */
sealed class GanhoErro {
    object DescricaoObrigatoria  : GanhoErro()
    object ValorInvalido         : GanhoErro()
    object DataForaDeCompetencia : GanhoErro()
    object CompetenciaInvalida   : GanhoErro()
}


