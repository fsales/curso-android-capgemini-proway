# 🤖 Agent Instructions — Projeto Rumo

Estas instruções guiam ferramentas de IA (ex: GitHub Copilot) e contribuidores humanos.
Todo código deve seguir obrigatoriamente estas regras.

---

## 📐 Arquitetura

Utilizar exclusivamente **MVVM**.

- UI:
  - Jetpack Compose
  - Apenas exibe estado
  - Envia eventos

- ViewModel:
  - Gerencia estado
  - Processa eventos
  - Emite UiEvent

- Domain:
  - UseCases isolados
  - Regras de negócio puras

- Data:
  - Repository
  - Room (DAO + Entities)

---

## 🎨 UI e Design

- Jetpack Compose obrigatório
- Material Design 3
- Componentização reutilizável
- Proibido XML

---

## 🧭 Navegação

Navegação deve ocorrer SOMENTE via UiEvent.

```kotlin
sealed interface UiEvent {
    data class ShowSnackbar(@StringRes val resId: Int) : UiEvent
}

sealed interface ListUiEvent : UiEvent {
    data class NavigateToDetalhes(val id: Long) : ListUiEvent
}

sealed interface DetalhesUiEvent : UiEvent {
    data object NavigateBack : DetalhesUiEvent
}
```

Coleta na UI:

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

Todas as ações do usuário devem ser modeladas com eventos.

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

## 🗄️ Persistência

- Room obrigatório
- DAOs retornam Flow
- Nenhuma chamada de banco na UI
- Usar o Result com onsuccess e onerror

---

## 📊 Gráficos

- Canvas no Compose
- Nenhuma library externa
- Implementação manual

---

## ✅ Código Esperado

- Kotlin idiomático
- Funções pequenas
- Classes coesas
- Sem lógica na UI
- Padrões consistentes em todo o projeto
- Criar componentes pequenos e reutilizaveis do compose