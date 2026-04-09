# 📱 Projeto Rumo — Plano de Desenvolvimento

## 🧭 Visão Geral

Rumo é um aplicativo Android de gerenciamento de despesas pessoais.
O objetivo é ajudar o usuário a compreender seu saldo financeiro e planejar sonhos e objetivos de longo prazo.

---

## ✅ Funcionalidades Principais

### 1. Ganhos
- Cadastro de ganhos mensais
- Valor, descrição e data
- Soma automática no saldo

### 2. Gastos
- Registro de despesas
- Classificação por data
- Subtração automática do saldo

### 3. Sonhos / Desejos
- Cadastro de objetivos financeiros (ex: viagem, imóvel, aposentadoria)
- Valor objetivo
- Visualização de progresso com base no saldo atual

### 4. Página Inicial (Home)
- Exibição do saldo atual
- Total de ganhos e gastos
- Gráfico visual (pizza) de Ganhos x Gastos

---

## 🧱 Arquitetura

O projeto segue o padrão **MVVM**, com separação clara de responsabilidades.

```
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

## 🧩 Diretrizes Técnicas

- Linguagem: Kotlin
- UI: Jetpack Compose
- Design: Material Design 3
- Arquitetura: MVVM
- Persistência: Room
- Gerenciamento de Estado: State + Flow
- Navegação desacoplada com UiEvent

---

## 📊 Gráficos

- Gráfico de pizza implementado com Canvas no Jetpack Compose
- Referência oficial:
  https://medium.com/@ahmed_shehata/compose-pie-chart-circle-deep-understanding-d09f1352043c

---

## ✅ Boas Práticas

- Criar componentes reutilizáveis sempre que possível
- UI não deve conter regras de negócio
- ViewModels não acessam API de UI
- Estados imutáveis com data class
- Navegação somente via UiEvent

---

## 🛣️ Roadmap

- [ ] Testes de ViewModel
- [ ] DataStore para preferências
- [ ] Filtro por período (mês / ano)
- [ ] Modularização futura