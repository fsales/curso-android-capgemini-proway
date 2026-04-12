package com.fsales.app.rumo.core.data.repository.mapper

import com.fsales.app.rumo.core.data.room.entity.GanhoEntity
import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.TipoGanho
import com.fsales.app.rumo.core.data.room.entity.enums.TipoGanho as TipoGanhoEntity

fun GanhoEntity.toDomain(): Ganho = Ganho(
    id = id,
    descricao = descricao,
    valor = valor,
    dataRecebimento = dataRecebimento,
    mesReferencia = mesReferencia,
    anoReferencia = anoReferencia,
    tipo = tipo.toDomain(),
    recorrente = recorrente,
    observacao = observacao
)

fun Ganho.toEntity(): GanhoEntity = GanhoEntity(
    id = id,
    descricao = descricao,
    valor = valor,
    dataRecebimento = dataRecebimento,
    mesReferencia = mesReferencia,
    anoReferencia = anoReferencia,
    tipo = tipo.toEntity(),
    recorrente = recorrente,
    observacao = observacao
)

private fun TipoGanhoEntity.toDomain(): TipoGanho = when (this) {
    TipoGanhoEntity.SALARIO -> TipoGanho.SALARIO
    TipoGanhoEntity.RENDA_EXTRA -> TipoGanho.RENDA_EXTRA
    TipoGanhoEntity.INVESTIMENTO -> TipoGanho.INVESTIMENTO
    TipoGanhoEntity.PRESENTE -> TipoGanho.PRESENTE
    TipoGanhoEntity.OUTRO -> TipoGanho.OUTRO
}

private fun TipoGanho.toEntity(): TipoGanhoEntity = when (this) {
    TipoGanho.SALARIO -> TipoGanhoEntity.SALARIO
    TipoGanho.RENDA_EXTRA -> TipoGanhoEntity.RENDA_EXTRA
    TipoGanho.INVESTIMENTO -> TipoGanhoEntity.INVESTIMENTO
    TipoGanho.PRESENTE -> TipoGanhoEntity.PRESENTE
    TipoGanho.OUTRO -> TipoGanhoEntity.OUTRO
}
