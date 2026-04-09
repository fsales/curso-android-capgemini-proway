# 📱 Rumo

<!-- Badges -->
![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue?logo=kotlin)
![Android](https://img.shields.io/badge/Android-Compose-green?logo=android)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-blueviolet)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange)
![Database](https://img.shields.io/badge/Database-Room-red)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

---

## 📖 Sobre o App

**Rumo** é um aplicativo Android de **gerenciamento financeiro pessoal**, focado em ajudar o usuário a entender **para onde o dinheiro está indo** e **qual caminho seguir para alcançar seus sonhos**.

O app permite registrar **ganhos**, **gastos** e **objetivos financeiros**, exibindo o **saldo atual**, **gráficos visuais** e o **progresso** rumo aos objetivos definidos.

---

## 🎯 Objetivo do Projeto

- Centralizar ganhos e gastos mensais
- Exibir o saldo disponível de forma clara
- Auxiliar no planejamento de sonhos e objetivos financeiros
- Aplicar boas práticas modernas de desenvolvimento Android
- Servir como projeto de portfólio profissional

---

## 🧭 Funcionalidades

### ✅ Ganhos
- Cadastro de ganhos
- Listagem de registros
- Atualização automática do saldo

### ✅ Gastos
- Registro de despesas
- Organização por data
- Impacto direto no saldo

### ✅ Sonhos / Desejos
- Criação de objetivos financeiros (viagem, imóvel, aposentadoria, etc.)
- Cálculo automático de progresso
- Indicação se o sonho é realizável com o saldo atual

### ✅ Página Inicial (Home)
- Saldo atual
- Total de ganhos e gastos
- Gráfico de pizza (Ganhos x Gastos)

---

## 🧱 Arquitetura

O app segue o padrão **MVVM (Model–View–ViewModel)**, garantindo separação de responsabilidades e escalabilidade.

```text
UI (Jetpack Compose)
 ↓
ViewModel (State + Events)
 ↓
UseCases (Regras de Negócio)
 ↓
Repository
 ↓
Room Database
```

---

## 📦 Camadas

- **UI:** Jetpack Compose + Material Design 3
- **ViewModel:** Estado da UI e eventos
- **Domain:** Regras de negócio (UseCases)
- **Data:** Repositórios e persistência local

---

## 🧭 Navegação e Eventos (UiEvent)

A navegação é desacoplada da UI utilizando **sealed interfaces**.

```kotlin
sealed interface UiEvent {
    data class ShowSnackbar(@StringRes val resId: Int) : UiEvent
}

sealed interface DetalhesUiEvent : UiEvent {
    data object NavigateBack : DetalhesUiEvent
}
```

### Coleta na UI

```kotlin
LaunchedEffect(viewModel) {
    viewModel.uiEvent.collect { event ->
        if (event is DetalhesUiEvent.NavigateBack) {
            navigateBack()
        }
    }
}
```

---

## 🎯 Ações da UI

As ações do usuário são tratadas via eventos, mantendo a UI simples e desacoplada.

```kotlin
sealed interface ListaEvento {
    data class Detalhes(val id: Long) : ListaEvento
}

fun onEvent(event: ListaEvento) {
    when (event) {
        is ListaEvento.Detalhes -> detalhe(event.id)
    }
}
```

---

## 🎨 UI & Design

- ✅ Jetpack Compose
- ✅ Material Design 3
- ✅ Componentes reutilizáveis
- ✅ Tema claro e escuro
- ✅ UI reativa baseada em estado

---

## 📊 Gráficos

A tela inicial apresenta um **gráfico de pizza** (Ganhos x Gastos), implementado com **Canvas no Compose**.

📚 **Referência utilizada:**
https://medium.com/@ahmed_shehata/compose-pie-chart-circle-deep-understanding-d09f1352043c

---

## 🗄️ Persistência de Dados

- Room Database
- Flow para observação reativa dos dados
- Entidades separadas para:
  - Movimentações (ganhos / gastos)
  - Sonhos (objetivos financeiros)

---

## 🧰 Tecnologias Utilizadas

- Kotlin
- Jetpack Compose
- Material Design 3
- ViewModel / Lifecycle
- Room
- Coroutines / Flow
- Navigation Compose

---

## 🗂️ Estrutura de Pacotes

```text
com.fabio.rumo
│
├── data
│   ├── local
│   └── repository
│
├── domain
│   ├── model
│   └── usecase
│
├── ui
│   ├── home
│   ├── ganhos
│   ├── gastos
│   ├── sonhos
│   └── components
│
└── navigation
```

---

## 🛣️ Roadmap Futuro

- ⬜ Testes unitários de ViewModel
- ⬜ DataStore para preferências
- ⬜ Filtro por período (mês/ano)
- ⬜ Exportação de dados
- ⬜ Modularização

---

## 👨‍💻 Autor

**Fabio de Oliveira**  
Arquiteto de Soluções • Desenvolvedor Android

---

## 📄 Licença

Este projeto é destinado a fins educacionais e de portfólio.

> 🤖 Este projeto possui regras explícitas para ferramentas de IA definidas em `Agent.md`.