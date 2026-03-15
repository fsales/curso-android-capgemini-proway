package br.com.app.contas.model

import br.com.app.contas.presentation.messages.Messages
import java.time.LocalDate

data class Conta(
    val id: Int? = null,
    val tipo: Tipo,
    var valor: Double,
    var descricao: String,
    var dataPagamento: LocalDate?
)

enum class Tipo(val codigo: Int, val descricao: String) {
    RECEITA(1, Messages.TIPO_RECEITA),
    DESPESA(2, Messages.TIPO_DESPESA);

    companion object {
        fun fromCodigo(codigo: Int): Tipo? {

            return entries.find { it.codigo == codigo }
        }
    }
}
