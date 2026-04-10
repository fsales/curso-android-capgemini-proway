# AGENTS.md - Guia para Agentes de Codigo (Projeto Rumo)

Este documento define como agentes de IA devem contribuir neste repositorio Android.
As regras abaixo sao obrigatorias para manter consistencia arquitetural e facilitar evolucao do app.

## 1) Contexto e escopo do projeto

- Projeto Android com dois modulos em `settings.gradle.kts`: `:app` e `:rumo-core`.
- Stack observada em Gradle: Kotlin, Jetpack Compose, Material 3, Coroutines/Flow, Room, Hilt e Navigation 3.
- Namespace principal atual: `com.fsales.app.rumo`.
- Estado atual do codigo: base inicial (template Compose em `MainActivity.kt`) e tema configurado.

## 2) Arquitetura obrigatoria

Use estritamente MVVM com separacao clara de responsabilidades:

- `UI (Compose)`: renderiza estado e envia eventos do usuario.
- `ViewModel`: processa eventos, coordena use cases, atualiza `UiState` e emite `UiEvent`.
- `Domain`: regras de negocio puras em casos de uso pequenos e focados.
- `Data`: repositories, DAOs e entities do Room.

Regra central: nenhuma regra de negocio na UI.

## 3) Contrato de modulos

- `app/`: telas, navegacao, ViewModels, componentes Compose e composicao de features.
- `rumo-core/`: persistencia local (Room), modelos compartilhados, repositories e utilitarios reutilizaveis.
- Evite dependencias ciclicas. `app` pode depender de `rumo-core`; o inverso nao.

## 4) Convencoes de estrutura por feature

Ao criar funcionalidades (ex: ganhos, gastos, sonhos, home), use estrutura orientada a feature:

```text
com.fsales.app.rumo.<feature>
|- data/
|  |- local/              (Entity, Dao, Database mappings)
|  \- repository/         (interface + impl)
|- domain/
|  \- usecase/            (interface por caso de uso)
|     \- impl/            (<Feature>UseCaseImpl — logica e validacao)
|- ui/
|  |- components/          (componentes reutilizaveis)
|  |- <Feature>Screen.kt
|  |- <Feature>ViewModel.kt
|  \- <Feature>UiState.kt
\- navigation/            (rotas e contratos de navegacao da feature)
```

## 5) UI e Compose

- Jetpack Compose obrigatorio para telas novas.
- Material 3 obrigatorio para componentes e tema.
- Componentes devem ser pequenos, reutilizaveis e sem acoplamento desnecessario.
- Layout XML e proibido para novas telas.
- XML de infraestrutura Android (ex: `AndroidManifest.xml`, `res/values`, `res/xml`) e permitido.

## 6) Estado, eventos e navegacao

- Toda acao de usuario deve virar evento tipado (sealed interface/class).
- Toda navegacao deve acontecer via `UiEvent` emitido pela ViewModel.
- A UI coleta `uiEvent` via `LaunchedEffect` e executa efeitos de navegacao/snackbar.
- Padrao minimo por tela:
  - `UiState` imutavel (`data class`, apenas `val`).
  - `Evento` de acao de usuario (ex: `GanhosEvento`).
  - `UiEvent` para efeitos de UI (navegacao, snackbar).

## 7) Persistencia e fluxo de dados

- Room e obrigatorio para persistencia local.
- DAOs devem expor `Flow` para observacao reativa.
- Nao acessar banco diretamente na UI.
- Trate resultado de operacoes com `Result` usando `onSuccess`/`onFailure`.

## 8) ViewModel e coroutines

- ViewModel nao deve importar `androidx.compose.*`.
- Use `StateFlow` para estado de tela e `Channel`/`Flow` para eventos de efeito unico.
- Use `viewModelScope` para orquestracao assincrona.
- Evite `LiveData` em codigo novo.

## 9) Graficos

- Graficos devem ser implementados manualmente com `Canvas` no Compose.
- Nao adicionar biblioteca externa de graficos.

## 10) Qualidade de codigo

- Kotlin idiomatico, funcoes curtas, classes coesas.
- Nomeacao consistente:
  - Dominio em portugues (`Ganho`, `Sonho`, `ObterSaldoUseCase`).
  - Infraestrutura pode seguir convencoes tecnicas em ingles.
- Minimize complexidade ciclomatica; extraia funcoes auxiliares quando necessario.
- Comentarios apenas quando realmente agregarem contexto nao obvio.

## 11) Dependencias e build

- Use Version Catalog (`gradle/libs.versions.toml`) para novas dependencias.
- Antes de adicionar biblioteca, valide necessidade e impacto arquitetural.
- Nao introduzir framework que conflite com MVVM + Compose + Room + Hilt.

## 12) Checklist para agentes antes de abrir PR

- Arquitetura da feature respeita MVVM de ponta a ponta.
- Navegacao acionada apenas por `UiEvent`.
- UI sem regra de negocio e sem acesso direto a DAO/repository.
- Room com DAO retornando `Flow` e camadas bem separadas.
- Componentes Compose reutilizaveis quando houver repeticao.
- Build e testes relevantes executados com sucesso.

## 13) Anti-padroes (nao fazer)

- Colocar logica de negocio em Composable.
- Navegar direto da UI sem intermediar `UiEvent`.
- ViewModel depender de classes Compose.
- Acessar banco de dados diretamente em tela/Composable.
- Duplicar componentes visuais que poderiam ser extraidos.
- ViewModel injetar repositorio diretamente (deve injetar use case).
- Colocar regra de negocio ou validacao na interface do use case (interface so define o contrato; logica fica no `*Impl`).
- Implementar regra de negocio diretamente na classe `*UseCaseImpl` sem a interface correspondente.

## 14) Prioridade em caso de conflito

Em caso de divergencia entre instrucoes, siga esta ordem:

1. Este `AGENTS.md`.
2. Regras explicitas do usuario na tarefa atual.
3. Convencoes ja consolidadas no codigo.

Se ainda houver ambiguidade, documente a suposicao no PR/descricao da mudanca.
