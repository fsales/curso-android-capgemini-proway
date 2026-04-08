# AGENTS.md

## Escopo
- Aplica-se a todo o repositório (`EAluno/`).
- Nenhum arquivo prévio de orientação para IA foi encontrado no glob solicitado; este é o guia canônico.

## Visão Geral do Projeto
- App Android com um único módulo Gradle: `:app` (`settings.gradle.kts`).
- Base em Kotlin + Jetpack Compose + Material 3.
- Duas features MVVM completas implementadas: listagem de alunos e detalhe de aluno.
- Cadeia de entrada atual: `app/src/main/AndroidManifest.xml` → `ApplicationApp` → `MainActivity` → `EAlunoNavHost` → telas.

## Arquitetura Atual e Direção
- Host de UI atual: uma `@AndroidEntryPoint ComponentActivity` em `app/src/main/java/com/fsales/app/e_aluno/MainActivity.kt`; inicializa Compose, aplica `AppTheme` e chama `EAlunoNavHost()`.
- Ponto de entrada da aplicação: `ApplicationApp` (`@HiltAndroidApp`) no pacote `config`; registrada como `android:name` no `AndroidManifest.xml`.
- **Arquitetura MVVM implementada** com Hilt para injeção de dependências. Estrutura por feature:
  - `ui` — Composables (screen + content stateless)
  - `viewmodel` — `ViewModel` com `StateFlow` de estado e `Flow` de eventos (`UiEvent`)
  - `data` — DAOs, repositórios e módulo Hilt (`DataModule`)
- Fluxo: `ui` observa state do `ViewModel` e dispara events; `ViewModel` coordena regras e acesso à camada `data`.

### Navegação
- `app/src/main/java/com/fsales/app/e_aluno/navigation/EAlunoNavHost.kt`
  - Usa Navigation 3 (`NavDisplay` + `rememberNavBackStack`).
  - Rotas `@Serializable`: `ListaRoute` (object) e `DetalheRoute(id: Long)`.

### Features Implementadas

**`ui/feature/lista/`** — Listagem de alunos
- `ListaAlunoScreen` — screen conectada ao ViewModel via `hiltViewModel()`
- `ListaAlunoContent` — stateless; `LazyColumn` com sticky headers (ativos/inativos), FAB scroll-to-top
- `ListaAlunoUiState` — `data class(ativos: List<Aluno>, inativos: List<Aluno>)`
- `ListaAlunoViewModel` — `@HiltViewModel`; expõe `uiState: StateFlow` e `uiEvent: Flow`
- `ListaEvento` — sealed interface: `Detalhes(id: Long)`
- `ListaAlunoScreenState` — `@Stable`; controla `LazyListState` e colapso de seções

**`ui/feature/detalhe/`** — Detalhe de aluno
- `DetalheAlunoScreen` — carrega dados via `LaunchedEffect(id)`, trata `NavigateBack`
- `DetalheAlunoContent` — stateless; mostra spinner, not-found ou `DetalheAlunoBody`
- `DetalheAlunoUiState` — `data class(isLoading, aluno, isNotFound)`
- `DetalheAlunoViewModel` — `@HiltViewModel`; `load(id: Long)` com deduplicação por `loadedId`

### Camada de Domínio (`domain/`)
- `AlunoRepository` — interface: `getAll(): Flow<List<Aluno>>`, `suspend getBy(id: Long): Result<Aluno?>`
- `model/Aluno` — data class com todos os campos do aluno
- `model/PeriodoTurno` — enum (MATUTINO, VESPERTINO, NOTURNO) com `fromValue()`
- `model/Semestre` — enum (PRIMEIRO–OITAVO) com `fromValue()`

### Camada de Dados (`data/`)
- `AlunoDao` — interface: `getAll(): Flow<List<AlunoEntity>>`, `suspend getBy(id: Long): AlunoEntity?`
- `AlunoFakeDao` — `@Inject constructor() : AlunoDao`; gera 80 alunos em memória
- `AlunoRepositoryImpl` — `@Inject constructor(dao: AlunoDao)`; mapeia entidades para domínio
- `entity/AlunoEntity` — data class espelho de `Aluno` com `turno`/`semestre` como `String`
- `di/DataModule` — `@Module @InstallIn(SingletonComponent::class)`; `@Binds` para `AlunoRepository` e `AlunoDao`

