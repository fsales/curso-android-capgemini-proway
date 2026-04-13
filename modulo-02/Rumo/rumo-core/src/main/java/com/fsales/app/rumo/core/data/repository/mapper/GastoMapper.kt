package com.fsales.app.rumo.core.data.repository.mapper

import com.fsales.app.rumo.core.data.room.entity.GastoEntity
import com.fsales.app.rumo.core.domain.model.CategoriaGasto
import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.data.room.entity.enums.CategoriaGasto as CategoriaGastoEntity

fun GastoEntity.toDomain(): Gasto = Gasto(
    id = id,
    descricao = descricao,
    valor = valor,
    dataGasto = dataGasto,
    mesReferencia = mesReferencia,
    anoReferencia = anoReferencia,
    categoria = categoria.toDomain(),
    essencial = essencial,
    recorrente = recorrente,
    observacao = observacao
)

fun Gasto.toEntity(): GastoEntity = GastoEntity(
    id = id,
    descricao = descricao,
    valor = valor,
    dataGasto = dataGasto,
    mesReferencia = mesReferencia,
    anoReferencia = anoReferencia,
    categoria = categoria.toEntity(),
    essencial = essencial,
    recorrente = recorrente,
    observacao = observacao
)

private fun CategoriaGastoEntity.toDomain(): CategoriaGasto = when (this) {
    CategoriaGastoEntity.MORADIA          -> CategoriaGasto.MORADIA
    CategoriaGastoEntity.ALIMENTACAO      -> CategoriaGasto.ALIMENTACAO
    CategoriaGastoEntity.TRANSPORTE       -> CategoriaGasto.TRANSPORTE
    CategoriaGastoEntity.SAUDE            -> CategoriaGasto.SAUDE
    CategoriaGastoEntity.EDUCACAO         -> CategoriaGasto.EDUCACAO
    CategoriaGastoEntity.LAZER            -> CategoriaGasto.LAZER
    CategoriaGastoEntity.CONTAS           -> CategoriaGasto.CONTAS
    CategoriaGastoEntity.OUTROS           -> CategoriaGasto.OUTROS
    CategoriaGastoEntity.SONHO_REALIZADO  -> CategoriaGasto.SONHO_REALIZADO
}

private fun CategoriaGasto.toEntity(): CategoriaGastoEntity = when (this) {
    CategoriaGasto.MORADIA         -> CategoriaGastoEntity.MORADIA
    CategoriaGasto.ALIMENTACAO     -> CategoriaGastoEntity.ALIMENTACAO
    CategoriaGasto.TRANSPORTE      -> CategoriaGastoEntity.TRANSPORTE
    CategoriaGasto.SAUDE           -> CategoriaGastoEntity.SAUDE
    CategoriaGasto.EDUCACAO        -> CategoriaGastoEntity.EDUCACAO
    CategoriaGasto.LAZER           -> CategoriaGastoEntity.LAZER
    CategoriaGasto.CONTAS          -> CategoriaGastoEntity.CONTAS
    CategoriaGasto.OUTROS          -> CategoriaGastoEntity.OUTROS
    CategoriaGasto.SONHO_REALIZADO -> CategoriaGastoEntity.SONHO_REALIZADO
}
