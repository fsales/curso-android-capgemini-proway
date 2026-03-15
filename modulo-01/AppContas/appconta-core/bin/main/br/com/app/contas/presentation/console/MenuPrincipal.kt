package br.com.app.contas.presentation.console

import br.com.app.contas.presentation.messages.Messages

class MenuPrincipal(
    private val imprimir: (String) -> Unit,
    private val ler: () -> Int
) {
    enum class Opcao(
        val codigo: Int,
        val descricao: String
    ) {
        ADICIONAR_CONTA(1, Messages.MENU_CADASTRAR_CONTA),
        LISTA_CONTA(2, Messages.MENU_LISTAR_CONTAS),
        PESQUISAR_CONTA(3, Messages.MENU_PESQUISAR_CONTA),
        ALTERAR_CONTA(4, Messages.MENU_ALTERAR_CONTA),
        REMOVER_CONTA(5, Messages.MENU_REMOVER_CONTA),
        FINALIZAR(6, Messages.MENU_FINALIZAR);

        companion object {
            fun exibirTodasAsOpcoes(): Sequence<String> {
                return entries.asSequence().map { opcao -> "${opcao.codigo}. ${opcao.descricao}" }
            }

            fun fromCodigo(codigo: Int): Opcao? {
                return entries.find { it.codigo == codigo }
            }
        }
    }

    fun exibir(processarOpcao: (Opcao?) -> Unit) {
        val menuBuilder = StringBuilder()
        menuBuilder.append("\n+--- ${Messages.MENU_TITULO} ---+\n")

        Opcao.exibirTodasAsOpcoes().forEach {
            menuBuilder.append("| $it\n")
        }

        menuBuilder.append("+---------------------+\n")
        menuBuilder.append(Messages.ESCOLHA_OPCAO)

        imprimir(menuBuilder.toString())

        val opcaoInt = ler()
        val opcaoEnum = Opcao.fromCodigo(opcaoInt)
        processarOpcao(opcaoEnum)
    }
}