package com.fsales.app.rumo.ui.feature.gasto.lista

import androidx.lifecycle.ViewModel
import com.fsales.app.rumo.core.domain.usecase.ListarGastosPorMesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListaGastoViewModel @Inject constructor(
    private val repository: ListarGastosPorMesUseCase
): ViewModel() {
}