package com.fsales.app.rumo.ui

import androidx.annotation.StringRes


/** Ganho */
sealed interface ListaGanhoUiEvent {
    data class ShowSnackbar(@param:StringRes val resId: Int)
    data object NavigateToCadastro : ListaGanhoUiEvent
}

sealed interface CadastroGanhoUiEvent {
    data object NavigateBack : CadastroGanhoUiEvent
}

/** Gasto */
sealed interface ListaGastoUiEvent {
    data class ShowSnackbar(@param:StringRes val resId: Int)
    data object NavigateToCadastro : ListaGastoUiEvent
}

sealed interface CadastroGastoUiEvent {
    data object NavigateBack : CadastroGastoUiEvent
}

/** Sonho */
sealed interface ListaSonhoUiEvent {
    data object NavigateBack : ListaSonhoUiEvent
}

/** Home */
sealed interface HomeUiEvent {
    data object NavigateToListaGanho : HomeUiEvent
    data object NavigateToListaGasto : HomeUiEvent
    data object NavigateToListaSonho : HomeUiEvent
}


