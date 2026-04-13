package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.ItemExtrato
import com.fsales.app.rumo.core.domain.repository.GanhoRepository
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import com.fsales.app.rumo.core.domain.usecase.ListarExtratoPorMesUseCase
import com.fsales.app.rumo.core.domain.usecase.validarCompetencia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ListarExtratoPorMesUseCaseImpl @Inject constructor(
    private val ganhoRepository: GanhoRepository,
    private val gastoRepository: GastoRepository,
) : ListarExtratoPorMesUseCase {

    override fun invoke(mesReferencia: Int, anoReferencia: Int): Flow<List<ItemExtrato>> {
        validarCompetencia(mesReferencia, anoReferencia)
        return combine(
            ganhoRepository.listarPorMes(mesReferencia, anoReferencia),
            gastoRepository.listarPorMes(mesReferencia, anoReferencia),
        ) { ganhos, gastos ->
            val itens = buildList {
                ganhos.forEach { add(ItemExtrato.GanhoItem(it)) }
                gastos.forEach { add(ItemExtrato.GastoItem(it)) }
            }
            // Ordena por data DESC; mesma data: id DESC para consistência visual
            itens.sortedWith(
                compareByDescending<ItemExtrato> { it.data }
                    .thenByDescending { it.id }
            )
        }
    }
}