### Componentes e Utilitários de UI
- `ui/components/`: `AlunoItem`, `AlunoCard`, `AlunoAvatar` (Coil `AsyncImage` + fallback de inicial), `DetailField`, `ListaSectionHeader`
- `ui/mapper/EnumUiText.kt`: extensões `@Composable` para `PeriodoTurno.toUiText()` e `Semestre.toUiText()`
- `ui/preview/PreviewData`: objeto com 20 `Aluno` para previews do Compose
- `ui/UiEvent`: sealed interface com `ShowSnackbar`; subtipos `ListUiEvent` e `DetalhesUiEvent`

### Sistema de Tema (`ui/theme/`)
- `Theme.kt`: `AppTheme`; esquema dinâmico (Android 12+) ou estático (light/dark).
- `Color.kt`: tokens de cor Material 3 gerados (light/dark + variantes de contraste).
- `Type.kt`: expõe `AppTypography`.
- `Spacing.kt`: `Spacing` data class + extensão `MaterialTheme.spacing`; usado em todos os composables.

## Limites de Dependências e Integrações
- Versões são centralizadas em `gradle/libs.versions.toml`; priorize adicionar/atualizar dependências ali.
- Integrações já habilitadas em `app/build.gradle.kts`:
  - Compose (`buildFeatures.compose = true`, BOM do Compose).
  - Navigation 3 (`androidx.navigation3.*`) adicionada, conectada via `EAlunoNavHost` com rotas `@Serializable`.
  - Hilt + KSP configurados para dependency injection (`hilt-android`, `ksp(hilt-android-compiler)`), com `@HiltAndroidApp` em `ApplicationApp`, `@AndroidEntryPoint` em `MainActivity` e `@HiltViewModel` nos ViewModels.
  - Kotlinx Serialization em uso nas rotas `ListaRoute` e `DetalheRoute`.
  - Coil 3 (`coil-compose`, `coil-network-okhttp`): carregamento assíncrono de imagens, usado em `AlunoAvatar`.
  - Material Icons (`material-icons-core`, `material-icons-extended`): ícones de UI nos composables.
- O manifest declara a permissão `INTERNET` (necessária para o Coil carregar imagens via rede).
- Para novas features MVVM, usar dependency injection com Hilt para fornecer dependências do `ViewModel` e da camada `data`.

## Workflows de Build e Teste
- Use o wrapper Gradle na raiz (`gradlew.bat` no Windows).
- Comandos comuns:
  - `./gradlew.bat :app:assembleDebug`
  - `./gradlew.bat :app:testDebugUnitTest`
  - `./gradlew.bat :app:connectedDebugAndroidTest` (requer emulador/dispositivo)
  - `./gradlew.bat :app:lintDebug`
- Testes atuais são apenas templates:
  - Unitário: `app/src/test/java/com/fsales/app/e_aluno/ExampleUnitTest.kt`
  - Instrumentado: `app/src/androidTest/java/com/fsales/app/e_aluno/ExampleInstrumentedTest.kt`

## Convenções do Código
- Manter o package raiz `com.fsales.app.e_aluno` para novos fontes Kotlin.
- Texto visível ao usuário deve ir para `app/src/main/res/values/strings.xml`.
- Reutilizar `AppTheme` e `AppTypography`; evitar novos roots de tema ad hoc.
- Seguir o estilo atual de plugins/dependências com aliases do version catalog.
- Restrições de toolchain em `app/build.gradle.kts`: `minSdk=26`, `targetSdk=36`, compatibilidade Java 11.
- Organizar novas features no padrão `ui/feature/<nome>/` com separação em `ui` (Composable), `viewmodel` (state/events) e `data` (data sources/modelos) — convenção ativa, seguir como nas features existentes.



