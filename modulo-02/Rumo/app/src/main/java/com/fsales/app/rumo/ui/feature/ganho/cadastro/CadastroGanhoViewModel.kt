package com.fsales.app.rumo.ui.feature.ganho.cadastro

import androidx.lifecycle.ViewModel
import com.fsales.app.rumo.core.domain.usecase.SalvarGanhoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CadastroGanhoViewModel @Inject constructor(
    private val salvarGanhoUseCase: SalvarGanhoUseCase
): ViewModel() {
}