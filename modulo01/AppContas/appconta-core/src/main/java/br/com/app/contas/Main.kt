package br.com.app.contas

import br.com.app.contas.presentation.console.ContaConsoleController
import br.com.app.contas.presentation.console.Cor
import br.com.app.contas.presentation.console.MenuPrincipal
import br.com.app.contas.presentation.messages.Messages
import br.com.app.contas.service.ContaService

fun main() {
    val contaService = ContaService()
    var deveContinuar = true

    val tela = ContaConsoleController(
        service = contaService,
        imprimir = ::println,
        lerLinha = ::readln
    )

    val menu = MenuPrincipal(
        imprimir = { print("${Cor.CYAN}$it${Cor.RESET}") },
        ler = { readlnOrNull()?.trim()?.toIntOrNull() ?: -1 }
    )

    while (deveContinuar) {
        menu.exibir { opcao ->
            when (opcao) {
                MenuPrincipal.Opcao.ADICIONAR_CONTA -> tela.adicionarConta()
                MenuPrincipal.Opcao.LISTA_CONTA -> tela.listarContas()
                MenuPrincipal.Opcao.PESQUISAR_CONTA -> tela.pesquisar()
                MenuPrincipal.Opcao.ALTERAR_CONTA -> tela.alterarConta()
                MenuPrincipal.Opcao.REMOVER_CONTA -> tela.removerConta()

                MenuPrincipal.Opcao.FINALIZAR -> {
                    tela.finalizar()
                    deveContinuar = false
                }

                null -> tela.opcaoInvalida()
            }

            if (deveContinuar) {
                println(Messages.PRESSIONE_ENTER)
                readlnOrNull()
            }
        }
    }
}

