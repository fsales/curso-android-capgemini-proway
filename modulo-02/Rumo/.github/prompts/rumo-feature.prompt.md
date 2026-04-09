---
description: "Scaffold a new feature (Ganhos, Gastos, Sonhos or Home) for the Rumo expense-management Android app following MVVM, Room, Jetpack Compose and Material3 conventions."
name: "Rumo — Nova Funcionalidade"
argument-hint: "Nome da funcionalidade (ex: Ganhos, Gastos, Sonhos, Home)"
agent: "agent"
---

# Rumo — Scaffold de Funcionalidade

Gere o código completo para a funcionalidade **{{args}}** no aplicativo Android **Rumo** de gerenciamento de despesas pessoais.

---

## Contexto do Projeto

Rumo ajuda o usuário a acompanhar ganhos, gastos e objetivos financeiros (sonhos/desejos).  
Saldo = soma de Ganhos − soma de Gastos.

### Funcionalidades existentes / previstas

| Funcionalidade | Descrição |
|---|---|
| **Ganhos** | Cadastro de ganhos com valor, descrição e data; somados automaticamente ao saldo |
| **Gastos** | Registro de despesas com valor, descrição e data; subtraídos do saldo |
| **Sonhos / Desejos** | Objetivos financeiros (viagem, imóvel, aposentadoria…) com valor-meta e progresso baseado no saldo atual |
| **Home** | Saldo atual, totais de ganhos e gastos, gráfico de pizza (Ganhos × Gastos) |

---

## Diretrizes Técnicas

### Stack obrigatória
- **Linguagem**: Kotlin  
- **UI**: Jetpack Compose  
- **Design**: Material Design 3  
- **Arquitetura**: MVVM  
- **Persistência**: Room  
- **Estado**: StateFlow / Flow  

### Estrutura de pacotes por funcionalidade
```
com.fsales.app.rumo
└── <funcionalidade>/           # ex: ganhos, gastos, sonhos, home
    ├── data/
    │   ├── local/
    │   │   ├── <Entidade>Entity.kt      # @Entity Room
    │   │   └── <Entidade>Dao.kt         # @Dao Room
    │   └── repository/
    │       ├── <Entidade>Repository.kt  # interface
    │       └── <Entidade>RepositoryImpl.kt
    ├── domain/
    │   └── usecase/
    │       └── <Ação><Entidade>UseCase.kt
    ├── ui/
    │   ├── <Funcionalidade>Screen.kt    # @Composable raiz
    │   ├── <Funcionalidade>ViewModel.kt
    │   ├── <Funcionalidade>UiState.kt   # data class imutável
    │   └── components/                  # componentes reutilizáveis
    └── navigation/                      # rotas se necessário
```

---

## Padrões de Código

### 1. UiState — estado imutável
```kotlin
data class GanhosUiState(
    val ganhos: List<Ganho> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
```

### 2. UiEvent — navegação desacoplada
```kotlin
sealed interface UiEvent {
    data class ShowSnackbar(@StringRes val resId: Int) : UiEvent
}

sealed interface GanhosUiEvent : UiEvent {
    data class NavigateToDetalhes(val id: Long) : GanhosUiEvent
    data object NavigateBack : GanhosUiEvent
}
```

Coleta no Composable:
```kotlin
LaunchedEffect(viewModel) {
    viewModel.uiEvent.collect { event ->
        when (event) {
            is GanhosUiEvent.NavigateToDetalhes -> navigateToDetalhes(event.id)
            is GanhosUiEvent.NavigateBack -> navigateBack()
            is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(
                context.getString(event.resId)
            )
        }
    }
}
```

### 3. Evento de ação do usuário
```kotlin
sealed interface GanhosEvento {
    data class SelecionarGanho(val id: Long) : GanhosEvento
    data object AdicionarGanho : GanhosEvento
    data class ExcluirGanho(val id: Long) : GanhosEvento
}
```

### 4. ViewModel
```kotlin
@HiltViewModel
class GanhosViewModel @Inject constructor(
    private val obterGanhosUseCase: ObterGanhosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GanhosUiState())
    val uiState: StateFlow<GanhosUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<GanhosUiEvent>()
    val uiEvent: Flow<GanhosUiEvent> = _uiEvent.receiveAsFlow()

    fun onEvent(event: GanhosEvento) {
        when (event) {
            is GanhosEvento.SelecionarGanho -> navigateToDetalhes(event.id)
            GanhosEvento.AdicionarGanho -> { /* ... */ }
            is GanhosEvento.ExcluirGanho -> excluirGanho(event.id)
        }
    }

    private fun navigateToDetalhes(id: Long) {
        viewModelScope.launch {
            _uiEvent.send(GanhosUiEvent.NavigateToDetalhes(id))
        }
    }
}
```

### 5. Gráfico de Pizza (Home)
Implementar usando **Canvas do Jetpack Compose**, conforme a referência:  
https://medium.com/@ahmed_shehata/compose-pie-chart-circle-deep-understanding-d09f1352043c

```kotlin
@Composable
fun PieChart(
    slices: List<PieSlice>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        var startAngle = -90f
        slices.forEach { slice ->
            val sweep = 360f * slice.fraction
            drawArc(
                color = slice.color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true
            )
            startAngle += sweep
        }
    }
}

data class PieSlice(val fraction: Float, val color: Color, val label: String)
```

### 6. Componentes reutilizáveis
Sempre que um elemento de UI se repetir em duas ou mais telas, extraia para `ui/components/`.  
Exemplos: `ValorCard`, `TransacaoItem`, `EmptyStateMessage`, `LoadingIndicator`.

---

## O que gerar

Para a funcionalidade **{{args}}**, produza:

1. **Entity + DAO** (Room) — se a funcionalidade persistir dados  
2. **Repository** — interface + implementação  
3. **UseCase(s)** — um arquivo por caso de uso  
4. **UiState** — data class com todos os campos relevantes  
5. **UiEvent** — sealed interface com todos os eventos de navegação/feedback  
6. **Evento de ação** — sealed interface para ações do usuário  
7. **ViewModel** — com `onEvent`, `uiState` e `uiEvent`  
8. **Screen Composable** — usando Material3, coletando UiEvent, chamando `onEvent`  
9. **Componentes reutilizáveis** — extraídos em `components/` quando aplicável  
10. **Rota de navegação** — se a funcionalidade introduzir novas telas  

Para a **Home**, inclua também o `PieChart` e um card de saldo.

---

## Restrições

- ViewModels **não** importam nenhuma classe de `androidx.compose.*`  
- UI **não** contém regras de negócio  
- Toda navegação acontece via `UiEvent`  
- Estados são sempre **imutáveis** (`data class`, `val`)  
- Use `kotlinx.coroutines.flow.Flow` e `StateFlow`; evite `LiveData`  
- Siga as convenções de nomenclatura em **português** para domínio (entidades, use cases, eventos) e **inglês** para infraestrutura (Room, Compose internals)
