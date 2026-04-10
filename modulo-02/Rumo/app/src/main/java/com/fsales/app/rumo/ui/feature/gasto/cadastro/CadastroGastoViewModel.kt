package com.fsales.app.rumo.ui.feature.gasto.cadastro

import androidx.lifecycle.ViewModel
import com.fsales.app.rumo.core.domain.usecase.SalvarGastoUseCase
import com.fsales.app.rumo.ui.CadastroGastoUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class CadastroGastoViewModel @Inject constructor(
    private val salvarGastoUseCase: SalvarGastoUseCase
): ViewModel() {

    private val _uiEvent = Channel< CadastroGastoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()
}