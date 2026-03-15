package br.com.app.contas.repository

import br.com.app.contas.presentation.messages.Messages
import br.com.app.contas.model.Conta

class ContaRepository {
    private val contas = mutableMapOf<Int, Conta>()
    private var nextId: Int = 1;

    fun adicionar(conta: Conta): Conta {
        val idGerado = gerarId()
        val contaComId = conta.copy(id = idGerado)
        contas[idGerado] = contaComId
        return contaComId
    }

    fun alterar(conta: Conta): Conta {
        val id = conta.id ?: throw IllegalArgumentException(Messages.ERRO_CONTA_SEM_ID_PARA_ALTERAR)
        contas[id] = conta
        return conta
    }

    fun pesquisar(filtro: String): Sequence<Conta> {
        return contas.values.asSequence().filter { c ->
            listOf(
                c.id.toString(),
                c.descricao,
                c.tipo.name,
                c.valor.toString()
            ).any { it.contains(filtro, ignoreCase = true) }
        }
    }

    fun remover(id: Int): Boolean = contas.remove(id) != null

    fun listar(): Sequence<Conta> = contas.values.asSequence()

    fun consultaPorId(id: Int): Conta? = contas[id]

    private fun gerarId(): Int {
        return  nextId++
    }
}