package br.com.app.contas.service

import br.com.app.contas.presentation.messages.Messages
import br.com.app.contas.extensions.toDate
import br.com.app.contas.model.Conta
import br.com.app.contas.model.Tipo
import br.com.app.contas.repository.ContaRepository


class ContaService {

    private val repository = ContaRepository()

    fun criarConta(
        tipo: Int,
        valor: String,
        descricao: String,
        dataPagamento: String?
    ): Conta {

        val conta = Conta(
            tipo = Tipo.fromCodigo(tipo)
                ?: throw IllegalArgumentException(Messages.ERRO_TIPO_CONTA_INVALIDO),
            valor = valor.toDoubleOrNull() ?: throw IllegalArgumentException(Messages.ERRO_VALOR_INVALIDO),
            descricao = descricao,
            dataPagamento = dataPagamento?.toDate()
        )

        return repository.adicionar(conta)
    }

    fun alterar(
        id: String,
        tipo: Int,
        valor: String,
        descricao: String,
        dataPagamento: String?
    ): Conta {
        val contaExistente = repository.consultaPorId(id.toIntOrNull() ?: throw IllegalArgumentException(Messages.ERRO_ID_INVALIDO))
            ?: throw IllegalArgumentException(Messages.ERRO_CONTA_NAO_ENCONTRADA_TEMPLATE.format(id))

        val contaAtualizada = contaExistente.copy(
            tipo = Tipo.fromCodigo(tipo) ?: throw IllegalArgumentException(Messages.ERRO_TIPO_CONTA_INVALIDO),
            valor = valor.toDoubleOrNull() ?: throw IllegalArgumentException(Messages.ERRO_VALOR_INVALIDO),
            descricao = descricao,
            dataPagamento = dataPagamento?.toDate()
        )

        return repository.alterar(contaAtualizada)
    }

    fun listar(): Sequence<Conta> = repository.listar()

    fun remover(id: String): Boolean = repository.remover(id.toIntOrNull() ?: throw IllegalArgumentException(Messages.ERRO_ID_INVALIDO))

    fun pesquisar(filtro: String): Sequence<Conta> = repository.pesquisar(filtro)
}