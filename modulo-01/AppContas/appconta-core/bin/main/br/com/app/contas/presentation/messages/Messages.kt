package br.com.app.contas.presentation.messages

object Messages {
    const val ESCOLHA_OPCAO = "Escolha uma opcao: "
    const val OPCAO_INVALIDA = "Opcao invalida!"
    const val PRESSIONE_ENTER = "\nPressione Enter para continuar..."
    const val MENU_TITULO = "MENU PRINCIPAL"

    const val MENU_CADASTRAR_CONTA = "Cadastrar Conta"
    const val MENU_LISTAR_CONTAS = "Listar Contas"
    const val MENU_PESQUISAR_CONTA = "Pesquisar Conta"
    const val MENU_ALTERAR_CONTA = "Alterar Conta"
    const val MENU_REMOVER_CONTA = "Remover Conta"
    const val MENU_FINALIZAR = "Finalizar"

    const val FILTRO = "Filtro (ID, Descricao ou Tipo):"
    const val ID = "ID:"
    const val TIPO = "Tipo (1 Receita / 2 Despesa):"
    const val VALOR = "Valor:"
    const val DESCRICAO = "Descricao:"
    const val DATA_PAGAMENTO = "Data pagamento:"

    const val CONTA_ALTERADA = "Conta alterada"
    const val CONTA_REMOVIDA = "Conta removida"
    const val CONTA_CRIADA = "Conta criada"
    const val FINALIZANDO = "Finalizando..."

    const val NENHUMA_CONTA_CRITERIO = "Nenhuma conta encontrada para o criterio."
    const val SALDO_RESULTADO = "SALDO DO RESULTADO:"
    const val DATA_INVALIDA = "Data invalida"
    const val CONTA_PENDENTE = "Pendente"

    const val VOCE_ESCOLHEU_ADICIONAR_CONTA = "Voce escolheu Adicionar Conta"
    const val VOCE_ESCOLHEU_LISTAR_CONTAS = "Voce escolheu Listar Contas"
    const val VOCE_ESCOLHEU_PESQUISAR_CONTA = "Voce escolheu Pesquisar Conta"
    const val VOCE_ESCOLHEU_ALTERAR_CONTA = "Voce escolheu Alterar Conta"
    const val VOCE_ESCOLHEU_REMOVER_CONTA = "Voce escolheu Remover Conta"

    const val TABELA_COLUNA_ID = "ID"
    const val TABELA_COLUNA_TIPO = "TIPO"
    const val TABELA_COLUNA_VALOR = "VALOR"
    const val TABELA_COLUNA_DESCRICAO = "DESCRICAO"
    const val TABELA_COLUNA_DATA = "DATA"

    const val TIPO_RECEITA = "Receita"
    const val TIPO_DESPESA = "Despesa"

    const val ERRO_TIPO_CONTA_INVALIDO = "Tipo de conta invalido"
    const val ERRO_VALOR_INVALIDO = "Valor invalido"
    const val ERRO_ID_INVALIDO = "ID invalido"
    const val ERRO_CONTA_NAO_ENCONTRADA_TEMPLATE = "Conta com ID %s nao encontrada"
    const val ERRO_CONTA_SEM_ID_PARA_ALTERAR = "Conta deve ter um ID para ser alterada"
}