package br.com.app.contas.extensions

import br.com.app.contas.model.Conta
import br.com.app.contas.model.Tipo
import br.com.app.contas.presentation.console.Cor
import br.com.app.contas.presentation.messages.Messages

fun Conta.formatada(): String {
    val corTipo = if (tipo == Tipo.RECEITA) Cor.VERDE else Cor.VERMELHO
    val sinal = if (tipo == Tipo.RECEITA) "+" else "-" // Simplifiquei o sinal

    return String.format(
        "| %-4d | $corTipo%-10s${Cor.RESET} | %1s %10.2f | %-20s | %-12s |",
        id,
        tipo.name,
        sinal,
        valor,
        descricao.take(20),
        dataPagamento?.toBrazilDateString() ?: Messages.CONTA_PENDENTE
    )
}