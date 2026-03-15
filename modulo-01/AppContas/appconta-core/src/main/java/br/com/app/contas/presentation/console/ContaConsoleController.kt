package br.com.app.contas.presentation.console

import br.com.app.contas.extensions.formatada
import br.com.app.contas.model.Conta
import br.com.app.contas.model.Tipo
import br.com.app.contas.presentation.messages.Messages
import br.com.app.contas.service.ContaService

class ContaConsoleController(
    private val service: ContaService,
    private val imprimir: (String) -> Unit,
    private val lerLinha: () -> String
) {
    fun adicionarConta() {
        imprimir("${Cor.VERDE}${Messages.VOCE_ESCOLHEU_ADICIONAR_CONTA}${Cor.RESET}")

        val tipo = ler(Messages.TIPO).toIntOrNull() ?: 0
        val valor = ler(Messages.VALOR)
        val descricao = ler(Messages.DESCRICAO)
        val data = ler(Messages.DATA_PAGAMENTO)

        runCatching {
            service.criarConta(tipo, valor, descricao, data)
        }.onSuccess {
            imprimir("${Cor.VERDE}${Messages.CONTA_CRIADA} ${it.id}${Cor.RESET}")
        }.onFailure {
            imprimir("${Cor.VERMELHO}${it.message}${Cor.RESET}")
        }
    }

    fun listarContas() {
        imprimir("${Cor.CYAN}${Messages.VOCE_ESCOLHEU_LISTAR_CONTAS}${Cor.RESET}")
        imprimirTabela(service.listar())
    }

    fun pesquisar() {
        imprimir("${Cor.AMARELO}${Messages.VOCE_ESCOLHEU_PESQUISAR_CONTA}${Cor.RESET}")

        val filtro = ler(Messages.FILTRO)
        val resultado = service.pesquisar(filtro)
        imprimirTabela(resultado)
    }

    fun alterarConta() {
        imprimir("${Cor.AMARELO}${Messages.VOCE_ESCOLHEU_ALTERAR_CONTA}${Cor.RESET}")

        val id = ler(Messages.ID)
        val tipo = ler(Messages.TIPO).toIntOrNull() ?: 0
        val valor = ler(Messages.VALOR)
        val descricao = ler(Messages.DESCRICAO)
        val data = ler(Messages.DATA_PAGAMENTO)

        runCatching {
            service.alterar(id, tipo, valor, descricao, data)
        }.onSuccess {
            imprimir("${Cor.VERDE}${Messages.CONTA_ALTERADA} ${it.id}${Cor.RESET}")
        }.onFailure {
            imprimir("${Cor.VERMELHO}${it.message}${Cor.RESET}")
        }
    }

    fun removerConta() {
        imprimir("${Cor.VERMELHO}${Messages.VOCE_ESCOLHEU_REMOVER_CONTA}${Cor.RESET}")

        val id = ler(Messages.ID)

        runCatching {
            service.remover(id)
        }.onSuccess {
            if (it) {
                imprimir("${Cor.VERDE}${Messages.CONTA_REMOVIDA}${Cor.RESET}")
            }
        }.onFailure {
            imprimir("${Cor.VERMELHO}${it.message}${Cor.RESET}")
        }
    }

    fun opcaoInvalida() {
        imprimir("${Cor.VERMELHO}${Messages.OPCAO_INVALIDA}${Cor.RESET}")
    }

    fun finalizar() {
        imprimir("${Cor.AZUL}${Messages.FINALIZANDO}${Cor.RESET}")
    }

    private fun ler(msg: String): String {
        imprimir(msg)
        return lerLinha()
    }

    private fun imprimirTabela(contas: Sequence<Conta>) {
        val lista = contas.toList()

        if (lista.isEmpty()) {
            imprimir("${Cor.AMARELO}${Messages.NENHUMA_CONTA_CRITERIO}${Cor.RESET}")
            return
        }

        imprimir("\n${"=".repeat(70)}")
        imprimir(
            String.format(
                "| %-4s | %-10s | %10s | %-20s | %-12s |",
                Messages.TABELA_COLUNA_ID,
                Messages.TABELA_COLUNA_TIPO,
                Messages.TABELA_COLUNA_VALOR,
                Messages.TABELA_COLUNA_DESCRICAO,
                Messages.TABELA_COLUNA_DATA
            )
        )
        imprimir("-".repeat(70))

        lista.forEach { conta ->
            imprimir(conta.formatada())
        }

        imprimir("=".repeat(70))

        val saldo = lista.sumOf { if (it.tipo == Tipo.RECEITA) it.valor else -it.valor }
        val corSaldo = if (saldo >= 0) Cor.VERDE else Cor.VERMELHO
        imprimir("${Messages.SALDO_RESULTADO} $corSaldo${String.format("%.2f", saldo)}${Cor.RESET}\n")
    }
}
