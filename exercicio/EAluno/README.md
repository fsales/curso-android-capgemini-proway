# E-Aluno

Aplicativo Android para gerenciamento e consulta de cadastro de alunos.

![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-7F52FF?logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-API%2026%2B-3DDC84?logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.03.01-4285F4?logo=jetpackcompose&logoColor=white)
![Hilt](https://img.shields.io/badge/Hilt-2.59.2-FF6F00)

---

## Sobre o Projeto

O **E-Aluno** é um aplicativo Android desenvolvido como exercício prático do curso de Android da **Capgemini/ProWay**. Seu objetivo é demonstrar a construção de um app moderno com Kotlin, Jetpack Compose e as principais bibliotecas do ecossistema Android, seguindo boas práticas de arquitetura.

O app permite visualizar uma lista de alunos cadastrados, com separação entre alunos ativos e inativos, e navegar para a tela de detalhe de cada aluno.

---

## Telas e Funcionalidades

### Listagem de Alunos

A tela principal exibe todos os alunos cadastrados organizados em duas seções:

- **Ativos** — alunos com matrícula ativa.
- **Inativos** — alunos com matrícula encerrada.

Cada seção pode ser expandida ou recolhida individualmente. A lista é rolável e conta com um botão de ação flutuante (FAB) que, ao ser acionado, retorna o scroll ao topo da lista. Alunos inativos são identificados visualmente com texto tachado.

### Detalhe do Aluno

Ao selecionar um aluno na lista, o app navega para a tela de detalhe, que exibe todos os campos do cadastro: nome completo, e-mail, telefone, matrícula, curso, turno, semestre, data de nascimento e situação. A tela trata os estados de carregamento (indicador de progresso) e de registro não encontrado. Um gesto ou botão de voltar retorna à listagem.

### Dados de Demonstração

O app utiliza 80 alunos gerados em memória, cobrindo diferentes combinações de curso, turno, semestre e situação (ativo/inativo). Alguns alunos possuem foto de perfil carregada via URL; os demais exibem um avatar com a inicial do nome.

---

## Arquitetura

O projeto segue o padrão **MVVM (Model-View-ViewModel)** com **Fluxo de Dados Unidirecional (UDF)**. A UI apenas observa o estado exposto pelo ViewModel e despacha eventos; o ViewModel coordena a lógica e acessa a camada de dados sem conhecer detalhes de apresentação.

```
┌─────────────────────────────────────────┐
│                   UI                    │  Composables (stateless) + Screen
│         observa estado, dispara eventos │
└────────────────────┬────────────────────┘
                     │ UiEvent / UiState
┌────────────────────▼────────────────────┐
│              ViewModel                  │  StateFlow de estado + Flow de eventos
│         coordena lógica de negócio      │
└────────────────────┬────────────────────┘
                     │ suspend / Flow
┌────────────────────▼────────────────────┐
│               Domain                   │  Interfaces (contratos) + modelos puros
└────────────────────┬────────────────────┘
                     │ implementação via DI
┌────────────────────▼────────────────────┐
│                 Data                    │  DAOs, repositórios, entidades, módulo Hilt
└─────────────────────────────────────────┘
```

### Camada UI

Composta por Composables construídos com Jetpack Compose e Material 3. Cada feature possui uma **Screen** (conectada ao ViewModel via injeção) e um **Content** stateless que recebe apenas dados e callbacks. O tema centralizado (`AppTheme`) suporta cores dinâmicas no Android 12+ e esquema estático em versões anteriores, com suporte a dark mode. O sistema de espaçamento é definido via `MaterialTheme.spacing`.

### Camada ViewModel

Cada tela possui seu próprio `ViewModel` anotado com `@HiltViewModel`. O ViewModel expõe:

- Um **`StateFlow` de estado de UI** — imutável, observado pela Screen para renderizar os Composables.
- Um **`Flow` de eventos de UI** — ações pontuais como navegar para outra tela ou exibir um Snackbar.

O ViewModel não depende de nada específico da UI; conhece apenas os contratos da camada de domínio.

### Camada de Domínio

Define os **contratos** (interfaces) e os **modelos de negócio** puros em Kotlin, sem dependência de Android ou de frameworks. É a camada mais estável do sistema.

- `AlunoRepository` — contrato de acesso aos dados de alunos.
- `Aluno` — modelo de domínio central.
- `PeriodoTurno` e `Semestre` — enums com valores de negócio.

### Camada de Dados

Implementa os contratos definidos no domínio. Atualmente utiliza uma fonte de dados em memória (`AlunoFakeDao`) que simula um banco de dados, o que facilita a substituição futura por uma implementação real (Room, Retrofit, etc.) sem impacto nas camadas superiores.

O mapeamento entre a entidade de dados (`AlunoEntity`) e o modelo de domínio (`Aluno`) ocorre no repositório (`AlunoRepositoryImpl`).

### Injeção de Dependências (Hilt)

A injeção de dependências é gerenciada pelo **Hilt**:

- `ApplicationApp` — ponto de entrada da aplicação, anotado com `@HiltAndroidApp`.
- `MainActivity` — anotada com `@AndroidEntryPoint` para receber injeções.
- ViewModels — anotados com `@HiltViewModel`; o Hilt os fornece via `hiltViewModel()`.
- `DataModule` — módulo Hilt que vincula as implementações concretas `AlunoFakeDao` e `AlunoRepositoryImpl` às interfaces `AlunoDao` e `AlunoRepository`, respectivamente.

---

## Navegação

A navegação é implementada com **Jetpack Navigation 3**, a versão mais recente da biblioteca de navegação para Compose.

- O grafo de navegação é definido em `EAlunoNavHost` utilizando `NavDisplay` e `rememberNavBackStack`.
- As rotas são classes/objetos Kotlin anotados com `@Serializable`, garantindo type-safety:
  - `ListaRoute` — rota para a tela de listagem (sem parâmetros).
  - `DetalheRoute(id: Long)` — rota para a tela de detalhe, recebendo o identificador do aluno.
- O back stack é gerenciado automaticamente pelo `NavDisplay`.

---

## Modelo de Domínio

### Aluno

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `Long` | Identificador único |
| `nome` | `String` | Primeiro nome |
| `sobrenome` | `String` | Sobrenome |
| `email` | `String` | Endereço de e-mail |
| `telefone` | `String` | Número de telefone |
| `matricula` | `String` | Código de matrícula |
| `curso` | `String` | Nome do curso |
| `turno` | `PeriodoTurno` | Turno das aulas |
| `semestre` | `Semestre` | Semestre atual |
| `dataNascimento` | `LocalDate` | Data de nascimento |
| `ativo` | `Boolean` | Indica se a matrícula está ativa |
| `fotoUrl` | `String?` | URL da foto de perfil (opcional) |

### PeriodoTurno

| Valor | Descrição |
|---|---|
| `MATUTINO` | Período da manhã |
| `VESPERTINO` | Período da tarde |
| `NOTURNO` | Período da noite |

### Semestre

| Valor | Valor |
|---|---|
| `PRIMEIRO` | `QUINTO` |
| `SEGUNDO` | `SEXTO` |
| `TERCEIRO` | `SETIMO` |
| `QUARTO` | `OITAVO` |

---

## Stack Tecnológica

| Tecnologia | Versão | Propósito |
|---|---|---|
| Kotlin | 2.3.20 | Linguagem principal |
| Android Gradle Plugin | 9.1.0 | Build do projeto Android |
| KSP | 2.3.6 | Processamento de anotações (Kotlin Symbol Processing) |
| Jetpack Compose BOM | 2026.03.01 | Toolkit de UI declarativa |
| Material 3 | (via BOM) | Sistema de design e componentes visuais |
| Material Icons | (via BOM) | Ícones de UI (core + extended) |
| Hilt | 2.59.2 | Injeção de dependências |
| Hilt Navigation Compose | 1.3.0 | Integração Hilt com ViewModels em Compose |
| Navigation 3 | 1.0.1 | Navegação type-safe entre telas |
| Lifecycle / ViewModel Compose | 2.10.0 | ViewModel, StateFlow e ciclo de vida |
| Coil 3 | 3.1.0 | Carregamento assíncrono de imagens |
| Kotlinx Serialization | 1.10.0 | Serialização das rotas de navegação |

---

## Estrutura de Pacotes

```
com.fsales.app.e_aluno
├── config/               — ApplicationApp (@HiltAndroidApp)
├── navigation/           — EAlunoNavHost, ListaRoute, DetalheRoute
├── domain/
│   ├── AlunoRepository   — contrato de acesso a dados
│   └── model/            — Aluno, PeriodoTurno, Semestre
├── data/
│   ├── AlunoDao          — contrato do DAO
│   ├── AlunoFakeDao      — implementação in-memory
│   ├── AlunoRepositoryImpl
│   ├── entity/           — AlunoEntity (modelo de persistência)
│   └── di/               — DataModule (bindings Hilt)
└── ui/
    ├── UiEvent           — sealed interface de eventos de UI
    ├── theme/            — AppTheme, cores, tipografia, espaçamento
    ├── components/       — AlunoItem, AlunoCard, AlunoAvatar, DetailField, ListaSectionHeader
    ├── mapper/           — extensões @Composable para enums (PeriodoTurno, Semestre)
    ├── preview/          — PreviewData (dados para @Preview do Compose)
    └── feature/
        ├── lista/        — Screen, ViewModel, UiState, Evento, ScreenState
        └── detalhe/      — Screen, ViewModel, UiState
```

---

## Pré-requisitos

- **Android Studio** Narwhal (ou superior, compatível com AGP 9.x)
- **JDK 11** ou superior
- **Android SDK** API nível 36

---

## Como Construir e Executar

O projeto utiliza o Gradle Wrapper. Todos os comandos devem ser executados na raiz do repositório.

| Ação | Comando (Windows) |
|---|---|
| Compilar APK de debug | `gradlew.bat :app:assembleDebug` |
| Executar testes unitários | `gradlew.bat :app:testDebugUnitTest` |
| Executar testes instrumentados | `gradlew.bat :app:connectedDebugAndroidTest` |
| Executar análise de lint | `gradlew.bat :app:lintDebug` |

> Os testes instrumentados requerem um emulador ou dispositivo físico conectado.

---

## Testes

O projeto possui a estrutura de testes configurada, mas as implementações ainda são os templates gerados automaticamente pelo Android Studio:

- **Testes unitários** — `app/src/test/`
- **Testes instrumentados** — `app/src/androidTest/`

A adição de cobertura de testes real é uma evolução futura do projeto.
