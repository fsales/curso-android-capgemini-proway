package com.fsales.app.rumo.core.domain.model

/**
 * Erros de domínio para a entidade [Sonho].
 * Identifica o tipo do erro — a mensagem para o usuário fica no strings.xml da UI.
 */
sealed class SonhoErro {
    object TituloObrigatorio : SonhoErro()
    object ValorMetaInvalido : SonhoErro()
}

