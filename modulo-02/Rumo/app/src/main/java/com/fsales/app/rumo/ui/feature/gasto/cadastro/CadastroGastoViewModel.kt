package com.fsales.app.rumo.ui.feature.gasto.cadastro

import androidx.lifecycle.ViewModel
import com.fsales.app.rumo.core.domain.usecase.SalvarGastoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CadastroGastoViewModel @Inject constructor(
    private val salvarGastoUseCase: SalvarGastoUseCase
): ViewModel() {
}