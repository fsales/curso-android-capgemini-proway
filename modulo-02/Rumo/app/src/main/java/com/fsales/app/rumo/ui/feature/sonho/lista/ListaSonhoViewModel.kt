package com.fsales.app.rumo.ui.feature.sonho.lista

import androidx.lifecycle.ViewModel
import com.fsales.app.rumo.core.domain.usecase.ListarSonhosUseCase
import com.fsales.app.rumo.ui.ListaSonhoUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class ListaSonhoViewModel @Inject constructor(
    private val repository: ListarSonhosUseCase
): ViewModel() {
    private val _uiEvent = Channel<ListaSonhoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()
}