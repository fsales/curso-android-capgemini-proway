# AppContas

![Kotlin](https://img.shields.io/badge/Kotlin-2.2-blueviolet?logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-Module-3DDC84?logo=android&logoColor=white)
![Gradle KTS](https://img.shields.io/badge/Gradle-KTS-brightgreen?logo=gradle&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)

Projeto multi-módulo em Kotlin com foco em gerenciamento de contas (receitas e despesas).

## Quick Start

No diretório raiz do projeto:

```powershell
.\gradlew clean build
.\gradlew :app:assembleDebug
```

Para executar o fluxo funcional atual, rode o `Main.kt` do módulo `appconta-core` pela IDE.

## Visão geral

O repositório possui dois módulos:

- **app**: módulo Android (estrutura base, sem telas/Activity implementadas no momento).
- **appconta-core**: regra de negócio e fluxo em console (CRUD de contas, pesquisa, cálculo de saldo e validações).

## Tecnologias

- Kotlin (JVM)
- Gradle (Kotlin DSL)
- Android Gradle Plugin
- AndroidX / Material (no módulo `app`)

## Estrutura

```text
AppContas/
├─ app/                  # Módulo Android
├─ appconta-core/        # Núcleo de negócio (console)
├─ build.gradle.kts      # Configuração raiz
├─ settings.gradle.kts   # Inclusão dos módulos
└─ gradlew(.bat)         # Wrapper do Gradle
```

## Pré-requisitos

- JDK 17
- Android Studio (para trabalhar com o módulo Android)
- Windows PowerShell ou terminal compatível

## Como compilar

No diretório raiz do projeto:

```powershell
.\gradlew clean build
```

Comandos úteis por módulo:

```powershell
# Build do core
.\gradlew :appconta-core:build
```

## Execução

Atualmente, o ponto de entrada funcional está no módulo `appconta-core`, arquivo `Main.kt`.

A forma mais simples de executar é pela IDE (Run em `Main.kt`):

- Pacote: `br.com.app.contas`
- Classe: `MainKt`

## Funcionalidades do core

- Cadastrar conta (receita/despesa)
- Listar contas em tabela
- Pesquisar por ID, descrição ou tipo
- Alterar conta
- Remover conta
- Exibir saldo consolidado
- Validar tipo, valor, ID e data (`dd/MM/yyyy`)

## Observações

- O módulo `app` está com configuração base e ainda não expõe fluxo de UI.
- O módulo `appconta-core` usa armazenamento em memória (sem persistência em banco).