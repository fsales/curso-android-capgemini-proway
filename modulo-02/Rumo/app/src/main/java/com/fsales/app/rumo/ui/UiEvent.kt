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

sealed interface CadastroGastoUiEvent {
    data object NavigateBack   : CadastroGastoUiEvent
    data object ErroAoSalvar   : CadastroGastoUiEvent
}

/** Sonho */
sealed interface ListaSonhoUiEvent {
    data object NavigateToCadastro : ListaSonhoUiEvent
}

sealed interface CadastroSonhoUiEvent {
    data object NavigateBack   : CadastroSonhoUiEvent
    data object ErroAoSalvar   : CadastroSonhoUiEvent
}

/** Home */
sealed interface HomeUiEvent {
    data object NavigateToListaGanho : HomeUiEvent
    data object NavigateToListaGasto : HomeUiEvent
    data object NavigateToListaSonho : HomeUiEvent
}


