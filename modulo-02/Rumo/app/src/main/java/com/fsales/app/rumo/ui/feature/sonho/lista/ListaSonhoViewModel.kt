package com.fsales.app.rumo.ui.feature.sonho.lista

import androidx.lifecycle.ViewModel
import com.fsales.app.rumo.core.domain.usecase.ListarSonhosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListaSonhoViewModel @Inject constructor(
    private val repository: ListarSonhosUseCase
): ViewModel() {
}