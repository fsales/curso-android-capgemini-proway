package com.fsales.app.rumo.ui

import androidx.annotation.StringRes


/** Ganho */
sealed interface ListaGanhoUiEvent {
    data class ShowSnackbar(@param:StringRes val resId: Int) : ListaGanhoUiEvent
    data object NavigateToCadastro : ListaGanhoUiEvent
    data class NavigateToDetalhe(val id: Long) : ListaGanhoUiEvent
}


/** Gasto */
sealed interface ListaGastoUiEvent {
    data class ShowSnackbar(@param:StringRes val resId: Int) : ListaGastoUiEvent
    data object NavigateToCadastro : ListaGastoUiEvent
    data class NavigateToDetalhe(val id: Long) : ListaGastoUiEvent
}


/** Sonho */
sealed interface ListaSonhoUiEvent {
    data object NavigateToCadastro : ListaSonhoUiEvent
    data class NavigateToDetalhe(val sonhoId: Long) : ListaSonhoUiEvent
}


/** Home */
sealed interface HomeUiEvent {
    data object NavigateToHome       : HomeUiEvent
    data object NavigateToExtrato    : HomeUiEvent
    data object NavigateToListaGanho : HomeUiEvent
    data object NavigateToListaGasto : HomeUiEvent
    data object NavigateToListaSonho : HomeUiEvent
}
