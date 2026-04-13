package com.fsales.app.rumo.core.domain.model

/**
 * Status calculado do sonho — derivado de [Sonho.valorAtual] e [Sonho.valorMeta].
 * Não é armazenado diretamente no banco.
 */
enum class StatusSonho(val descricao: String) {
    NAO_INICIADO("Não iniciado"),
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDO("Concluído"),
}
