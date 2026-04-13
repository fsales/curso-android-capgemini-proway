package com.fsales.app.rumo.core.data.repository.mapper

import com.fsales.app.rumo.core.data.room.entity.SonhoEntity
import com.fsales.app.rumo.core.domain.model.PrioridadeSonho
import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.core.data.room.entity.enums.PrioridadeSonho as PrioridadeSonhoEntity

fun SonhoEntity.toDomain(): Sonho = Sonho(
    id          = id,
    titulo      = titulo,
    descricao   = descricao,
    valorMeta   = valorMeta,
    prioridade  = prioridade.toDomain(),
    prazoAlvo   = prazoAlvo,
    dataCriacao = dataCriacao,
    concluido   = concluido,
)

fun Sonho.toEntity(): SonhoEntity = SonhoEntity(
    id          = id,
    titulo      = titulo,
    descricao   = descricao,
    valorMeta   = valorMeta,
    prioridade  = prioridade.toEntity(),
    prazoAlvo   = prazoAlvo,
    dataCriacao = dataCriacao,
    concluido   = concluido,
)

private fun PrioridadeSonhoEntity.toDomain(): PrioridadeSonho = when (this) {
    PrioridadeSonhoEntity.BAIXA -> PrioridadeSonho.BAIXA
    PrioridadeSonhoEntity.MEDIA -> PrioridadeSonho.MEDIA
    PrioridadeSonhoEntity.ALTA -> PrioridadeSonho.ALTA
}

private fun PrioridadeSonho.toEntity(): PrioridadeSonhoEntity = when (this) {
    PrioridadeSonho.BAIXA -> PrioridadeSonhoEntity.BAIXA
    PrioridadeSonho.MEDIA -> PrioridadeSonhoEntity.MEDIA
    PrioridadeSonho.ALTA -> PrioridadeSonhoEntity.ALTA
}

