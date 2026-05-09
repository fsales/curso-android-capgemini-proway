# 📱 SmartContact

<!-- Badges -->
![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue?logo=kotlin)
![Android](https://img.shields.io/badge/Android-Compose-green?logo=android)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-blueviolet)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange)
![Database](https://img.shields.io/badge/Database-Room-red)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

---

## 📖 Sobre o App

**SmartContact** é um aplicativo Android para **gerenciamento de contatos**. Permite ao usuário cadastrar, editar, buscar e organizar contatos pessoais e profissionais de forma simples e eficiente.

---

## 🎯 Objetivo do Projeto

- Centralizar e organizar contatos

---

## 🧭 Funcionalidades

- Cadastro, edição e exclusão de contatos
- Interface moderna com tema claro e escuro

---

## 🧱 Arquitetura

O app segue o padrão **MVVM (Model–View–ViewModel)** para garantir separação de responsabilidades e escalabilidade.

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
- **Data:** Repositórios e persistência local (Room)

---

## 🧭 Navegação e Eventos

A navegação e ações da UI são tratadas via eventos, mantendo a interface desacoplada da lógica de negócio.

---

## 🎨 UI & Design

- Jetpack Compose
- Material Design 3
- Componentes reutilizáveis
- Tema claro e escuro
- UI reativa baseada em estado

---

## 🗄️ Persistência de Dados

- Room Database para armazenar contatos localmente
- Flow para observação reativa dos dados

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

## 📄 Licença

Este projeto é destinado a fins educacionais e de portfólio.
