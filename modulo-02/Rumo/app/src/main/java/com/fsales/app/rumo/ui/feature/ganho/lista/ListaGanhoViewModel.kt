package com.fsales.app.rumo.ui.feature.ganho.lista

import androidx.lifecycle.ViewModel
import com.fsales.app.rumo.core.domain.usecase.ListarGanhosPorMesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListaGanhoViewModel @Inject constructor(
    private val repository: ListarGanhosPorMesUseCase
): ViewModel() {
}