package com.fsales.app.rumo.ui

import androidx.annotation.StringRes


/** Ganho */
sealed interface ListaGanhoUiEvent {
    data class ShowSnackbar(@param:StringRes val resId: Int)
    data object NavigateToCadastro : ListaGanhoUiEvent
}


/** Gasto */
sealed interface ListaGastoUiEvent {
    data class ShowSnackbar(@param:StringRes val resId: Int)
    data object NavigateToCadastro : ListaGastoUiEvent
}


/** Sonho */
sealed interface ListaSonhoUiEvent {
    data object NavigateToCadastro : ListaSonhoUiEvent
    data class NavigateToDetalhe(val sonhoId: Long) : ListaSonhoUiEvent
}


/** Home */
sealed interface HomeUiEvent {
    data object NavigateToSaldo      : HomeUiEvent
    data object NavigateToExtrato    : HomeUiEvent
    data object NavigateToListaGanho : HomeUiEvent
    data object NavigateToListaGasto : HomeUiEvent
    data object NavigateToListaSonho : HomeUiEvent
}
